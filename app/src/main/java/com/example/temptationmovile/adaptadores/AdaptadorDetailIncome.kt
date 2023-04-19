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
import com.example.temptationmovile.clases.DetailIncome
import com.example.temptationmovile.clases.Income
import com.example.temptationmovile.viewHolder.BrandViewHolder
import com.example.temptationmovile.viewHolder.DetailIncomeViewHolder

class AdaptadorDetailIncome (private val lista: List<DetailIncome>?, val onClickListener: (DetailIncome, Int)->Unit):
    RecyclerView.Adapter<DetailIncomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailIncomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DetailIncomeViewHolder(layoutInflater.inflate(R.layout.elemento_lista_detailincome,parent,false))
    }

    override fun onBindViewHolder(holder: DetailIncomeViewHolder, position: Int) {
        val item = lista!![position]
        holder.render(item,onClickListener)
    }

    override fun getItemCount(): Int {
        return lista!!.size
    }

}