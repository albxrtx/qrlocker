package com.example.qrlockerapp.retrofit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ReservaViewModel : ViewModel() {
    fun checkTaquilla(idTaquilla: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.obtenerEstadoTaquilla(idTaquilla)
                if (respuesta.ok && respuesta.row != null) {
                    onResult(!respuesta.row.reservado, null)
                } else {
                    onResult(false, respuesta.error)
                }
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun crearReserva(idTaquilla: String, fechaFin: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val body = mapOf("fecha_fin" to fechaFin)

                Log.d("API_CALL", "POST /reservas/$idTaquilla")
                Log.d("API_CALL", "Body enviado: $body")

                val respuesta = RetrofitClient.apiService.crearReserva(idTaquilla, body)

                Log.d("API_CALL", "Respuesta recibida: $respuesta")
                if (respuesta.ok) {
                    onResult(true, "Reserva creada correctamente")
                } else {
                    onResult(false, respuesta.error ?: "Error desconocido")
                }
            } catch (e: Exception) {
                Log.e("API_CALL", "ERROR: ${e.message}")
                onResult(false, e.message)
            }
        }
    }
}