package com.example.qrlockerapp.retrofit

import com.example.qrlockerapp.model.RespuestaReserva
import com.example.qrlockerapp.model.RespuestaTaquilla
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body

interface ApiService{

    @GET("taquillas/{id_taquilla}")
    suspend fun obtenerEstadoTaquilla(
        @Path("id_taquilla") idTaquilla:String
    ): RespuestaTaquilla

    @POST("reservas/{id_taquilla}")
    suspend fun crearReserva(
        @Path("id_taquilla") idTaquilla: String,
        @Body reserva: Map<String, String>
    ): RespuestaReserva
}