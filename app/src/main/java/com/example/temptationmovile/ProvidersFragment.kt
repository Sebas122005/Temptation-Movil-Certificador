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
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.temptationmovile.adaptadores.AdaptadorProvider
import com.example.temptationmovile.clases.*
import com.example.temptationmovile.databinding.BrandFragmentBinding
import com.example.temptationmovile.databinding.FragmentProvidersBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.ProviderService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProvidersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProvidersFragment : Fragment() {
    private var _binding:FragmentProvidersBinding?=null
    private val binding get() = _binding!!
    private lateinit var adaptadorProvider: AdaptadorProvider
    private var llmanager:LinearLayoutManager?=null
    val objprob = Provider()
    private var fila:Int?=null

    private var providerservice: ProviderService? = null
    private var registroprovider: MutableList<Provider>?=null

    var objutilidad =  Util()
    private var dialogo: AlertDialog.Builder? = null
    var ft: FragmentTransaction?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_provider, container, false)
        _binding=FragmentProvidersBinding.inflate(inflater,container,false)
        //creamos los controles
        var context=binding.root.context
        llmanager=LinearLayoutManager(context)

        registroprovider = ArrayList()
        providerservice = ApiUtil.providerService

        mostrarProvider(context)

        binding.btnregistrarprov.setOnClickListener {
            if (binding.txtnombreprov1.getText().toString() == "") {
                objutilidad.MensajeToast(context, "Ingrese el Nombre")
                binding.txtnombreprov1.requestFocus()
            } else {
                //envienadoo los valores
                objprob.name_prov = binding.txtnombreprov1.text.toString()
                objprob.ruc=binding.txtrucprov.text.toString()
                objprob.address=binding.txtdireccionProv.text.toString()
                objprob.company_name=binding.txtempresaProv.text.toString()
                objprob.phone=binding.txtphoneProv.text.toString().toInt()
                objprob.email=binding.txtemailProv.text.toString()
                objprob.description=binding.txtdescriptionProv.text.toString()
                objprob.state = if (binding.chbprovider1.isChecked) 1 else 0
                registrar(context, objprob)
                DialogoCRUD("Registro de Proveedor","Se registro el proveedor",ProvidersFragment())
            }
        }


        binding.btneliminarprov.setOnClickListener {
            if(fila!=null){
                EliminarProduct(context,binding.lblidprovider.text.toString().toLong())
                val fprovider = ProvidersFragment()
                DialogoCRUD("Se Deshabilito  del Proveedor", "Se Cambio el estado a deshabilitado del proveedor ",fprovider)
            }else{
                objutilidad.MensajeToast(context,"Seleccione un elemento de la lista")
                binding.lstlistarprovider.requestFocus()
            }
        }

        binding.btnactualizarprov.setOnClickListener {
            if(fila !=null){
                objprob.name_prov = binding.txtnombreprov1.text.toString()
                objprob.ruc=binding.txtrucprov.text.toString()
                objprob.address=binding.txtdireccionProv.text.toString()
                objprob.company_name=binding.txtempresaProv.text.toString()
                objprob.phone=binding.txtphoneProv.text.toString().toInt()
                objprob.email=binding.txtemailProv.text.toString()
                objprob.description=binding.txtdescriptionProv.text.toString()
                objprob.state = if (binding.chbprovider1.isChecked) 1 else 0
                objprob.idprovider=binding.lblidprovider.text.toString().toInt()
                ActualizarProvider(context,objprob,objprob.idprovider.toLong())
                val fprovider = ProvidersFragment()
                DialogoCRUD("Actualización de Proveedor", "Se actualizo el proveedor correctamente",fprovider)
            }else{
                objutilidad.MensajeToast(context,"Seleccione un elemento de la lista")
                binding.lstlistarprovider.requestFocus()
            }
        }


        return binding.root


    }

    fun onClickListener(prov:Provider,pos:Int){
        binding.txtdescriptionProv.setText(prov.description.toString())
        binding.txtnombreprov1.setText(prov.name_prov.toString())
        binding.txtemailProv.setText(prov.email.toString())
        binding.txtempresaProv.setText(prov.company_name.toString())
        binding.txtphoneProv.setText(prov.phone.toString())
        binding.txtrucprov.setText(prov.ruc.toString())
        binding.txtdireccionProv.setText(prov.address.toString())
        if (prov.state==1){
            binding.chbprovider1.setChecked(true)
        }else{
            binding.chbprovider1.setChecked(false)
        }
        binding.lblidprovider.setText(prov.idprovider.toString())
    }
    fun onClickDeleteChangued(pos:Int,prov:Provider){
        if (prov.state==1){
            EliminarProduct(binding.root.context,prov.idprovider.toLong())
            DialogoCRUD("Proveedor Deshabilitado","Se deshabilito el estado del proveedor  "+prov.name_prov,ProvidersFragment())
        }else{
            prov.state=1
            ActualizarProvider(binding.root.context,prov,prov.idprovider.toLong())
            DialogoCRUD("Proveedor Habilitado","Se habilitó el estado del proveedor "+prov.name_prov,ProvidersFragment())
        }
    }
    //CREAMOS LA FUNCION PARA MOSTRAR LAS CATEGORIAS
    fun mostrarProvider(context: Context){
        val call = providerservice!!.MostrarProvider()
        call!!.enqueue(object : Callback<List<Provider>?> {
            override fun onResponse(
                call: Call<List<Provider>?>,
                response: Response<List<Provider>?>
            ) {
                if(response.isSuccessful){
                    registroprovider = response.body() as MutableList<Provider>
                    adaptadorProvider = AdaptadorProvider(lista=registroprovider!!,
                        onClickListener={b,pos->onClickListener(b,pos)},
                        onClickDeleteChangued={del,bra->onClickDeleteChangued(del,bra)})
                }
            }
            override fun onFailure(call: Call<List<Provider>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }
        })
    }

    fun registrar(context: Context,c:Provider){
        val call = providerservice!!.RegistrarProvider(c)
        call!!.enqueue(object :Callback<Provider?>{
            override fun onResponse(call: Call<Provider?>, response: Response<Provider?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro al proveedor")
                }
            }
            override fun onFailure(call: Call<Provider?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }

    fun ActualizarProvider(context: Context, p:Provider, id: Long ){
        val call = providerservice!!.ActualizarProvider(id,p)
        call!!.enqueue(object : Callback<Provider?>{
            override fun onResponse(call: Call<Provider?>, response: Response<Provider?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Proveedor")
                    fila=null
                }
            }

            override fun onFailure(call: Call<Provider?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }

    fun EliminarProduct(context: Context, id: Long){
        val call = providerservice!!.EliminarrProvider(id)
        call!!.enqueue(object: Callback<Provider?>{
            override fun onResponse(call: Call<Provider?>, response: Response<Provider?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<Provider?>, t: Throwable) {
                Log.e("Error",t.message!!)
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
    fun DialogoCRUDEliminar(titulo: String, mensaje: String, fragment: Fragment) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}