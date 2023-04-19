package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Color
import com.example.temptationmovile.databinding.ElementoListaBrandBinding
import com.example.temptationmovile.databinding.ElementoListaColorBinding

class ColorViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaColorBinding.bind(view)

    fun render(
        color: Color,
        onClickListener: (Color, Int) -> Unit,
        onClickDeleteChangued: (Int, Color) -> Unit
    ){
        binding.lstidcolor.text=color.idcolor.toString()
        binding.lstnameColor.text=color.name_col.toString()
        if (color.state==1){
            binding.lststatecolor.text="Habilitado"
            binding.idbtnEstadoColor.text="Deshabilitar"
        }else{
            binding.lststatecolor.text="Deshabilitado"
            binding.idbtnEstadoColor.text="Habilitar"
        }
        binding.idbtnEstadoColor.setOnClickListener { onClickDeleteChangued(adapterPosition,color) }
        itemView.setOnClickListener { onClickListener(color,adapterPosition) }
    }
}