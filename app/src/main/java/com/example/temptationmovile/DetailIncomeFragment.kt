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
import com.example.temptationmovile.R.*
import com.example.temptationmovile.adaptadores.*
import com.example.temptationmovile.clases.*
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.DetailIncomeService
import com.example.temptationmovile.servicios.IncomeService
import com.example.temptationmovile.servicios.ProductService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DetailIncomeFragment : Fragment() {

    private lateinit var cboProducDetaill: Spinner
    private lateinit var cboIdIncome: Spinner
    private lateinit var lbldetidincome: TextView
    private lateinit var txtPrecioDetail: EditText
    private lateinit var txtCantidadDetail: EditText
    private lateinit var txtIgvDetail: TextView
    private lateinit var btnRegistrarDetail: Button
    private lateinit var btnActualizarDetail: Button
    private lateinit var btnSalirDetail: Button
    private lateinit var lstdetailincome: ListView

    private val objproducto = Product()
    private val objdetailincome = DetailIncome()

    private var coddetail= 0
    private var idincomedetail= 0
    private var codincomedetail= 0
    private var idproductodetail= 0
    private var codproductodetail= 0
    private var preciodetail= 0.0
    private var cantidaddetail= 0
    private var igvdetail= 0.18
    private var fila = -1
    private var pos = -1
    val raiz = null

    private var indiceProducto= 0
    private var indiceIncome= 0

    private var detailincomeServide: DetailIncomeService?=null
    private var productoService: ProductService?=null
    private var incomeService: IncomeService?=null

    private var registroProducto: List<Product>? = null
    private var registroIncome: List<Income>? = null
    private var registroDetail: List<DetailIncome>? = null

    var objutilidad =  Util()
    private var dialogo: AlertDialog.Builder? = null
    var ft: FragmentTransaction?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    //@SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val raiz = inflater.inflate(layout.fragment_detail_income, container, false)
        lbldetidincome = raiz.findViewById(R.id.lbliddetailincome)
        cboIdIncome = raiz.findViewById(R.id.cboidincome)
        cboProducDetaill = raiz.findViewById(R.id.cboproductosdetail)
        txtPrecioDetail = raiz.findViewById(R.id.txtpreciodetail)
        txtCantidadDetail =raiz.findViewById(R.id.txtcantidaddetail)
        txtIgvDetail = raiz.findViewById(R.id.txtigvdetail)
        btnRegistrarDetail = raiz.findViewById(R.id.btnregistrardetail)
        btnActualizarDetail = raiz.findViewById(R.id.btnactualizardetail)
        btnSalirDetail = raiz.findViewById(R.id.btnsalirdetail)
        lstdetailincome = raiz.findViewById(R.id.lstdetailincome)
        lstdetailincome.isNestedScrollingEnabled = true


        registroProducto=ArrayList()
        registroIncome=ArrayList()
        registroDetail=ArrayList()

        detailincomeServide=ApiUtil.detailincomeService
        productoService=ApiUtil.productService
        incomeService=ApiUtil.incomeService

        mostrarComboProducto(raiz.context)
        mostrarComboIncome(raiz.context)
        mostrardetailincome(raiz.context)

        btnRegistrarDetail.setOnClickListener {
            if(txtCantidadDetail.text.toString() == ""){
                objutilidad.MensajeToast(raiz.context,"Ingresa la Cantidad")
                txtCantidadDetail.requestFocus()
            }else if(txtPrecioDetail.text.toString()== ""){
                objutilidad.MensajeToast(raiz.context,"Ingresa el Precio")
                txtPrecioDetail.requestFocus()
            }else if(cboProducDetaill.selectedItemPosition==-1){
                objutilidad.MensajeToast(raiz.context,"Seleccionne una Producto")
                cboProducDetaill.requestFocus()
            }else if(cboIdIncome.selectedItemPosition==-1){
                objutilidad.MensajeToast(raiz.context,"Seleccionne una Compra")
                cboIdIncome.requestFocus()
            }else if(txtIgvDetail.text.toString()== ""){
                objutilidad.MensajeToast(raiz.context,"Ingresa el IGV")
                txtIgvDetail.requestFocus()
            } else{
                preciodetail = txtPrecioDetail.text.toString().toDouble()
                cantidaddetail = txtCantidadDetail.text.toString().toInt()
                //igvdetail = txtIgvDetail.text.toString().toDouble()
                idincomedetail = cboIdIncome.selectedItemPosition
                codincomedetail = (registroIncome as ArrayList<Income>).get(idincomedetail).idincome
                idproductodetail = cboProducDetaill.selectedItemPosition
                codproductodetail = (registroProducto as ArrayList<Product>).get(idproductodetail).idproduc

                objdetailincome.idincome=codincomedetail
                objdetailincome.idproduc= codproductodetail
                objdetailincome.price_buy = preciodetail
                objdetailincome.quantity = cantidaddetail
                objdetailincome.igv = igvdetail

                println(objdetailincome).toString()

                registroDetailIncome(raiz.context,objdetailincome)
                val fdetailincome = DetailIncomeFragment()
                DialogoCRUD("Registro de Producto", "Se registro la Compra Correctamente",fdetailincome)

            }

        }

        btnActualizarDetail.setOnClickListener {
            if(fila >=0){
                coddetail = lbldetidincome.text.toString().toInt()
                preciodetail = txtPrecioDetail.text.toString().toDouble()
                cantidaddetail = txtCantidadDetail.text.toString().toInt()
                //igvdetail = txtPrecioDetail.text.toString().toDouble()
                idproductodetail = cboProducDetaill.selectedItemPosition
                codproductodetail = (registroProducto as ArrayList<Product>).get(idproductodetail).idproduc
                idincomedetail = cboProducDetaill.selectedItemPosition
                codincomedetail = (registroIncome as ArrayList<Income>).get(idincomedetail).idincome

                objdetailincome.idincome=codincomedetail
                objdetailincome.idproduc= codproductodetail
                objdetailincome.price_buy = preciodetail
                objdetailincome.quantity = cantidaddetail
                objdetailincome.igv = igvdetail

                actualizarDetailIncome(raiz.context,objdetailincome,coddetail.toLong())
                val fdetailincome = DetailIncomeFragment()
                DialogoCRUD("Actualizacion de Producto", "Se Actualizo el Producto Correctamente",fdetailincome)
            }else{
                objutilidad.MensajeToast(raiz.context,"Seleccione un elemento de la lista")
                lstdetailincome.requestFocus()
            }
        }

        lstdetailincome.setOnItemClickListener { adapterView, view, i, l ->
            fila = i
            lbldetidincome.text = (registroDetail as ArrayList<DetailIncome>).get(fila).iddetincome.toString()
            txtCantidadDetail.setText(""+ (registroDetail as ArrayList<DetailIncome>).get(fila).quantity.toString().toInt())
            txtPrecioDetail.setText(""+ (registroDetail as ArrayList<DetailIncome>).get(fila).price_buy.toString().toDouble())
            txtIgvDetail.setText(""+ (registroDetail as ArrayList<DetailIncome>).get(fila).igv.toString().toDouble())

            for (x in (registroProducto as ArrayList<Product>).indices){
                if((registroProducto as ArrayList<Product>).get(x).idproduc == (registroDetail as ArrayList<DetailIncome>).get(fila).idproduc){
                    indiceProducto = x
                }
            }
            for (x in (registroIncome as ArrayList<Income>).indices){
                if((registroIncome as ArrayList<Income>).get(x).idincome == (registroDetail as ArrayList<DetailIncome>).get(fila).idincome){
                    indiceIncome = x
                }
            }
            cboProducDetaill.setSelection(indiceProducto)
            cboIdIncome.setSelection(indiceIncome)

        }

        return raiz
        //return inflater.inflate(R.layout.fragment_detail_income, container, false)
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
        call!!.enqueue(object : Callback<List<DetailIncome>?>{
            override fun onResponse(call: Call<List<DetailIncome>?>, response: Response<List<DetailIncome>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Detalle de Compra")
                }
            }

            override fun onFailure(call: Call<List<DetailIncome>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }


    fun mostrarComboProducto(context: Context){
        val call = productoService!!.MostrarProduct()
        call.enqueue(object : Callback<List<Product>?>{
            override fun onResponse(call: Call<List<Product>?>, response: Response<List<Product>?>) {
                if(response.isSuccessful){
                    registroProducto = response.body()
                    cboProducDetaill.adapter = AdaptadorComboProducto(context,registroProducto)
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
                    cboIdIncome.adapter = AdaptadorComboIncome(context,registroIncome)
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
                    registroDetail = response.body()
                    lstdetailincome.adapter = AdaptadorDetailIncome(context, registroDetail!!)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailIncomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailIncomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}