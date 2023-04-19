package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Income
import com.example.temptationmovile.databinding.ElementoListaBrandBinding
import com.example.temptationmovile.databinding.ElementoListaIncomeBinding

class IncomeViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaIncomeBinding.bind(view)

    fun render(
        icome: Income,
        onClickListener: (Income, Int) -> Unit
    ){
        binding.lstidincome.text=icome.idincome.toString()
        binding.lstnomproviders.text=icome.idprovider.toString()
        binding.lstfechaincome.text=icome.dateinco.toString()
        itemView.setOnClickListener { onClickListener(icome,adapterPosition) }
    }
}