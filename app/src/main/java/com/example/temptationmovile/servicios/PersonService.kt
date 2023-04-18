package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.Person
import com.example.temptationmovile.clases.ResponseLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PersonService {
    @POST("login")
    fun login(@Body responseLogin: ResponseLogin?): Call<ResponseLogin>?

    @GET("persons")
    fun MostrarUsuarios():Call<List<Person>>

    @POST("addperson")
    fun RegistrarUsuario(@Body p:Person):Call<Person>

    @PUT("person/{idperson}")
    fun ActualizarUsuario(@Path("idperson") idperson:Long,@Body p:Person):Call<List<Person>?>?

    @DELETE("person/{idperson}")
    fun EliminarUsuario(@Path("idperson")idperson:Long):Call<List<Person>?>?


}