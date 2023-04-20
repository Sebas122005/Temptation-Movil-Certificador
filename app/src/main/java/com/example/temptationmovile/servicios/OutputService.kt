package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.Output

import retrofit2.Call
import retrofit2.http.*

interface OutputService {
    @GET("outputs")
    fun MostrarOutputs(): Call<List<Output>>

    @POST("output")
    fun RegistrarOutput(@Body p: Output): Call<Output>

    @PUT("output/{idout}")
    fun ActualizarOutput(@Path("idout") idout: Long, @Body p: Output): Call<Output>?

    @DELETE("output/{idout}")
    fun EliminarOutput(@Path("idout") idout:Long):Call<Output>?
}