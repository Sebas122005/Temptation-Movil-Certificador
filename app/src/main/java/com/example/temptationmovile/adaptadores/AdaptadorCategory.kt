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
import com.example.temptationmovile.clases.Category
import com.example.temptationmovile.viewHolder.BrandViewHolder
import com.example.temptationmovile.viewHolder.CategoriaViewHolder

class AdaptadorCategory(private val lista: List<Category>?,val onClickListener: (Category,Int)->Unit ,val onClickDeleteChangued:(Int,Category)->Unit):
    RecyclerView.Adapter<CategoriaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CategoriaViewHolder(layoutInflater.inflate(R.layout.elemento_lista_category,parent,false))
    }
    override fun getItemCount(): Int {
        return lista!!.size
    }
    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val item = lista!![position]
        holder.render(item,onClickListener,onClickDeleteChangued)
    }


}