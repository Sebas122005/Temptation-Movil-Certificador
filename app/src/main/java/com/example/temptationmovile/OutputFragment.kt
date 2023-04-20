package com.example.temptationmovile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.temptationmovile.adaptadores.AdaptadorComboBrand
import com.example.temptationmovile.adaptadores.AdaptadorComboProducto
import com.example.temptationmovile.adaptadores.AdaptadorOutput
import com.example.temptationmovile.adaptadores.AdaptadorProduct
import com.example.temptationmovile.clases.DetailIncome
import com.example.temptationmovile.clases.Output
import com.example.temptationmovile.clases.Product
import com.example.temptationmovile.databinding.OutputFragmentBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.OutputService
import com.example.temptationmovile.servicios.ProductService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OutputFragment : Fragment() {

    private var _binding:OutputFragmentBinding?=null
    private val binding get() = _binding!!
    private lateinit var adaptadorOutput: AdaptadorOutput
    private var llmanager:LinearLayoutManager?=null
    private var productService: ProductService? = null
    private var outputService: OutputService? = null
    val objoutput = Output()

    private var registroProducto: List<Product>? = null
    private var registroOutput: MutableList<Output>? = null

    private var fecha = Calendar.getInstance().time
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
    private var fechaAct = formatoFecha.format(fecha)
    private var fechita = fechaAct
    private var fila :Int?=null

    var objutilidad =  Util()

    private var dialogo: AlertDialog.Builder? = null
    var ft: FragmentTransaction?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = OutputFragmentBinding.inflate(inflater, container, false)
        var context=binding.root.context
        registroProducto = ArrayList()
        registroOutput = ArrayList()
        llmanager = LinearLayoutManager(context)
        productService = ApiUtil.productService
        outputService = ApiUtil.outputService

        mostrarComboProduc(context)
        mostrarOutput(context)

        binding.txtfecha.setText(fechaAct)

        binding.btnRegistrarOut.setOnClickListener {
            if(binding.txtcant.text.toString() == ""){
                objutilidad.MensajeToast(context,"Ingresa la Cantidad")
                binding.txtcant.requestFocus()
            }else if (binding.txtdestino.text.toString() == ""){
                objutilidad.MensajeToast(context,"Ingresa el Destino")
                binding.txtdestino.requestFocus()
            }else if(binding.cboproducto.selectedItemPosition == -1){
                objutilidad.MensajeToast(context,"Seleccione un Producto")
            }
            else{
                var state = if (binding.chbestadoOut.isChecked) 1 else 0

                objoutput.idproduc = (registroProducto as java.util.ArrayList<Product>).get(binding.cboproducto.selectedItemPosition).idproduc
                objoutput.quantity = binding.txtcant.text.toString().toInt()
                objoutput.dateout = fechaAct
                objoutput.destino = binding.txtdestino.toString()
                objoutput.state = state

                registrarOutput(context,objoutput)
                val foutput = OutputFragment()
                DialogoCRUD("Registro de Salida","Se registro la salida correctamente",foutput)

            }
        }

        binding.btnActualizarOut.setOnClickListener {
            if(fila!=null){
                fechita = fechaAct

                objoutput.idproduc = (registroProducto as ArrayList<Product>).get(binding.cboproducto.selectedItemPosition).idproduc
                objoutput.quantity = binding.txtcant.text.toString().toInt()
                objoutput.dateout = fechaAct
                objoutput.destino = binding.txtdestino.text.toString()
                objoutput.state = if (binding.chbestadoOut.isChecked) 1 else 0

                actualizarOutput(context,objoutput,objoutput.idout.toLong())
                val foutput = OutputFragment()
                DialogoCRUD("Actualización de Salida","Se actualizó la salida correctamente",foutput)
            }else{
                objutilidad.MensajeToast(context,"Seleccione un elemento de la lista")
                binding.lstout.requestFocus()
            }
        }

        return  binding.root
    }
    fun mostrarComboProduc(context:Context){
        val call = productService!!.MostrarProduct()
        call.enqueue(object :Callback<List<Product>?>{
            override fun onResponse(
                call: Call<List<Product>?>,
                response: Response<List<Product>?>
            ) {
                if(response.isSuccessful){
                    registroProducto = response.body()
                    binding.cboproducto.adapter = AdaptadorComboProducto(context, registroProducto)
                }
            }
            override fun onFailure(call: Call<List<Product>?>, t: Throwable) {
                Log.e("Error Combo: ", t.message.toString())
            }

        })
    }
    fun onClickListener(out:Output,pos:Int){
        fila=pos
        binding.lblidout.text=out.idout.toString()
        binding.txtcant.setText(out.quantity.toString())
        binding.txtfecha.setText(out.dateout.toString())
        binding.txtdestino.setText(out.destino.toString())
        for (x in (registroProducto as ArrayList<Product>).indices){
            if((registroProducto as ArrayList<Product>).get(x).idproduc == (registroOutput as ArrayList<DetailIncome>).get(out.idproduc).idproduc){
                binding.cboproducto.setSelection(x)
            }
        }
        if (out.state==1) {
            binding.chbestadoOut.setChecked(true)
        }else {
            binding.chbestadoOut.setChecked(false)
        }
    }
    fun onClickDeleteChangued(del:Int,out: Output){
        if (out.state==1){
            eliminarOutput(out.idout.toLong())
            DialogoCRUD("Salida Deshabilitado","Se deshabilito el estado de la Salida al destino"+out.destino+" con ID "+out.idout+ "en la fecha :"+out.dateout,OutputFragment())
        }else{
            out.state=1
            actualizarOutput(binding.root.context,out,out.idout.toLong())
            DialogoCRUD("Salida Habilitado","Se habilitó el estado de la Salidaal destino"+out.destino+" con ID "+out.idout+ "en la fecha :"+out.dateout,OutputFragment())
        }
    }
    fun mostrarOutput(context:Context){
        val call = outputService!!.MostrarOutputs()
        call!!.enqueue(object: Callback<List<Output>>{
            override fun onResponse(call: Call<List<Output>>, response: Response<List<Output>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroOutput = response.body() as MutableList<Output>
                    adaptadorOutput = AdaptadorOutput(lista=registroOutput!!,
                        onClickListener={b,pos->onClickListener(b,pos)},
                        onClickDeleteChangued={del,bra->onClickDeleteChangued(del,bra)})
                }
            }
            override fun onFailure(call: Call<List<Output>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }
        })
    }

    fun registrarOutput(context:Context,o:Output){
        val call = outputService!!.RegistrarOutput(o)
        call!!.enqueue(object :Callback<Output?>{
            override fun onResponse(call: Call<Output?>, response: Response<Output?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro el producto")
                }
            }

            override fun onFailure(call: Call<Output?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }
    fun actualizarOutput(context:Context,o:Output,id:Long){
        val call = outputService!!.ActualizarOutput(id,o)
        call!!.enqueue(object :Callback<Output?>{
            override fun onResponse(call: Call<Output?>, response: Response<Output?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente la Salida")
                    fila=null
                }
            }

            override fun onFailure(call: Call<Output?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }

    fun eliminarOutput(id:Long){
        val call=outputService!!.EliminarOutput(id)
        call!!.enqueue(object :Callback<Output?>{
            override fun onResponse(call: Call<Output?>, response: Response<Output?>) {
                if (response.isSuccessful){
                    Log.i("mensaje","se elimino")
                }
            }

            override fun onFailure(call: Call<Output?>, t: Throwable) {
                Log.i("Error",t.message!!.toString())
            }

        })
    }

    fun DialogoCRUD(titulo: String, mensaje: String, fragment: Fragment) {
        dialogo = AlertDialog.Builder(context)
        dialogo!!.setTitle(titulo)
        dialogo!!.setMessage(mensaje)
        dialogo!!.setCancelable(false)
        dialogo!!.setPositiveButton("Ok") { dialogo, which ->
            val fra = fragment
            ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.contenedor, fra, null)
            ft?.addToBackStack(null)
            ft?.commit()
        }
        dialogo!!.show()
    }
    fun DialogoCRUDv2(titulo: String, mensaje: String, fragment: Fragment) {
        dialogo = AlertDialog.Builder(context)
        dialogo!!.setTitle(titulo)
        dialogo!!.setMessage(mensaje)
        dialogo!!.setCancelable(false)
        dialogo!!.setPositiveButton("Sí") { dialogo, which ->
            val fra = fragment
            ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.contenedor, fra, null)
            ft?.addToBackStack(null)
            ft?.commit()
        }
        dialogo!!.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        dialogo!!.show()
    }

}