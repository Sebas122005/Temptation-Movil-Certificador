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
import com.example.temptationmovile.clases.Style
import com.example.temptationmovile.viewHolder.BrandViewHolder
import com.example.temptationmovile.viewHolder.EstiloViewHolder

class AdaptadorStyle(private val lista: List<Style>?, val onClickListener: (Style, Int)->Unit, val onClickDeleteChangued:(Int, Style)->Unit):
    RecyclerView.Adapter<EstiloViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstiloViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EstiloViewHolder(layoutInflater.inflate(R.layout.elemento_lista_style,parent,false))
    }

    override fun getItemCount(): Int {
        return lista!!.size
    }

    override fun onBindViewHolder(holder: EstiloViewHolder, position: Int) {
        val item = lista!![position]
        holder.render(item,onClickListener,onClickDeleteChangued)
    }

}