package com.example.proyectoecommerce
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceCategoria {

        @GET("getAllCategories")
        fun getAllCatergorias(): Call< List<Categoria> >

        @GET("getById")
        fun getCategoriaById(
            @Query("id") id: Int
        ): Call< Categoria >

}