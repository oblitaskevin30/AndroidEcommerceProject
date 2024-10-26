package com.example.proyectoecommerce

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDAO {
    @Insert
    fun saveProduct(product:Product)

    @Delete
    fun deleteProduct(product: Product)

    @Query("SELECT * FROM product_table")
    fun getProduct(): List<Product>
}