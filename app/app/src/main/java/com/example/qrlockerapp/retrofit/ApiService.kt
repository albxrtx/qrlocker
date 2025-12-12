package com.example.qrlockerapp.retrofit

import com.example.qrlockerapp.model.RespuestaReserva
import com.example.qrlockerapp.model.RespuestaTaquilla
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body

interface ApiService {

    /**
     * Obtiene el estado de una taquilla específica.
     *
     * @param idTaquilla El ID de la taquilla a consultar.
     * @return Un objeto [RespuestaTaquilla] que contiene la información de la taquilla.
     */
    @GET("taquillas/{id_taquilla}")
    suspend fun obtenerEstadoTaquilla(
        @Path("id_taquilla") idTaquilla: String
    ): RespuestaTaquilla

    /**
     * Crea una nueva reserva para una taquilla.
     *
     * @param idTaquilla El ID de la taquilla para la que se crea la reserva.
     * @param reserva Un mapa que contiene los datos de la reserva (ej. fecha de fin).
     * @return Un objeto [RespuestaReserva] que contiene el resultado de la operación.
     */
    @POST("reservas/{id_taquilla}")
    suspend fun crearReserva(
        @Path("id_taquilla") idTaquilla: String,
        @Body reserva: Map<String, String>
    ): RespuestaReserva
}
