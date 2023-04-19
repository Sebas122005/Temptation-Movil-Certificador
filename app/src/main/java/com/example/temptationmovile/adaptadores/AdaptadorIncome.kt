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
import com.example.temptationmovile.clases.Income
import com.example.temptationmovile.viewHolder.BrandViewHolder
import com.example.temptationmovile.viewHolder.IncomeViewHolder


class AdaptadorIncome(private val lista: List<Income>?, val onClickListener: (Income, Int)->Unit):
    RecyclerView.Adapter<IncomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IncomeViewHolder(layoutInflater.inflate(R.layout.elemento_lista_income,parent,false))
    }

    override fun getItemCount(): Int {
        return lista!!.size
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val item= lista!![position]
        holder.render(item,onClickListener)
    }

}