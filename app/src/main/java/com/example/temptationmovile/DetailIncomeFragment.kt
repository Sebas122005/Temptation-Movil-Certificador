package com.example.temptationmovile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.temptationmovile.R.*
import com.example.temptationmovile.adaptadores.*
import com.example.temptationmovile.clases.*
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.DetailIncomeService
import com.example.temptationmovile.servicios.IncomeService
import com.example.temptationmovile.servicios.ProductService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import com.example.temptationmovile.databinding.FragmentDetailIncomeBinding
import retrofit2.Callback
import retrofit2.Response

class DetailIncomeFragment : Fragment(),incomeFragment.OnFragmentInteractionListener {

    private var _binding:FragmentDetailIncomeBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapterDetailIncome: AdaptadorDetailIncome
    //services
    private var detailincomeServide: DetailIncomeService?=null
    private var productoService: ProductService?=null
    private var incomeService: IncomeService?=null
    //listas
    private var registroProducto: List<Product>? = null
    private var registroIncome: List<Income>? = null
    private var registroDetail: MutableList<DetailIncome>? = null

    private val objproducto = Product()
    private val objdetailincome = DetailIncome()
    var objutilidad =  Util()
    private var dialogo: AlertDialog.Builder? = null
    var ft: FragmentTransaction?= null
    private var fila:Int? =null
    private var llmanager:LinearLayoutManager?=null

    private var indiceProducto= 0
    private var indiceIncome= 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailIncomeBinding.inflate(inflater, container, false)
        var context=binding.root.context
        llmanager= LinearLayoutManager(context)
        registroProducto=ArrayList()
        registroIncome=ArrayList()
        registroDetail=ArrayList()

        detailincomeServide=ApiUtil.detailincomeService
        productoService=ApiUtil.productService
        incomeService=ApiUtil.incomeService

        mostrarComboProducto(context)
        mostrarComboIncome(context)
        mostrardetailincome(context)

        binding.btnregistrardetail.setOnClickListener {
            if(binding.txtcantidaddetail.text.toString() == ""){
                objutilidad.MensajeToast(context,"Ingresa la Cantidad")
                binding.txtcantidaddetail.requestFocus()
            }else if(binding.txtpreciodetail.text.toString()== ""){
                objutilidad.MensajeToast(context,"Ingresa el Precio")
                binding.txtpreciodetail.requestFocus()
            }else if(binding.cboproductosdetail.selectedItemPosition==-1){
                objutilidad.MensajeToast(context,"Seleccionne una Producto")
                binding.cboproductosdetail.requestFocus()
            }else if(binding.cboidincome.selectedItemPosition==-1){
                objutilidad.MensajeToast(context,"Seleccionne una Compra")
                binding.cboidincome.requestFocus()
            }else if(binding.txtigvdetail.text.toString()== ""){
                objutilidad.MensajeToast(context,"Ingresa el IGV")
                binding.txtigvdetail.requestFocus()
            } else{
                objdetailincome.idproduc = (registroProducto as ArrayList<Product>).get(binding.cboproductosdetail.selectedItemPosition).idproduc
                objdetailincome.idincome=(registroIncome as ArrayList<Income>).get(binding.cboidincome.selectedItemPosition).idincome
                objdetailincome.price_buy = binding.txtpreciodetail.text.toString().toDouble()
                objdetailincome.quantity = binding.txtcantidaddetail.text.toString().toInt()
                objdetailincome.igv = binding.txtigvdetail.text.toString().toDouble()
                println(objdetailincome).toString()
                registroDetailIncome(context,objdetailincome)
                val fdetailincome = DetailIncomeFragment()
                DialogoCRUD("Registro de Detalle Compra", "Se registró Correctamente",fdetailincome)
            }

        }

