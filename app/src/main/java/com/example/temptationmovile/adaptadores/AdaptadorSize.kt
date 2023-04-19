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
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.clases.Size
import com.example.temptationmovile.viewHolder.BrandViewHolder
import com.example.temptationmovile.viewHolder.SizeViewHolder

class AdaptadorSize(private val lista: List<Size>?, val onClickListener: (Size, Int)->Unit, val onClickDeleteChangued:(Int, Size)->Unit):
    RecyclerView.Adapter<SizeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SizeViewHolder(layoutInflater.inflate(R.layout.elemento_lista_size,parent,false))
    }

    override fun getItemCount(): Int {
        return lista!!.size
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val item = lista!![position]
        holder.render(item, onClickListener, onClickDeleteChangued)
    }

}