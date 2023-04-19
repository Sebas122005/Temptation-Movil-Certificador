package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Product
import com.example.temptationmovile.viewHolder.BrandViewHolder
import com.example.temptationmovile.viewHolder.ProductoViewHolder

class AdaptadorProduct(private val lista: List<Product>?, val onClickListener: (Product, Int)->Unit, val onClickDeleteChangued:(Int, Product)->Unit):
    RecyclerView.Adapter<ProductoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductoViewHolder(layoutInflater.inflate(R.layout.elemento_lista_producto,parent,false))
    }

    override fun getItemCount(): Int {
        return lista!!.size
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val item = lista!![position]
        holder.render(item,onClickListener,onClickDeleteChangued)
    }


}