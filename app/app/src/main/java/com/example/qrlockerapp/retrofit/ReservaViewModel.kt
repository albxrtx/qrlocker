package com.example.qrlockerapp.retrofit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ReservaViewModel : ViewModel() {

    fun crearReserva(idTaquilla: String, fechaFin: String, onResult: (Boolean, String?) -> Unit) {
        // Inicia una corutina para realizar la operación de red en un hilo secundario.
        viewModelScope.launch {
            try {
                // Prepara el cuerpo de la solicitud con la fecha de finalización.
                val body = mapOf("fecha_fin" to fechaFin)
                // Llama al método de la API para crear la reserva a través del cliente Retrofit.
                val respuesta = RetrofitClient.apiService.crearReserva(idTaquilla, body)

                // Comprueba si la respuesta de la API fue exitosa.
                if (respuesta.ok) {
                    // Invoca el callback con éxito (true) y un mensaje de confirmación.
                    onResult(true, "Reserva creada correctamente")
                } else {
                    // Invoca el callback con fallo (false) y el mensaje de error de la API, o un mensaje genérico.
                    onResult(false, respuesta.error ?: "Error desconocido")
                }
            } catch (e: Exception) {
                // Captura cualquier excepción que ocurra durante la llamada de red (ej. problemas de conexión).
                // Invoca el callback con fallo (false) y el mensaje de la excepción.
                onResult(false, e.message)
            }
        }
    }
}
