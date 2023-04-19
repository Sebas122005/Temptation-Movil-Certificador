package com.example.temptationmovile.adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Cotizar
import com.example.temptationmovile.viewHolder.CotizarViewHolder

class CotizarAdapter(private val listaCotizacion:List<Cotizar>,private val onClickListener:(Cotizar)->Unit,private val onClickDelete: (Int) -> Unit):RecyclerView.Adapter<CotizarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CotizarViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        return CotizarViewHolder(layoutInflater.inflate(R.layout.item_cotizar,parent,false))
    }

    override fun getItemCount(): Int {
        return listaCotizacion.size
    }

    override fun onBindViewHolder(holder: CotizarViewHolder, position: Int) {
        val item = listaCotizacion[position]
        holder.render(item,onClickListener,onClickDelete)
    }

}