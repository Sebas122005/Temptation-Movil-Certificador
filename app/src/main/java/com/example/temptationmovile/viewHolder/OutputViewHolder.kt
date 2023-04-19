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
        onClickListener: (Output, Int) -> Unit
    ){
        binding.lblidprod.text=output.idout.toString()
        binding.lblidprod.text=output.idproduc.toString()
        binding.lblcant.text=output.quantity.toString()
        binding.lbldestino.text=output.destino.toString()

        itemView.setOnClickListener { onClickListener(output,adapterPosition) }
    }
}