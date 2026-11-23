package com.example.lumis.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

data class EcopontoOsm(
    val osmId: String,
    val nome: String,
    val endereco: String,
    val latitude: Double,
    val longitude: Double,
    val distanciaMetros: Int,
    val horario: String,
    val materiais: String
)

object OverpassEcopontoRepository {
    private const val ENDPOINT = "https://overpass-api.de/api/interpreter"

    suspend fun buscarProximos(latitude: Double, longitude: Double, raioMetros: Int = 10_000): List<EcopontoOsm> =
        withContext(Dispatchers.IO) {
            val query = """
                [out:json][timeout:25];
                nwr(around:$raioMetros,$latitude,$longitude)["amenity"="recycling"];
                out center tags;
            """.trimIndent()
            val resposta = executarConsulta("data=${URLEncoder.encode(query, Charsets.UTF_8.name())}")
            parsearResposta(resposta, latitude, longitude)
        }

    private suspend fun executarConsulta(body: String): String {
        var ultimaFalha: Exception? = null
        repeat(3) { tentativa ->
            val connection = (URL(ENDPOINT).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 15_000
                readTimeout = 30_000
                doOutput = true
                setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                setRequestProperty("Accept", "application/json")
                setRequestProperty("User-Agent", "LumisApp/1.0")
            }
            try {
                connection.outputStream.bufferedWriter(Charsets.UTF_8).use { it.write(body) }
                val codigo = connection.responseCode
                if (codigo in 200..299) {
                    return connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                }
                // Lê o corpo de erro para diagnóstico (sem dados sensíveis)
                val errorBody = connection.errorStream.bufferedReader(Charsets.UTF_8).readText().takeIf { it.isNotBlank() } ?: "Sem corpo de erro"
                ultimaFalha = IllegalStateException("O serviço de ecopontos respondeu com $codigo. Corpo do erro: ${errorBody.take(200)}")
                // Apenas erros transitórios merecem retry: 429 (Too Many Requests), 502/503/505 (Server Errors)
                if (codigo !in listOf(429, 502, 503, 504)) throw ultimaFalha
            } catch (erro: Exception) {
                // Não repita para erros não transitórios (ex: 406 Not Acceptable)
                if (erro is IllegalStateException && erro.message?.startsWith("O serviço de ecopontos respondeu com") == true) {
                    throw erro
                }
                ultimaFalha = erro
            } finally {
                connection.disconnect()
            }
            if (tentativa < 2) delay(750L * (1 shl tentativa))
        }
        throw ultimaFalha ?: IllegalStateException("Não foi possível consultar os ecopontos.")
    }

    private fun parsearResposta(json: String, origemLat: Double, origemLon: Double): List<EcopontoOsm> {
        val elementos = JSONObject(json).optJSONArray("elements") ?: return emptyList()
        return buildList {
            for (indice in 0 until elementos.length()) {
                val elemento = elementos.optJSONObject(indice) ?: continue
                val centro = elemento.optJSONObject("center")
                val latitude = when {
                    elemento.has("lat") -> elemento.optDouble("lat", Double.NaN)
                    centro != null -> centro.optDouble("lat", Double.NaN)
                    else -> Double.NaN
                }
                val longitude = when {
                    elemento.has("lon") -> elemento.optDouble("lon", Double.NaN)
                    centro != null -> centro.optDouble("lon", Double.NaN)
                    else -> Double.NaN
                }
                if (!latitude.isFinite() || !longitude.isFinite()) continue

                val tags = elemento.optJSONObject("tags") ?: JSONObject()
                val tipo = elemento.optString("type", "osm")
                val id = elemento.optLong("id")
                add(
                    EcopontoOsm(
                        osmId = "$tipo/$id",
                        nome = tags.optString("name").ifBlank { tags.optString("operator").ifBlank { "Ponto de reciclagem" } },
                        endereco = montarEndereco(tags),
                        latitude = latitude,
                        longitude = longitude,
                        distanciaMetros = distanciaEmMetros(origemLat, origemLon, latitude, longitude),
                        horario = tags.optString("opening_hours").ifBlank { "Horário não informado" },
                        materiais = materiaisAceitos(tags)
                    )
                )
            }
        }.distinctBy { it.osmId }.sortedBy { it.distanciaMetros }.take(20)
    }

    private fun montarEndereco(tags: JSONObject): String {
        val rua = tags.optString("addr:street")
        val numero = tags.optString("addr:housenumber")
        val bairro = tags.optString("addr:suburb")
        val cidade = tags.optString("addr:city")
        return listOf(
            listOf(rua, numero).filter { it.isNotBlank() }.joinToString(", "),
            bairro,
            cidade
        ).filter { it.isNotBlank() }.joinToString(" • ").ifBlank { "Endereço não informado no OpenStreetMap" }
    }

    private fun materiaisAceitos(tags: JSONObject): String {
        val nomes = linkedMapOf(
            "recycling:paper" to "papel",
            "recycling:cardboard" to "papelão",
            "recycling:glass" to "vidro",
            "recycling:glass_bottles" to "garrafas de vidro",
            "recycling:plastic" to "plástico",
            "recycling:plastic_bottles" to "garrafas plásticas",
            "recycling:metal" to "metal",
            "recycling:cans" to "latas",
            "recycling:batteries" to "pilhas e baterias",
            "recycling:electrical_appliances" to "eletrônicos",
            "recycling:clothes" to "roupas",
            "recycling:shoes" to "calçados",
            "recycling:used_car_oil" to "óleo automotivo",
            "recycling:cooking_oil" to "óleo de cozinha"
        )
        val encontrados = nomes.filterKeys { tags.optString(it).equals("yes", true) }.values
        return encontrados.joinToString(", ").ifBlank {
            tags.optString("recycling_type").ifBlank { "Materiais não detalhados no OpenStreetMap" }
        }
    }

    private fun distanciaEmMetros(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val raioTerra = 6_371_000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
        return (raioTerra * 2 * atan2(sqrt(a), sqrt(1 - a))).roundToInt()
    }
}
