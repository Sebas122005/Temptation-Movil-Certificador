package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Category
import com.example.temptationmovile.databinding.ElementoListaCategoryBinding

class CategoriaViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaCategoryBinding.bind(view)

    fun render(
        Category: Category,
        onClickListener: (Category, Int) -> Unit,
        onClickDeleteChangued: (Int, Category) -> Unit
    ){
        binding.lstidcat.text=Category.idcat.toString()
        binding.lstnameCat.text=Category.name_cat.toString()
        if (Category.state==1){
            binding.lststateCat.text="Habilitado"
            binding.idbtnEstadoCategoria.text="Deshabilitar"
        }else{
            binding.lststateCat.text="Deshabilitado"
            binding.idbtnEstadoCategoria.text="Habilitar"
        }
        binding.idbtnEstadoCategoria.setOnClickListener { onClickDeleteChangued(adapterPosition,Category) }
        itemView.setOnClickListener { onClickListener(Category,adapterPosition) }
    }
}