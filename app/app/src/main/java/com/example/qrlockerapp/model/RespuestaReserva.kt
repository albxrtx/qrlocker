package com.example.qrlockerapp.model

data class RespuestaReserva(
    val ok: Boolean,
    val reserva: Reserva? = null,
    val message: String? = null,
    val error: String? = null
)
