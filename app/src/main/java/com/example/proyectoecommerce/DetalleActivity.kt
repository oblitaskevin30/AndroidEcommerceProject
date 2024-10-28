package com.example.proyectoecommerce

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectoecommerce.databinding.ActivityDetalleBinding
import com.example.proyectoecommerce.databinding.ActivityMainBinding

class DetalleActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetalleBinding
    lateinit var dataBase: ProductSingletonDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = ProductSingletonDataBase.getDataBase(this)

        val idProduct = intent.getIntExtra("idProduct", 0)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val cantidad = intent.getIntExtra("cantidad", 0)
        val categoria = intent.getStringExtra("categoria") ?: ""
        val tiendas = intent.getStringExtra("tiendas") ?: ""
        val prioridad = intent.getBooleanExtra("prioridad", false)
        val notasAdicionales = intent.getStringExtra("notasAdicionales") ?: ""
        val comprado = intent.getBooleanExtra("comprado", false)



        binding.nombreProducto.text = nombre
        binding.cantidad.text = "Cantidad: $cantidad"
        binding.categoria.text = "Categoría: $categoria"
        binding.tienda.text = "Tiendas: $tiendas"
        binding.prioridad.text = if (prioridad) "Prioridad: Alta" else "Prioridad: Baja"
        binding.notasAdicionales.text = "Notas: $notasAdicionales"

        binding.btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        binding.btnEliminar.setOnClickListener {
            val producto = Product(idProduct=idProduct,nombre = nombre, cantidad = cantidad, categoria = categoria,
                tiendas = tiendas, prioridad = prioridad, notasAdicionales = notasAdicionales , comprado = comprado)
            showAlertDialog(producto)
        }

        binding.btnEditar.setOnClickListener {

            val intent = Intent(this, CrearActivity::class.java)
            intent.putExtra("idProduct", idProduct)
            intent.putExtra("nombre", nombre)
            intent.putExtra("cantidad", cantidad)
            intent.putExtra("categoria", categoria)
            intent.putExtra("tiendas", tiendas)
            intent.putExtra("prioridad", prioridad)
            intent.putExtra("notasAdicionales",notasAdicionales)
            intent.putExtra("comprado", comprado)

            this.startActivity(intent)

        }

    }

    fun showAlertDialog(product: Product) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Quieres Eliminar el producto ${product.nombre}?")

        builder.setPositiveButton("Confirmar") { dialog, _ ->
            dataBase.productDao().deleteProduct(product)
            val next = Intent(this,ActivityMainBinding::class.java)
            startActivity(next)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->

            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}