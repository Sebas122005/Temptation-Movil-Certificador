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
import com.example.temptationmovile.adaptadores.AdaptadorProvider
import com.example.temptationmovile.clases.*
import com.example.temptationmovile.databinding.BrandFragmentBinding
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
    private lateinit var txt_Nomb: EditText
    private lateinit var chb_Est: CheckBox
    private lateinit var lbl_CodProv: TextView
    private lateinit var btn_Registrar: Button
    private lateinit var btn_Actualizar: Button
    private lateinit var btn_Eliminar: Button
    private lateinit var btn_Salir: Button
    private lateinit var txt_RucProv: EditText
    private lateinit var txt_EmpresaProv: EditText
    private lateinit var txt_TelefonoProv: EditText
    private lateinit var txt_EmailProv: EditText
    private lateinit var txt_DescripcionPro: EditText
    private lateinit var txt_DireccionProv: EditText
    private lateinit var lst_Provider: ListView


    val objprob = Provider()
    private var idprovider=0
    private var name_prov=""
    private var ruc=""
    private var company_name=""
    private var phone=0
    private var email=""
    private var description=""
    private var address=""
    private var state=1
    private var fila =-1

    private lateinit var binding: ProvidersFragment
    private var providerservice: ProviderService? = null
    private var registroprovider: List<Provider>?=null
    var objutilidad =  Util()
    private var dialogo: AlertDialog.Builder? = null

    //creamos transicion para fragmento
    var ft: FragmentTransaction?= null
    private var _binding: BrandFragmentBinding? = null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_provider, container, false)
        val raiz=inflater.inflate(R.layout.fragment_providers,container,false)
        //creamos los controles
        txt_Nomb=raiz.findViewById(R.id.txtnombreprov1)
        chb_Est=raiz.findViewById(R.id.chbprovider1)
        lbl_CodProv=raiz.findViewById(R.id.lblidprovider)
        btn_Registrar=raiz.findViewById(R.id.btnregistrarprov)
        btn_Actualizar=raiz.findViewById(R.id.btnactualizarprov)
        btn_Eliminar=raiz.findViewById(R.id.btneliminarprov)
        lst_Provider=raiz.findViewById(R.id.lstlistarprovider)
        txt_DescripcionPro=raiz.findViewById(R.id.txtdescription_prov)
        txt_DireccionProv=raiz.findViewById(R.id.txtdireccion_prov)
        txt_EmailProv=raiz.findViewById(R.id.txtemail_prov)
        txt_EmpresaProv=raiz.findViewById(R.id.txtempresa_prov)
        txt_RucProv=raiz.findViewById(R.id.txtrucprov)
        txt_TelefonoProv=raiz.findViewById(R.id.txtphone_prov)
        lst_Provider.isNestedScrollingEnabled = true


        registroprovider = ArrayList()

        providerservice = ApiUtil.providerService


        mostrarProvider(raiz.context)

        btn_Registrar.setOnClickListener {
            if (txt_Nomb.getText().toString() == "") {
                objutilidad.MensajeToast(raiz.context, "Ingrese el Nombre")
                txt_Nomb.requestFocus()
            } else {
                name_prov = txt_Nomb.getText().toString()
                ruc = txt_RucProv.getText().toString()
                company_name = txt_EmpresaProv.getText().toString()
                phone = txt_TelefonoProv.getText().toString().toInt()
                email= txt_EmailProv.getText().toString()
                description= txt_DescripcionPro.getText().toString()
                address= txt_DireccionProv.getText().toString()
                state = if (chb_Est.isChecked) 1 else 0
                //envienadoo los valores
                objprob.name_prov = name_prov
                objprob.ruc=ruc
                objprob.address=address
                objprob.company_name=company_name
                objprob.phone=phone
                objprob.email=email
                objprob.description=description
                objprob.state = state
                //Log.e(objprob.name_prov, (objprob.state).toString())
                registrar(raiz.context, objprob)
                //Log.e(objprob.name_prov, (objprob.state).toString())

                //actualizamos el brand
                val fprov = ProvidersFragment()
                ft = fragmentManager?.beginTransaction()
                ft?.replace(R.id.contenedor,fprov,null)
                ft?.addToBackStack(null)
                ft?.commit()
            }
        }

        lst_Provider.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                //asignamos los valores a cada control
                lbl_CodProv.setText(""+(registroprovider as ArrayList<Provider>).get(fila).idprovider)
                txt_Nomb.setText(""+(registroprovider as ArrayList<Provider>).get(fila).name_prov)
                txt_DescripcionPro.setText(""+(registroprovider as ArrayList<Provider>).get(fila).description)
                txt_TelefonoProv.setText(""+(registroprovider as ArrayList<Provider>).get(fila).phone)
                txt_EmailProv.setText(""+(registroprovider as ArrayList<Provider>).get(fila).email)
                txt_RucProv.setText(""+(registroprovider as ArrayList<Provider>).get(fila).ruc)
                txt_EmpresaProv.setText(""+(registroprovider as ArrayList<Provider>).get(fila).company_name)
                txt_DireccionProv.setText(""+(registroprovider as ArrayList<Provider>).get(fila).address)


                if((registroprovider as ArrayList<Provider>).get(fila).state != 0){
                    chb_Est.setChecked(true)
                }else{
                    chb_Est.setChecked(false)
                }

            }
        )

        btn_Eliminar.setOnClickListener {
            if(fila>=0){
                idprovider = lbl_CodProv.text.toString().toInt()


                EliminarProduct(raiz.context,idprovider.toLong())
                val fprovider = ProvidersFragment()
                DialogoCRUDEliminar("¿Eliminar el Producto?", "¿Desea Eliminar el Producto?",fprovider)

            }else{
                objutilidad.MensajeToast(raiz.context,"Seleccione un elemento de la lista")
                lst_Provider.requestFocus()
            }
        }

        btn_Actualizar.setOnClickListener {
            if(fila >=0){
                idprovider = lbl_CodProv.text.toString().toInt()
                name_prov =txt_Nomb.text.toString()
                ruc = txt_RucProv.text.toString()
                company_name = txt_EmpresaProv.text.toString()
                phone = txt_TelefonoProv.text.toString().toInt()
                email = txt_EmailProv.text.toString()
                description = txt_DescripcionPro.text.toString()
                address = txt_DireccionProv.text.toString()
                state = if (chb_Est.isChecked) 1 else 0



                objprob.idprovider = idprovider
                objprob.name_prov = name_prov
                objprob.ruc = ruc
                objprob.company_name = company_name
                objprob.phone = phone
                objprob.email = email
                objprob.description = description
                objprob.address = address
                objprob.state = state
                ActualizarProvider(raiz.context,objprob,idprovider.toLong())
                val fprovider = ProvidersFragment()
                DialogoCRUDEliminar("Actualizacion de Proveedor", "¿Quiere Actualizar el Proveedor?",fprovider)
            }else{
                objutilidad.MensajeToast(raiz.context,"Seleccione un elemento de la lista")
                lst_Provider.requestFocus()
            }
        }


        return raiz


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
                    registroprovider = response.body()
                    lst_Provider.adapter = AdaptadorProvider(context,registroprovider)
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
        call!!.enqueue(object : Callback<List<Provider>?>{
            override fun onResponse(call: Call<List<Provider>?>, response: Response<List<Provider>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Proveedor")
                }
            }

            override fun onFailure(call: Call<List<Provider>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }

    fun EliminarProduct(context: Context, id: Long){
        val call = providerservice!!.EliminarrProvider(id)
        call!!.enqueue(object: Callback<List<Provider>?>{
            override fun onResponse(call: Call<List<Provider>?>, response: Response<List<Provider>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Provider>?>, t: Throwable) {
                Log.e("Error",t.message!!)
            }

        })
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