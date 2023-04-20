package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.Color
import com.example.temptationmovile.clases.Style
import retrofit2.Call
import retrofit2.http.*

interface StyleService {
    @GET("styles")
    fun MostrarEstilo(): Call<List<Style>>

    @POST("style")
    fun RegistrarEstilo(@Body b: Style): Call<Style>?

    @PUT("style/{idstyles}")
    fun ActualizarEstilo(@Path("idstyles") id: Long, @Body b: Style): Call<Style>?

    @DELETE("style/{idstyles}")
    fun EliminarEstilo(@Path("idstyles") id: Long ): Call<Style>?
}