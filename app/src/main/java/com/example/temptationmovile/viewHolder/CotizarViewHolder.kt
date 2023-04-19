package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Cotizar
import com.example.temptationmovile.databinding.ItemCotizarBinding

class CotizarViewHolder(view:View):RecyclerView.ViewHolder(view) {

    val binding=ItemCotizarBinding.bind(view)

    fun render(
        cotizar: Cotizar,
        onClickListener: (Cotizar) -> Unit,
        onClickDelete: (Int) -> Unit
    ){
        binding.idProductoCot.text=cotizar.name_p
        binding.idPrecioCot.text=cotizar.price.toString()
        itemView.setOnClickListener { onClickListener(cotizar) }
        binding.idQuitarProCot.setOnClickListener { onClickDelete(adapterPosition) }
    }
}