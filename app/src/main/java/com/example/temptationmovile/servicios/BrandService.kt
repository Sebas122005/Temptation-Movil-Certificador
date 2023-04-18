package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.Brand
import retrofit2.Call
import retrofit2.http.*

interface BrandService {
    @GET("brands")
    fun MostrarBrand(): Call<List<Brand>>


    @POST("brand")
    fun RegistrarBrand(@Body b:Brand): Call<Brand>?

    @PUT("brand/{id}")
    fun ActualizarBrand(@Path("id") id: Long, @Body b:Brand): Call<List<Brand>?>?


    @DELETE("brand/{id}")
    fun EliminarBrand(@Path("id") id: Long ): Call<List<Brand>?>?
}