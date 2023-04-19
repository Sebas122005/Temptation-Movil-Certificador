package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Size
import com.example.temptationmovile.databinding.ElementoListaBrandBinding
import com.example.temptationmovile.databinding.ElementoListaSizeBinding

class SizeViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaSizeBinding.bind(view)

    fun render(
        tl: Size,
        onClickListener: (Size, Int) -> Unit,
        onClickDeleteChangued: (Int, Size) -> Unit
    ){
        binding.lstcodsiz.text=tl.idsize.toString()
        binding.lstnamesiz.text=tl.name_size.toString()
        if (tl.state==1){
            binding.lststateSiz.text="Habilitado"
            binding.idbtnEstadoTalla.text="Deshabilitar"
        }else{
            binding.lststateSiz.text="Deshabilitado"
            binding.idbtnEstadoTalla.text="Habilitar"
        }
        binding.idbtnEstadoTalla.setOnClickListener { onClickDeleteChangued(adapterPosition,tl) }
        itemView.setOnClickListener { onClickListener(tl,adapterPosition) }
    }
}