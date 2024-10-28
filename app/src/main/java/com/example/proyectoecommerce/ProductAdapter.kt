package com.example.proyectoecommerce

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.proyectoecommerce.databinding.ItemProductBinding


class ProductAdapter(private var productList: MutableList<Product>
    ) : Adapter<ProductViewHolder>() {
    private val productosSeleccionados: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.count()
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val unProduct = productList[position]
        holder.completarInfoProducto(unProduct,{ product:Product, check : Boolean  ->
            if (check){
                productosSeleccionados.add(product)
                Log.d("Producto Agregado", "Producto agregado: ${product.nombre + product.idProduct}")
                println(productosSeleccionados.toString())

            }else{
                productosSeleccionados.remove(product)
                Log.d("Producto Agregado", "Producto agregado: ${product.nombre + product.idProduct}")
                println(productosSeleccionados.toString())
            }

        })

        if (unProduct.comprado) {
            holder.itemView.setBackgroundColor(Color.GREEN)

        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)

        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleActivity::class.java)
            intent.putExtra("idProduct", unProduct.idProduct)
            intent.putExtra("nombre", unProduct.nombre)
            intent.putExtra("cantidad", unProduct.cantidad)
            intent.putExtra("categoria", unProduct.categoria)
            intent.putExtra("tiendas", unProduct.tiendas)
            intent.putExtra("prioridad", unProduct.prioridad)
            intent.putExtra("notasAdicionales", unProduct.notasAdicionales)
            intent.putExtra("comprado", unProduct.comprado)

            context.startActivity(intent)
        }
    }

    fun getProductosSeleccionados() : List<Product> {
        return productosSeleccionados
    }

    fun actualizarLista(newProductList: List<Product>) {
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()
    }
}

class ProductViewHolder(private val binding:ItemProductBinding ) : ViewHolder(binding.root) {



    fun completarInfoProducto(product: Product , onClickCheck : (Product , Boolean) -> Unit ) {
        binding.nombre.text = product.nombre
        binding.decripcion.text = product.tiendas
        binding.categoriaView.text = product.categoria

        if (product.prioridad == true) {
            binding.prioridad.text = "alta"
        } else {
            binding.prioridad.text = ""
        }

        if(product.comprado == true){

            binding.checkbox.setBackgroundColor(Color.GREEN)
        } else {
            binding.checkbox.setBackgroundColor(Color.TRANSPARENT)
        }

        binding.checkbox.setOnCheckedChangeListener { compoundButton, checked ->
            onClickCheck(product , checked)
        }

    }
}