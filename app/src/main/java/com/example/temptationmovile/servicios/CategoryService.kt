package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Category
import retrofit2.Call
import retrofit2.http.*

interface CategoryService {

    @GET("categorys")
    fun MostrarCategory(): Call<List<Category>>

    @POST("category")
    fun RegistrarCategory(@Body b: Category): Call<Category>?

    @PUT("category/{idcat}")
    fun ActualizarCategory(@Path("idcat") id: Long, @Body b: Category): Call<Category>?


    @DELETE("category/{idcat}")
    fun EliminarCategory(@Path("idcat") id: Long ): Call<Category>?
}