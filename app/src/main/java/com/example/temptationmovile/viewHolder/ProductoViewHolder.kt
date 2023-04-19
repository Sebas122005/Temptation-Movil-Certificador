package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Product
import com.example.temptationmovile.databinding.ElementoListaBrandBinding
import com.example.temptationmovile.databinding.ElementoListaProductoBinding

class ProductoViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaProductoBinding.bind(view)

    fun render(
        pro: Product,
        onClickListener: (Product, Int) -> Unit,
        onClickDeleteChangued: (Int, Product) -> Unit
    ){
        binding.lblidproducE.text=pro.idproduc.toString()
        binding.lblnamePE.text=pro.name_p.toString()
        binding.lblpriceE.text=pro.price.toString()
        binding.lblstockE.text=pro.stock.toString()
        if (pro.state==1){
            binding.lblstateE.text="Habilitado"
            binding.idbtnEstadoProducto.text="Deshabilitar"
        }else{
            binding.lblstateE.text="Deshabilitado"
            binding.idbtnEstadoProducto.text="Habilitar"
        }
        binding.idbtnEstadoProducto.setOnClickListener { onClickDeleteChangued(adapterPosition,pro) }
        itemView.setOnClickListener { onClickListener(pro,adapterPosition) }
    }
}