package com.example.proyectoecommerce

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoecommerce.databinding.ActivityCrearBinding
import com.example.proyectoecommerce.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip


class CrearActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCrearBinding
    private lateinit var database : ProductSingletonDataBase
    private  var categoriaSelected = ""
    private var categoriaIdSelected:Int = 0

    private var productName : String = ""
    private var productCant : String = ""
    private var category : String = ""
    private var store : String =""
    private var priority : Boolean = false
    private var adicionalText : String = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = ProductSingletonDataBase.getDataBase(this)

        val categoriasList = intent.getStringArrayListExtra("CategoriasList")?: arrayListOf()
        val tiendaList = intent.getStringArrayListExtra("TiendaList")?: arrayListOf()
        var categoriasId = intent.getIntegerArrayListExtra("CategoriasId")?: arrayListOf()

        makeChipsTienda(tiendaList)
        configCategoria(categoriasList,categoriasId)

        val idProduct = intent.getIntExtra("idProduct", 0)
        if (idProduct != 0) {
            // Solo si idProduct no es 0, significa que se está editando
            val nombre = intent.getStringExtra("nombre") ?: ""
            val cantidad = intent.getIntExtra("cantidad", 0)
            val categoria = intent.getStringExtra("categoria") ?: ""
            val tiendas = intent.getStringExtra("tiendas") ?: ""
            val prioridad = intent.getBooleanExtra("prioridad", false)
            val notasAdicionales = intent.getStringExtra("notasAdicionales") ?: ""


            binding.nombreProducto.setText(nombre)
            binding.cantidadProducto.setText(cantidad.toString())
            binding.spinerCategorias.setSelection(categoriasList.indexOf(categoria))

            tiendaList.forEach { chipText ->
                if (tiendas.contains(chipText)) {
                    binding.chipGroupTienda.findViewById<Chip>(chipText.hashCode()).isChecked = true
                }
            }
            binding.switchPrioridad.isChecked = prioridad
            binding.textAreaProducto.setText(notasAdicionales)
        }


        binding.btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnGuardar.setOnClickListener {
            productName = binding.nombreProducto.text.toString()
            productCant = binding.cantidadProducto.text.toString()
            category = binding.spinerCategorias.selectedItem?.toString()?:"sin categoria"
            adicionalText = binding.textAreaProducto.text.toString()

            if(productName.isNullOrEmpty()
                || category.isNullOrEmpty()
                || productCant.isNullOrEmpty())
            {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Cuidado!!")
                dialog.setMessage("Debes completar todos los campos para continuar")
                dialog.setCancelable(true)
                dialog.show()

            }else{

                var selectedChipsText = binding.chipGroupTienda.checkedChipIds.map { chipId ->
                    binding.chipGroupTienda.findViewById<Chip>(chipId).text.toString()
                }
                store = selectedChipsText.joinToString (",")

                priority = binding.switchPrioridad.isChecked

                var productoNuevo =
                    Product(
                        nombre = productName,
                        cantidad = productCant.toInt(),
                        categoria = category,
                        tiendas = store,
                        prioridad = priority,
                        notasAdicionales = adicionalText
                    )

                if (idProduct != 0) {
                    var idProduct = idProduct
                    var productoModificar =
                        Product(
                            idProduct=idProduct,
                            nombre = productName,
                            cantidad = productCant.toInt(),
                            categoria = category,
                            tiendas = store,
                            prioridad = priority,
                            notasAdicionales = adicionalText
                        )
                    val mensajeConfirmacion : String = "modificar"
                    showAlertDialogModificar(productoModificar,mensajeConfirmacion)
                } else {
                    val mensajeCreado : String = "crear"
                    showAlertDialogCrear(productoNuevo,mensajeCreado)
                }


                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }


    }

    fun makeChipsTienda(listaDeTiendas : ArrayList<String>){
        binding.chipGroupTienda.removeAllViews()
        listaDeTiendas.forEach { chipText ->
            val newChip = Chip(this)
            newChip.text = chipText
            newChip.isCheckable = true

            binding.chipGroupTienda.addView(newChip)
        }

    }

    private fun configCategoria(listCategorias: ArrayList<String>,categoriasIds: ArrayList<Int>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listCategorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinerCategorias.adapter = adapter

        // Obtener la categoria seleccionada
        binding.spinerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoriaSelected = listCategorias[position]
                categoriaIdSelected = categoriasIds[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                categoriaSelected = ""
                categoriaIdSelected = 0

            }

        }
    }

    fun showAlertDialogCrear(product: Product , mensaje : String) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Quieres ${mensaje} el producto ${product.nombre}?")

        builder.setPositiveButton("Confirmar") { dialog, _ ->
            database.productDao().saveProduct(product)
            val next = Intent(this, ActivityMainBinding::class.java)
            startActivity(next)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->

            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun showAlertDialogModificar(product: Product , mensaje : String) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Quieres ${mensaje} el producto ${product.nombre}?")

        builder.setPositiveButton("Confirmar") { dialog, _ ->
            database.productDao().updateProduct(product)
            val next = Intent(this, ActivityMainBinding::class.java)
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