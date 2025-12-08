package com.example.qrlockerapp.retrofit

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
                val respuesta = RetrofitClient.apiService.crearReserva(idTaquilla, body)
                onResult(respuesta.ok, respuesta.message ?: respuesta.error)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }
}