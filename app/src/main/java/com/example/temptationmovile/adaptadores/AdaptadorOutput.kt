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
import com.example.temptationmovile.clases.Output
import com.example.temptationmovile.clases.Product
import com.example.temptationmovile.viewHolder.BrandViewHolder
import com.example.temptationmovile.viewHolder.OutputViewHolder

class AdaptadorOutput(private val lista: List<Output>?, val onClickListener: (Output, Int)->Unit):
    RecyclerView.Adapter<OutputViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutputViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OutputViewHolder(layoutInflater.inflate(R.layout.elemento_lista_output,parent,false))
    }

    override fun getItemCount(): Int {
        return lista!!.size
    }

    override fun onBindViewHolder(holder: OutputViewHolder, position: Int) {
        val item = lista!![position]
        holder.render(item,onClickListener)
    }

}