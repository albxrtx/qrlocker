package com.example.qrlockerapp.retrofit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrlockerapp.model.Taquilla
import kotlinx.coroutines.launch

class TaquillaViewModel: ViewModel() {

    fun obtenerEstado(idTaquilla: String, onResult: (Taquilla?, String?) -> Unit) {
        // Inicia una corutina para realizar la operación de red en un hilo secundario.
        viewModelScope.launch {
            try {
                // Llama al método de la API para obtener el estado de la taquilla a través del cliente Retrofit.
                val respuesta = RetrofitClient.apiService.obtenerEstadoTaquilla(idTaquilla)
                // Comprueba si la respuesta de la API fue exitosa y si contiene datos.
                if (respuesta.ok && respuesta.row != null) {
                    // Invoca el callback con el objeto Taquilla y sin mensaje de error.
                    onResult(respuesta.row, null)
                } else {
                    // Invoca el callback con null y el mensaje de error de la API, o un mensaje genérico.
                    onResult(null, respuesta.error ?: "No devuelve row")
                }
            } catch (e: Exception) {
                // Captura cualquier excepción que ocurra durante la llamada de red (ej. problemas de conexión).
                // Invoca el callback con null y el mensaje de la excepción.
                onResult(null, e.message ?: "Excepción desconocida")
            }
        }
    }

}
