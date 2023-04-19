package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.databinding.ElementoListaBrandBinding

class BrandViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaBrandBinding.bind(view)

    fun render(
        brand: Brand,
        onClickListener: (Brand,Int) -> Unit,
        onClickDeleteChangued: (Int, Brand) -> Unit
    ){
        binding.lstidband.text=brand.idbrand.toString()
        binding.lstnameBrand.text=brand.name_brand.toString()
        if (brand.state==1){
            binding.lststate.text="Habilitado"
            binding.idbtnEstadoMarca.text="Deshabilitar"
        }else{
            binding.lststate.text="Deshabilitado"
            binding.idbtnEstadoMarca.text="Habilitar"
        }
        binding.idbtnEstadoMarca.setOnClickListener { onClickDeleteChangued(adapterPosition,brand) }
        itemView.setOnClickListener { onClickListener(brand,adapterPosition) }
    }
}