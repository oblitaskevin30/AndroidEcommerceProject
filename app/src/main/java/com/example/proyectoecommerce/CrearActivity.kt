package com.example.proyectoecommerce

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoecommerce.databinding.ActivityCrearBinding
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
                    Product(nombre = productName,
                        cantidad = productCant.toInt(),
                        categoria = category,
                        tiendas = store,
                        prioridad = priority,
                        notasAdicionales = adicionalText
                    )

                database.productDao().saveProduct(productoNuevo)

                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("excelente")
                dialog.setMessage("producto creado correctamente")
                dialog.setCancelable(true)
                dialog.show()

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

}