package com.example.lumis.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class EnderecoCep(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val cidade: String,
    val uf: String
)

object CepService {
    suspend fun buscar(cepInformado: String): EnderecoCep = withContext(Dispatchers.IO) {
        val cep = cepInformado.filter(Char::isDigit)
        require(cep.length == 8) { "Informe um CEP com 8 números" }

        val conexao = URL("https://viacep.com.br/ws/$cep/json/").openConnection() as HttpURLConnection
        try {
            conexao.requestMethod = "GET"
            conexao.connectTimeout = 6_000
            conexao.readTimeout = 6_000
            conexao.setRequestProperty("Accept", "application/json")

            if (conexao.responseCode !in 200..299) error("Não foi possível consultar o CEP")
            val resposta = conexao.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(resposta)
            if (json.optBoolean("erro", false)) error("CEP não encontrado")

            EnderecoCep(
                cep = json.optString("cep"),
                logradouro = json.optString("logradouro"),
                bairro = json.optString("bairro"),
                cidade = json.optString("localidade"),
                uf = json.optString("uf")
            )
        } finally {
            conexao.disconnect()
        }
    }
}
