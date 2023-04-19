package com.example.temptationmovile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.adaptadores.CotizarAdapter
import com.example.temptationmovile.clases.Cotizar
import com.example.temptationmovile.clases.CotizarProvider
import com.example.temptationmovile.databinding.GenerarCotizacionFragmentBinding

class GenerarCotizacionFragment : Fragment() {

    private var _binding:GenerarCotizacionFragmentBinding?=null
    private val binding get() = _binding!!
    private var mutableList:MutableList<Cotizar>?=CotizarProvider.listaCotizar.toMutableList()
    private lateinit var adapterCotizar:CotizarAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GenerarCotizacionFragmentBinding.inflate(inflater, container, false)
        initRecyclerView(binding.root)
        binding.btnAgregarProCot.setOnClickListener {
            if (binding.edtproductoCotizar.text.toString().length<1 && binding.edtprecioCotizar.text.toString().length<1){
                Toast.makeText(requireContext(),"Debe ingresar datos en los campos",Toast.LENGTH_SHORT).show()
            }else{
                var cot = Cotizar(binding.edtproductoCotizar.text.toString(),binding.edtprecioCotizar.text.toString().toDouble())
                mutableList!!.add(cot)
                adapterCotizar.notifyItemInserted(mutableList!!.size-1);
                //llmanager.scrollToPositionWithOffset(1,5)
            }
        }
        return binding.root
    }

    private fun initRecyclerView(view:View){
        /*val decoration = DividerItemDecoration(view.context,manager.orientation)*/
        adapterCotizar=CotizarAdapter(
            listaCotizacion=mutableList!!,
            onClickListener={cot->onItemSelected(cot)},
            onClickDelete={pos->onClickDeleteItem(pos)}
            )
        binding.lstCotizar.layoutManager=LinearLayoutManager(view.context)
        binding.lstCotizar.adapter= adapterCotizar

        //binding.lstCotizar.addItemDecoration(decoration);
    }

    private fun onClickDeleteItem(pos:Int){
        mutableList!!.removeAt(pos)
        adapterCotizar.notifyItemRemoved(pos)
    }



    private fun onItemSelected(cotizar:Cotizar){

    }
}