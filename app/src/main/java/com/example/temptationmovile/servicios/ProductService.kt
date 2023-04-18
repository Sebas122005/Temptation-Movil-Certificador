package com.example.temptationmovile.servicios


import com.example.temptationmovile.clases.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductService {

    @GET("products")
    fun MostrarProduct(): Call<List<Product>>

    @POST("addproduct")
    fun RegistrarProduct(@Body p: Product): Call<Product>


    @PUT("product/{idproduc}")
    fun ActualizarProduct(@Path("idproduc") id: Long, @Body p:Product): Call<List<Product>?>?



    @DELETE("product/{idproduc}")
    fun EliminarProduct(@Path("idproduc") id:Long): Call<List<Product>?>?
}