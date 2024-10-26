package com.example.proyectoecommerce

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.proyectoecommerce.databinding.ItemProductBinding

class ProductAdapter(private val productList: List<Product>) : Adapter<ProductViewHolder>() {
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
        holder.completarInfoProducto(unProduct)
    }
}

class ProductViewHolder(private val binding:ItemProductBinding) : ViewHolder(binding.root) {
    fun completarInfoProducto(product: Product){
        binding.nombre.text = product.nombre
        binding.decripcion.text = product.tiendas

        if(product.prioridad == true){
            binding.prioridad.text = "alta"
        }else{
            binding.prioridad.text = ""
        }
    }
}