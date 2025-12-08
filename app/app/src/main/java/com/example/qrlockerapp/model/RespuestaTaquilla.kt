package com.example.qrlockerapp.model

data class RespuestaTaquilla(
    val ok: Boolean,
    val row: Taquilla? = null,
    val error: String? = null
)
