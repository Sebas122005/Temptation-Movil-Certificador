package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.Income
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IncomeService {

    @GET("incomes")
    fun MostrarIncomes(): Call<List<Income>>

    @POST("income")
    fun RegistrarIncome(@Body p: Income): Call<Income>

    @PUT("income/{idincome}")
    fun ActualizarIncome(@Path("idincome") id: Long, @Body p: Income): Call<List<Income>?>?

    @DELETE("income/{idincome}")
    fun EliminarIncome(@Path("idincome") id:Long): Call<List<Income>?>?
}