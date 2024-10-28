package com.example.proyectoecommerce

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val idProduct:Int = 0,
    val nombre : String,
    val cantidad : Int,
    val categoria : String,
    val tiendas : String,
    val prioridad : Boolean,
    val notasAdicionales : String,
    var comprado : Boolean = false
)
