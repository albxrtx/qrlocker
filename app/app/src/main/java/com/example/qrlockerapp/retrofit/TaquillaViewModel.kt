package com.example.qrlockerapp.retrofit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrlockerapp.model.Taquilla
import kotlinx.coroutines.launch

class TaquillaViewModel: ViewModel() {

    fun obtenerEstado(idTaquilla: String, onResult: (Taquilla?, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.obtenerEstadoTaquilla(idTaquilla)
                Log.d("HomeScreen", "Respuesta del backend: $respuesta")
                if (respuesta.ok && respuesta.row != null) {
                    onResult(respuesta.row, null)
                } else {
                    onResult(null, respuesta.error ?: "No devuelve row")
                }
            } catch (e: Exception) {
                onResult(null, e.message ?: "Excepción desconocida")
            }
        }
    }

}