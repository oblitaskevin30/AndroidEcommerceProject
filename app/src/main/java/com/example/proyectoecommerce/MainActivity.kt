
package com.example.proyectoecommerce

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoecommerce.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var dataBase: ProductSingletonDataBase
    private  lateinit var  adapterProduct: ProductAdapter

    private var tiendaListSeleceted:MutableList<String> = mutableListOf()

    private var categoriaSeleccionada : String? = null

    private var categoriasCrearIDs: MutableList<Int> = mutableListOf()
    private var categoriasCrearList: MutableList<String> = mutableListOf()
    private var tiendaCrearList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = ProductSingletonDataBase.getDataBase(this)

        adapterProduct = ProductAdapter(dataBase.productDao().getProduct().toMutableList())
        binding.rvProduct.adapter = adapterProduct

        fetchCategorias()
        fetchTiendas()

        binding.btnCrear.setOnClickListener {

            val intent = Intent(this, CrearActivity::class.java)
            intent.putStringArrayListExtra("CategoriasList", ArrayList(categoriasCrearList))
            intent.putStringArrayListExtra("TiendaList", ArrayList(tiendaCrearList) )
            intent.putIntegerArrayListExtra("CategoriasId", ArrayList(categoriasCrearIDs) )
            startActivity(intent)

        }

        binding.btnEliminar.setOnClickListener{
            eliminarProductosSeleccionados()
        }

        binding.btnComprado.setOnClickListener(){
            marcarComoComprado()
        }


    }

    override fun onResume() {
        super.onResume()
        adapterProduct.actualizarLista(dataBase.productDao().getProduct().toMutableList())

    }

    //FETCH CATEGORIAS

    private fun fetchCategorias() {

        CategoriaInstance.api.getAllCatergorias()
            .enqueue(object : Callback<List<Categoria>> {
                override fun onResponse(
                    call: Call<List<Categoria>>,
                    response: Response<List<Categoria>>
                ) {
                    if (response.isSuccessful) {
                        val listCategoria = response.body() ?: emptyList()
                        makechipsCategorias(listCategoria)

                    } else {
                        // error controlado
                        showError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                    // error no controlado
                    showError("Error: ${t.message}")
                }

            })

    }

    fun makechipsCategorias(listCategorias : List<Categoria>){
        binding.chipGroupCategorias.removeAllViews()
        listCategorias.forEach { chipCat ->
            val newChip = Chip(this)
                newChip.text = chipCat.name
                newChip.isCheckable = true
                categoriasCrearList.add(chipCat.name)
                categoriasCrearIDs.add(chipCat.id)

                newChip.setOnCheckedChangeListener{_,isChecked->
                    if(isChecked){

                        categoriaSeleccionada = chipCat.name

                    }else{
                        if(categoriaSeleccionada == chipCat.name){
                            categoriaSeleccionada = null
                        }

                    }

                    filtrarPorCategoria()
                }

                binding.chipGroupCategorias.addView(newChip)
            }

    }

    //FETCH TIENDAS
    private fun fetchTiendas() {
        TiendaInstance.api.getAllTienda()
            .enqueue(object : Callback<List<Tienda>> {
                override fun onResponse(
                    call: Call<List<Tienda>>,
                    response: Response<List<Tienda>>
                ) {
                    if (response.isSuccessful) {
                        val listTienda = response.body() ?: emptyList()
                        makechipsTienda(listTienda)

                    } else {
                        // error controlado
                        showError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Tienda>>, t: Throwable) {
                    // error no controlado
                    showError("Error: ${t.message}")
                }

            })

    }


    fun makechipsTienda(listaTienda : List<Tienda>){
        binding.chipGroupTiendas.removeAllViews()
        listaTienda.forEach { chipTienda ->
            val newChip = Chip(this)
            newChip.text = chipTienda.name
            newChip.isCheckable = true
            tiendaCrearList.add(chipTienda.name)

            newChip.setOnCheckedChangeListener{_,isChecked->
                if(isChecked){
                    tiendaListSeleceted.add(chipTienda.name)
                }else{
                    tiendaListSeleceted.remove(chipTienda.name)
                }
            }

            filtrarPorTiendas()

            binding.chipGroupTiendas.addView(newChip)

        }

    }

    //mensaje de error
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun eliminarProductosSeleccionados(){

        val productosSeleccionados = adapterProduct.getProductosSeleccionados()

        if(productosSeleccionados.isNotEmpty()){
            productosSeleccionados.forEach {
                producto ->
                dataBase.productDao().deleteProduct(producto)
                Toast.makeText(this, "Productos eliminados", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "No hay productos seleccionados", Toast.LENGTH_SHORT).show()
        }

        adapterProduct.actualizarLista(productosSeleccionados.toMutableList())

    }

    private fun marcarComoComprado() {

        val productosSeleccionados  = adapterProduct.getProductosSeleccionados()

        if (productosSeleccionados.isNotEmpty()) {


            productosSeleccionados.forEach { product ->
                product.comprado = !product.comprado
                dataBase.productDao().updateProduct(product)

            }
            adapterProduct.actualizarLista(dataBase.productDao().getProduct())

            Toast.makeText(this, "Productos marcados como comprados", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "No hay productos seleccionados", Toast.LENGTH_SHORT).show()
        }


        adapterProduct.notifyDataSetChanged()
    }

    private fun filtrarPorCategoria(){

        val productosFiltrados = if (categoriaSeleccionada != null) {
            dataBase.productDao().getProductsByCategoria(categoriaSeleccionada!!)
        } else {
            dataBase.productDao().getProduct()
        }
        adapterProduct.actualizarLista(productosFiltrados.toMutableList())

    }

    private fun filtrarPorTiendas(){
        val productosFiltrados = if (tiendaListSeleceted.isNotEmpty()) {
            dataBase.productDao().getProductsByMultiplesTiendas(tiendaListSeleceted)
        } else {
            dataBase.productDao().getProduct()
        }
        adapterProduct.actualizarLista(productosFiltrados.toMutableList())
    }

}