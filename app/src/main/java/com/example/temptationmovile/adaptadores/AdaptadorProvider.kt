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
import com.example.temptationmovile.clases.Provider
import com.example.temptationmovile.viewHolder.BrandViewHolder
import com.example.temptationmovile.viewHolder.ProveedorViewHolder

class AdaptadorProvider (private val lista: List<Provider>?,val onClickListener: (Provider,Int)->Unit ,val onClickDeleteChangued:(Int,Provider)->Unit):
RecyclerView.Adapter<ProveedorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProveedorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProveedorViewHolder(layoutInflater.inflate(R.layout.elemento_lista_provider,parent,false))
    }

    override fun getItemCount(): Int {
        return lista!!.size
    }

    override fun onBindViewHolder(holder: ProveedorViewHolder, position: Int) {
        val item = lista!![position]
        holder.render(item,onClickListener,onClickDeleteChangued)
    }


}