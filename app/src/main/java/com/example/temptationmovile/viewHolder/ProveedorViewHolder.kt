package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Provider
import com.example.temptationmovile.databinding.ElementoListaBrandBinding
import com.example.temptationmovile.databinding.ElementoListaProviderBinding

class ProveedorViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaProviderBinding.bind(view)

    fun render(
        provider: Provider,
        onClickListener: (Provider, Int) -> Unit,
        onClickDeleteChangued: (Int, Provider) -> Unit
    ){
        binding.lstidprov.text=provider.idprovider.toString()
        binding.lstnameProv.text=provider.name_prov.toString()
        if (provider.state==1){
            binding.lststatePro.text="Habilitado"
            binding.idbtnEstadoProveedor.text="Deshabilitar"
        }else{
            binding.lststatePro.text="Deshabilitado"
            binding.idbtnEstadoProveedor.text="Habilitar"
        }
        binding.idbtnEstadoProveedor.setOnClickListener { onClickDeleteChangued(adapterPosition,provider) }
        itemView.setOnClickListener { onClickListener(provider,adapterPosition) }
    }
}