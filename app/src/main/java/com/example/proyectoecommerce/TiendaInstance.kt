package com.example.proyectoecommerce
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TiendaInstance {
    private const val BASE_URL = "https://2782e730-dbaf-414a-8426-bc759689516f.mock.pstmn.io"

    private val logginInterception = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logginInterception)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiServiceTienda by lazy {
        retrofit.create(ApiServiceTienda::class.java)
    }
}