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
import com.example.temptationmovile.adaptadores.AdaptadorComboBrand
import com.example.temptationmovile.adaptadores.AdaptadorComboProducto
import com.example.temptationmovile.adaptadores.AdaptadorOutput
import com.example.temptationmovile.adaptadores.AdaptadorProduct
import com.example.temptationmovile.clases.Output
import com.example.temptationmovile.clases.Product
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

    private lateinit var cboproducto: Spinner
    private lateinit var txtcant: EditText
    private lateinit var txtfecha: TextView
    private lateinit var txtdestino: EditText
    private lateinit var chbestado_out: CheckBox
    private lateinit var lblidout: TextView
    private lateinit var btnRegistrar_out: Button
    private lateinit var btnActualizar_out: Button
    private lateinit var lstout: ListView

    private val objoutput = Output()
    private var cod = 0
    private var nom = ""



    private var fecha = Calendar.getInstance().time
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
    private var fechaAct = formatoFecha.format(fecha)
    private var fechita = fechaAct

    private var fa = "20/07/2023"

    private var destino = ""
    private var cantidad = 0
    private var idproduc = 0
    private var codproduc = 0
    private var state = 1
    private var fila = -1
    private var pos = -1
    val raiz = null

    private var indiceProduct = 0

    private var productService: ProductService? = null
    private var outputService: OutputService? = null

    private var registroProducto: List<Product>? = null
    private var registroOutput: List<Output>? = null

    var objutilidad =  Util()

    private var dialogo: AlertDialog.Builder? = null

    var ft: FragmentTransaction?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz = inflater.inflate(R.layout.output_fragment, container, false)

        txtcant = raiz.findViewById(R.id.txtcant)
        txtfecha = raiz.findViewById(R.id.txtfecha)

        txtdestino = raiz.findViewById(R.id.txtdestino)
        chbestado_out = raiz.findViewById(R.id.chbestado_out)
        lblidout = raiz.findViewById(R.id.lblidout)
        lstout = raiz.findViewById(R.id.lstout)
        btnRegistrar_out = raiz.findViewById(R.id.btnRegistrar_out)
        btnActualizar_out = raiz.findViewById(R.id.btnActualizar_out)
        cboproducto = raiz.findViewById(R.id.cboproducto)
        lstout.isNestedScrollingEnabled = true

        registroProducto = ArrayList()
        registroOutput = ArrayList()

        productService = ApiUtil.productService
        outputService = ApiUtil.outputService

        mostrarComboProduc(raiz.context)
        mostrarOutput(raiz.context)
        //fechaAct = txtfecha.text.toString()
        txtfecha.setText(fechaAct)


        btnRegistrar_out.setOnClickListener {
            if(txtcant.text.toString() == ""){
                objutilidad.MensajeToast(raiz.context,"Ingresa la Cantidad")
                txtcant.requestFocus()
            }else if (txtdestino.text.toString() == ""){
                objutilidad.MensajeToast(raiz.context,"Ingresa el Destino")
                txtdestino.requestFocus()
            }else if(cboproducto.selectedItemPosition == -1){
                objutilidad.MensajeToast(raiz.context,"Seleccione un Producto")
            }
            else{
                cantidad = txtcant.text.toString().toInt()
                destino = txtdestino.text.toString()
                idproduc = cboproducto.selectedItemPosition
                codproduc = (registroProducto as ArrayList<Product>).get(idproduc).idproduc
                state = if (chbestado_out.isChecked) 1 else 0
                fechita = fechaAct

                objoutput.idproduc = codproduc
                objoutput.quantity = cantidad
                objoutput.dateout = fechita
                objoutput.destino = destino
                objoutput.state = state

                registrarOutput(raiz.context,objoutput)
                val foutput = OutputFragment()
                DialogoCRUDv2("Registro de Salida","Se registro la salida correctamente",foutput)

            }
        }
        lstout.setOnItemClickListener { adapterView, view, i, l ->
            fila = i
            lblidout.text = (registroOutput as ArrayList<Output>).get(fila).idout.toString()
            txtcant.setText(""+(registroOutput as ArrayList<Output>).get(fila).quantity.toString().toInt())
            txtfecha.setText(""+(registroOutput as ArrayList<Output>).get(fila).dateout.toString())
            txtdestino.setText(""+(registroOutput as ArrayList<Output>).get(fila).destino.toString())
            for(x in (registroProducto as ArrayList<Product>).indices){
                if((registroProducto as ArrayList<Product>).get(x).idproduc == (registroOutput as ArrayList<Output>).get(fila).idproduc){
                    indiceProduct = x
                }
            }
            cboproducto.setSelection(indiceProduct)
            if ((registroOutput as ArrayList<Output>).get(fila).state != 0){
                chbestado_out.setChecked(true)
            }else{
                chbestado_out.setChecked(false)
            }
        }

        btnActualizar_out.setOnClickListener {
            if(fila>=0){
                cod = lblidout.text.toString().toInt()
                cantidad = txtcant.text.toString().toInt()
                destino = txtdestino.text.toString()
                idproduc = cboproducto.selectedItemPosition
                codproduc = (registroProducto as ArrayList<Product>).get(idproduc).idproduc
                state = if (chbestado_out.isChecked) 1 else 0
                fechita = fechaAct

                objoutput.idproduc = codproduc
                objoutput.quantity = cantidad
                objoutput.dateout = fechita
                objoutput.destino = destino
                objoutput.state = state

                actualizarOutput(raiz.context,objoutput,cod.toLong())
                val foutput = OutputFragment()
                DialogoCRUDv2("Actualización de Salida","Se actualizó la salida correctamente",foutput)
            }else{
                objutilidad.MensajeToast(raiz.context,"Seleccione un elemento de la lista")
                lstout.requestFocus()
            }
        }

        return  raiz
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
                    cboproducto.adapter = AdaptadorComboProducto(context, registroProducto)
                }
            }

            override fun onFailure(call: Call<List<Product>?>, t: Throwable) {
                Log.e("Error Combo: ", t.message.toString())
            }

        })
    }

    fun mostrarOutput(context:Context){
        val call = outputService!!.MostrarOutputs()
        call!!.enqueue(object: Callback<List<Output>>{
            override fun onResponse(call: Call<List<Output>>, response: Response<List<Output>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroOutput = response.body()
                    lstout.adapter = AdaptadorOutput(context,registroOutput)
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
        val call = outputService!!.ActualizarProduct(id,o)
        call!!.enqueue(object :Callback<List<Output>?>{
            override fun onResponse(call: Call<List<Output>?>, response: Response<List<Output>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente la Salida")
                }
            }

            override fun onFailure(call: Call<List<Output>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
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