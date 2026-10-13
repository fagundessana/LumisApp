package com.example.lumis.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

// ---------------------------------------------------------------------------------------------
// HELPER DE LOCALIZAÇÃO HÍBRIDA (texto livre + GPS)
// - Se o usuário concedeu FINE (exata): usa PRIORITY_HIGH_ACCURACY.
// - Se concedeu só COARSE (aproximada): usa PRIORITY_BALANCED_POWER_ACCURACY.
// - O Android 12+ mostra sozinho o switch "Usar localização exata" quando
//   pedimos as duas permissões juntas via RequestMultiplePermissions.
// ---------------------------------------------------------------------------------------------

object LocalizacaoHelper {

    fun temAlgumaPermissao(context: Context): Boolean {
        return temAproximada(context) || temExata(context)
    }

    fun temAproximada(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun temExata(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Retorna "Cidade, Estado" (ex.: "São Paulo, São Paulo") ou lança exceção
     * com mensagem amigável para exibir na UI.
     */
    suspend fun obterRegiaoAtual(context: Context): String {
        val exata = temExata(context)
        val aproximada = temAproximada(context)
        if (!exata && !aproximada) {
            throw SecurityException("Permita o acesso à localização para detectar sua região.")
        }

        val prioridade = if (exata) {
            Priority.PRIORITY_HIGH_ACCURACY
        } else {
            Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }

        // 1) Tenta posição atual; se nula, cai para lastLocation.
        val local: Location? = try {
            pedirPosicaoAtual(context, prioridade)
        } catch (_: Exception) {
            null
        } ?: try {
            pedirUltimaPosicao(context)
        } catch (_: Exception) {
            null
        }

        if (local == null) {
            throw IllegalStateException("Não consegui obter sua posição. Ative o GPS e tente de novo.")
        }

        // 2) Converte lat/lon em "Cidade, Estado".
        return formatarRegiao(context, local.latitude, local.longitude)
    }

    private suspend fun pedirPosicaoAtual(context: Context, prioridade: Int): Location? =
        suspendCancellableCoroutine { cont ->
            try {
                val client = LocationServices.getFusedLocationProviderClient(context)
                val tokenSource = CancellationTokenSource()
                cont.invokeOnCancellation { tokenSource.cancel() }
                client.getCurrentLocation(prioridade, tokenSource.token)
                    .addOnSuccessListener { loc -> if (cont.isActive) cont.resume(loc) }
                    .addOnFailureListener { if (cont.isActive) cont.resume(null) }
            } catch (_: Exception) {
                if (cont.isActive) cont.resume(null)
            }
        }

    private suspend fun pedirUltimaPosicao(context: Context): Location? =
        suspendCancellableCoroutine { cont ->
            try {
                val client = LocationServices.getFusedLocationProviderClient(context)
                client.lastLocation
                    .addOnSuccessListener { loc -> if (cont.isActive) cont.resume(loc) }
                    .addOnFailureListener { if (cont.isActive) cont.resume(null) }
            } catch (_: Exception) {
                if (cont.isActive) cont.resume(null)
            }
        }

    private suspend fun formatarRegiao(context: Context, lat: Double, lon: Double): String =
        withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val enderecos = buscarEnderecos(geocoder, lat, lon)

                val primeiro = enderecos.firstOrNull()
                if (primeiro == null) {
                    throw IllegalStateException("Não encontrei sua cidade. Digite manualmente.")
                }

                val cidade = primeiro.locality
                    ?: primeiro.subAdminArea
                    ?: primeiro.adminArea
                val estado = primeiro.adminArea

                when {
                    cidade.isNullOrBlank() && estado.isNullOrBlank() ->
                        throw IllegalStateException("Não encontrei sua cidade. Digite manualmente.")
                    !cidade.isNullOrBlank() && !estado.isNullOrBlank() && !cidade.equals(estado, ignoreCase = true) ->
                        "$cidade, $estado"
                    !cidade.isNullOrBlank() -> cidade
                    else -> estado!!
                }
            } catch (e: SecurityException) {
                throw e
            } catch (e: IllegalStateException) {
                throw e
            } catch (_: Exception) {
                throw IllegalStateException("Falha ao buscar sua cidade. Digite manualmente.")
            }
        }

    @Suppress("DEPRECATION")
    private suspend fun buscarEnderecos(
        geocoder: Geocoder,
        lat: Double,
        lon: Double
    ): List<android.location.Address> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { cont ->
                try {
                    geocoder.getFromLocation(lat, lon, 1, object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<android.location.Address>) {
                            if (cont.isActive) cont.resume(addresses.toList())
                        }

                        override fun onError(errorMessage: String?) {
                            if (cont.isActive) cont.resume(emptyList())
                        }
                    })
                } catch (_: Exception) {
                    if (cont.isActive) cont.resume(emptyList())
                }
            }
        } else {
            try {
                geocoder.getFromLocation(lat, lon, 1) ?: emptyList()
            } catch (_: Exception) {
                emptyList()
            }
        }
    }
}
