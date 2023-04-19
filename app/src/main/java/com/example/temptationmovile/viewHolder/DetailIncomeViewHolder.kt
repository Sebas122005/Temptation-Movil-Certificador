package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.DetailIncome
import com.example.temptationmovile.databinding.ElementoListaBrandBinding
import com.example.temptationmovile.databinding.ElementoListaDetailincomeBinding

class DetailIncomeViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaDetailincomeBinding.bind(view)

    fun render(
        detailIm: DetailIncome,
        onClickListener: (DetailIncome, Int) -> Unit
    ){
        binding.lstiddetailincomeE.text=detailIm.iddetincome.toString()
        binding.lstidincomeE.text=detailIm.idincome.toString()
        binding.lstpriceBuyE.text=detailIm.price_buy.toString()
        binding.lstquantityE.text=detailIm.quantity.toString()
        binding.lstidproductE.text=detailIm.idproduc.toString()
        itemView.setOnClickListener { onClickListener(detailIm,adapterPosition) }
    }
}