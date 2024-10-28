package com.example.proyectoecommerce

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDAO {
    @Insert
    fun saveProduct(product:Product)

    @Delete
    fun deleteProduct(product: Product)

    @Query("SELECT * FROM product_table")
    fun getProduct(): List<Product>

    @Update
    fun updateProduct(product: Product)

    @Query("SELECT * FROM product_table WHERE categoria = :categoria")
    fun getProductsByCategoria(categoria: String): List<Product>

    @Query("SELECT * FROM product_table WHERE tiendas = :tienda")
    fun getProductsByTiendas(tienda: String): List<Product>


    @Query("SELECT * FROM product_table WHERE categoria IN (:TiendasList)")
    fun getProductsByMultiplesTiendas(TiendasList: List<String>): List<Product>

}