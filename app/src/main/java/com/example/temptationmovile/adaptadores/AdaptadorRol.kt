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
import com.example.temptationmovile.viewHolder.BrandViewHolder
import com.example.temptationmovile.viewHolder.RolViewHolder

class AdaptadorRol(private val lista: List<Rol>?,val onClickListener: (Rol,Int)->Unit ,val onClickDeleteChangued:(Int,Rol)->Unit):
    RecyclerView.Adapter<RolViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RolViewHolder(layoutInflater.inflate(R.layout.elemento_lista_rol,parent,false))
    }

    override fun getItemCount(): Int {
        return lista!!.size
    }

    override fun onBindViewHolder(holder: RolViewHolder, position: Int) {
        val item = lista!![position]
        holder.render(item,onClickListener,onClickDeleteChangued)
    }

}