package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.Size
import retrofit2.Call
import retrofit2.http.*

interface SizeService {

    @GET("sizes")
    fun Mostrarsizes(): Call<List<Size>>


    @POST("size")
    fun Registrarsize(@Body b: Size): Call<Size>?

    @PUT("size/{id}")
    fun Actualizarsize(@Path("id") id: Long, @Body b: Size): Call<List<Size>?>?


    @DELETE("size/{id}")
    fun Eliminarrsize(@Path("id") id: Long ): Call<List<Size>?>?
}