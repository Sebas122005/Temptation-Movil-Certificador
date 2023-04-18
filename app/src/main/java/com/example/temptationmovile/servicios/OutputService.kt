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
    fun ActualizarProduct(@Path("idout") id: Long, @Body p: Output): Call<List<Output>?>?

}