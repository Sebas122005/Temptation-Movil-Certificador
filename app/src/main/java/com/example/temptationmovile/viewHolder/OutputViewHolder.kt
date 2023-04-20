package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Output
import com.example.temptationmovile.databinding.ElementoListaBrandBinding
import com.example.temptationmovile.databinding.ElementoListaOutputBinding

class OutputViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaOutputBinding.bind(view)

    fun render(
        output: Output,
        onClickListener: (Output, Int) -> Unit,
        onClickDeleteChangued: (Int, Output) -> Unit
    ){
        binding.lblidprod.text=output.idout.toString()
        binding.lblidprod.text=output.idproduc.toString()
        binding.lblcant.text=output.quantity.toString()
        binding.lbldestino.text=output.destino.toString()
        if (output.state==1){
            binding.lststateSalida.text="Habilitado"
            binding.idbtnEstadoSalida.text="Deshabilitar"
        }else{
            binding.lststateSalida.text="Deshabilitado"
            binding.idbtnEstadoSalida.text="Habilitar"
        }
        binding.idbtnEstadoSalida.setOnClickListener { onClickDeleteChangued(adapterPosition,output) }
        itemView.setOnClickListener { onClickListener(output,adapterPosition) }
    }
}