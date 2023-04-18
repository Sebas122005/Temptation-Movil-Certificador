package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.Provider
import retrofit2.Call
import retrofit2.http.*

interface ProviderService {
    @GET("providers")
    fun MostrarProvider(): Call<List<Provider>?>?


    @POST("addprovider")
    fun RegistrarProvider(@Body b: Provider): Call<Provider>?

    @PUT("provider/{idprovider}")
    fun ActualizarProvider(@Path("idprovider") id: Long, @Body b: Provider): Call<List<Provider>?>?


    @DELETE("provider/{idprovider}")
    fun EliminarrProvider(@Path("idprovider") id: Long ): Call<List<Provider>?>?
}