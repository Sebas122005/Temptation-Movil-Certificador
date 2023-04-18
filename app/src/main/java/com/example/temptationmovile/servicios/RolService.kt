package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.Rol
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RolService {

    @GET("roles")
    fun MostrarRol():Call<List<Rol>>
    @GET("role/{id}")
    fun MostrarRolEspec(@Path("id")id: Long):Call<Rol>?
    @POST("role")
    fun RegistrarRol(@Body r:Rol):Call<Rol>?
    @PUT("role/{id}")
    fun ActualizarRol(@Path("id")id:Long,@Body r: Rol):Call<List<Rol>?>?

    @DELETE("role/{id}")
    fun EliminarRol(@Path("id")id: Long):Call<List<Rol>?>?



}