        binding.btnactualizardetail.setOnClickListener {
            if(fila !=null){
                objdetailincome.iddetincome=binding.lbliddetailincome.text.toString().toInt()
                objdetailincome.idproduc = (registroProducto as ArrayList<Product>).get(binding.cboproductosdetail.selectedItemPosition).idproduc
                objdetailincome.idincome=(registroIncome as ArrayList<Income>).get(binding.cboidincome.selectedItemPosition).idincome
                objdetailincome.price_buy = binding.txtpreciodetail.text.toString().toDouble()
                objdetailincome.quantity = binding.txtcantidaddetail.text.toString().toInt()
                objdetailincome.igv = binding.txtigvdetail.text.toString().toDouble()

                actualizarDetailIncome(context,objdetailincome,objdetailincome.iddetincome.toLong())
                val fdetailincome = DetailIncomeFragment()
                DialogoCRUD("Actualizacion del Detalle Compra", "Se Actualizó Correctamente",fdetailincome)
            }else{
                objutilidad.MensajeToast(context,"Seleccione un elemento de la lista")
                binding.lstdetailincome.requestFocus()
            }
        }
        return binding.root
    }
    fun onClickListener(detail:DetailIncome,pos:Int){
        fila =pos
        binding.lbliddetailincome.text=detail.iddetincome.toString()
        binding.txtcantidaddetail.setText(detail.quantity.toString())
        binding.txtpreciodetail.setText(detail.price_buy.toString())
        binding.txtigvdetail.setText(detail.igv.toString())
        for (x in (registroProducto as ArrayList<Product>).indices){
            if((registroProducto as ArrayList<Product>).get(x).idproduc == (registroDetail as ArrayList<DetailIncome>).get(detail.idproduc).idproduc){
                binding.cboproductosdetail.setSelection(x)
            }
        }
        for (x in (registroIncome as ArrayList<Income>).indices){
            if((registroIncome as ArrayList<Income>).get(x).idincome == (registroDetail as ArrayList<DetailIncome>).get(detail.idincome).idincome){
                binding.cboidincome.setSelection(x)
            }
        }
    }

    fun registroDetailIncome(context: Context, d:DetailIncome){
        val call = detailincomeServide!!.RegistrarDetailIncome(d)
        call!!.enqueue(object :Callback<DetailIncome?>{
            override fun onResponse(call: Call<DetailIncome?>, response: Response<DetailIncome?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro el Detalle de Compra")
                }
            }
            override fun onFailure(call: Call<DetailIncome?>, t: Throwable) {
                println("Error Detalle: ").toString()
            }

        })
    }

    fun actualizarDetailIncome(context: Context, p:DetailIncome, id: Long ){
        val call = detailincomeServide!!.ActualizarDetailIncome(id,p)
        call!!.enqueue(object : Callback<DetailIncome?>{
            override fun onResponse(call: Call<DetailIncome?>, response: Response<DetailIncome?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Detalle de Compra")
                }
            }

            override fun onFailure(call: Call<DetailIncome?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        }
        )
    }


    fun mostrarComboProducto(context: Context){
        val call = productoService!!.MostrarProduct()
        call.enqueue(object : Callback<List<Product>?>{
            override fun onResponse(call: Call<List<Product>?>, response: Response<List<Product>?>) {
                if(response.isSuccessful){
                    registroProducto = response.body()
                    binding.cboproductosdetail.adapter = AdaptadorComboProducto(context,registroProducto)
                }
            }

            override fun onFailure(call: Call<List<Product>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun mostrarComboIncome(context: Context){
        val call = incomeService!!.MostrarIncomes()
        call.enqueue(object : Callback<List<Income>?>{
            override fun onResponse(call: Call<List<Income>?>, response: Response<List<Income>?>) {
                if(response.isSuccessful){
                    registroIncome = response.body()
                    binding.cboidincome.adapter = AdaptadorComboIncome(context,registroIncome)
                }
            }

            override fun onFailure(call: Call<List<Income>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun mostrardetailincome(context: Context){
        val call = detailincomeServide!!.MostrarDetailIncomes()
        call!!.enqueue(object :Callback<List<DetailIncome>>{
            override fun onResponse(call: Call<List<DetailIncome>>, response: Response<List<DetailIncome>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroDetail = response.body() as MutableList<DetailIncome>
                    adapterDetailIncome = AdaptadorDetailIncome(lista=registroDetail!!,
                        onClickListener={b,pos->onClickListener(b,pos)})
                    binding.lstdetailincome.layoutManager=llmanager
                    binding.lstdetailincome.adapter=adapterDetailIncome
                }else{
                    println("Error")
                }
            }

            override fun onFailure(call: Call<List<DetailIncome>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
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

    override fun onFragmentInteraction(idIncome: Int) {
        if (idIncome!=null){
            for (x in (registroIncome as ArrayList<Income>).indices){
                if((registroIncome as ArrayList<Income>).get(x).idincome == (registroDetail as ArrayList<DetailIncome>).get(idIncome).idincome){
                    binding.cboidincome.setSelection(x)
                }
            }
        }
    }
}