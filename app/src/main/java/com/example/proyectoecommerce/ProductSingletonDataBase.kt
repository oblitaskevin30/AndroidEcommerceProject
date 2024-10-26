package com.example.proyectoecommerce

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Product::class
    ],
    version = 1,
    exportSchema = false
)

abstract class ProductSingletonDataBase : RoomDatabase() {
    abstract fun productDao () : ProductDAO

    companion object {

        @Volatile
        private var INSTANCE:  ProductSingletonDataBase ? = null

        fun getDataBase( context : Context) : ProductSingletonDataBase{
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductSingletonDataBase::class.java,
                    "app_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE=instance
                instance
            }
        }

    }
}
