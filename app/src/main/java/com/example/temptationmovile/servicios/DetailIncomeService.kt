package com.example.temptationmovile.servicios

import com.example.temptationmovile.clases.DetailIncome
import retrofit2.Call
import retrofit2.http.*

interface DetailIncomeService {

    @GET("detailincomes")
    fun MostrarDetailIncomes(): Call<List<DetailIncome>>

    @POST("detailincome")
    fun RegistrarDetailIncome(@Body p: DetailIncome): Call<DetailIncome>?

    @PUT("detailincome/{iddetincome}")
    fun ActualizarDetailIncome(@Path("iddetincome") id: Long, @Body p: DetailIncome): Call<List<DetailIncome>?>?

    @DELETE("detailincome/{iddetincome}")
    fun EliminarDetailIncome(@Path("iddetincome") id:Long): Call<List<DetailIncome>?>?
}