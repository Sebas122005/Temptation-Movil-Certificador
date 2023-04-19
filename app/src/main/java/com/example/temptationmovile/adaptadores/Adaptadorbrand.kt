package com.example.temptationmovile.adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.viewHolder.BrandViewHolder

class Adaptadorbrand(private val lista: List<Brand>?,val onClickListener: (Brand,Int)->Unit ,val onClickDeleteChangued:(Int,Brand)->Unit):
    RecyclerView.Adapter<BrandViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BrandViewHolder(layoutInflater.inflate(R.layout.elemento_lista_brand,parent,false))
    }

    override fun getItemCount(): Int {
        return lista!!.size
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val item = lista!![position]
        holder.render(item,onClickListener,onClickDeleteChangued)
    }

}