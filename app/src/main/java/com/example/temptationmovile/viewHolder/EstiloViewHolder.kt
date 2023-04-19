package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Style
import com.example.temptationmovile.databinding.ElementoListaBrandBinding
import com.example.temptationmovile.databinding.ElementoListaStyleBinding

class EstiloViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaStyleBinding.bind(view)

    fun render(
        style: Style,
        onClickListener: (Style, Int) -> Unit,
        onClickDeleteChangued: (Int, Style) -> Unit
    ){
        binding.lstidstyle.text=style.idstyles.toString()
        binding.lstnameStyle.text=style.name_sty.toString()
        if (style.state==1){
            binding.lststateStyle.text="Habilitado"
            binding.idbtnEstadoEstilo.text="Deshabilitar"
        }else{
            binding.lststateStyle.text="Deshabilitado"
            binding.idbtnEstadoEstilo.text="Habilitar"
        }
        binding.idbtnEstadoEstilo.setOnClickListener { onClickDeleteChangued(adapterPosition,style) }
        itemView.setOnClickListener { onClickListener(style,adapterPosition) }
    }
}