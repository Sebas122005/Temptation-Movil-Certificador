package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.databinding.ElementoListaBrandBinding
import com.example.temptationmovile.databinding.ElementoListaRolBinding

class RolViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaRolBinding.bind(view)

    fun render(
        rol: Rol,
        onClickListener: (Rol, Int) -> Unit,
        onClickDeleteChangued: (Int, Rol) -> Unit
    ){
        binding.lstcodrol.text=rol.idrol.toString()
        binding.lstnamerol.text=rol.namerol.toString()
        if (rol.state==1){
            binding.lststateRol.text="Habilitado"
            binding.idbtnEstadoRol.text="Deshabilitar"
        }else{
            binding.lststateRol.text="Deshabilitado"
            binding.idbtnEstadoRol.text="Habilitar"
        }
        binding.idbtnEstadoRol.setOnClickListener { onClickDeleteChangued(adapterPosition,rol) }
        itemView.setOnClickListener { onClickListener(rol,adapterPosition) }
    }
}