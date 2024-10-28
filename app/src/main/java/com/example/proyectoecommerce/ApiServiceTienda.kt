package com.example.proyectoecommerce

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceTienda {
    @GET("getAllStores")
    fun getAllTienda(): Call<List<Tienda>>

    @GET("getById")
    fun getTiendaById(
        @Query("id") id: Int
    ): Call<Tienda>



}