package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.Color
import retrofit2.Call
import retrofit2.http.*

interface ColorService {
    @GET("colors")
    fun MostrarColor(): Call<List<Color>>

    @POST("color")
    fun RegistrarColor(@Body b: Color): Call<Color>?

    @PUT("color/{idcolor}")
    fun ActualizarColor(@Path("idcolor") id: Long, @Body b: Color): Call<List<Color>?>?

    @DELETE("color/{idcolor}")
    fun EliminarColor(@Path("idcolor") id: Long ): Call<List<Color>?>?
}