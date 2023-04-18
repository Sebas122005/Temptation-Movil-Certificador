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
import com.example.temptationmovile.adaptadores.AdaptadorRol
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.databinding.FragmentRolBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.RolService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RolFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var txtRol: EditText
    private lateinit var chbEst: CheckBox
    private lateinit var lblCodRol: TextView
    private lateinit var btnRegistra: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var lstRol: ListView

    val objRol= Rol()
    private var idrol = 0
    private var name_rol=""
    private var state=1
    private var fila=-1

    private var rolService:RolService?=null
    private var registroRol:List<Rol>?=null
    var objutil = Util()

    var ft:FragmentTransaction?=null

    private var dialogo: AlertDialog.Builder? = null

    private var _binding:FragmentRolBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz=inflater.inflate(R.layout.fragment_rol,container,false)
        //creamos los controles
        txtRol=raiz.findViewById(R.id.txtRol)
        chbEst=raiz.findViewById(R.id.chbestado_rol)
        lblCodRol=raiz.findViewById(R.id.lblidRol)
        btnRegistra=raiz.findViewById(R.id.btnregistrar_rol)
        btnActualizar=raiz.findViewById(R.id.btnactualizar_rol)
        btnEliminar=raiz.findViewById(R.id.btneliminar_rol)
        lstRol=raiz.findViewById(R.id.lstRol)
        lstRol.isNestedScrollingEnabled = true

        registroRol = ArrayList()

        rolService = ApiUtil.rolService


        mostrarRol(raiz.context)


        btnRegistra.setOnClickListener {
            if (txtRol.getText().toString() == "") {
                objutil.MensajeToast(raiz.context, "Ingrese el Nombre")
                txtRol.requestFocus()
            } else {
                name_rol = txtRol.getText().toString()
                state = if (chbEst.isChecked) 1 else 0
                //envienadoo los valores
                objRol.namerol= name_rol
                objRol.state = state
                Log.e(objRol.namerol, (objRol.state).toString())
                registrar(raiz.context, objRol)
                Log.e(objRol.namerol, (objRol.state).toString())
                //actualizamos el rol
                val frol = RolFragment()
                DialogoCRUD("Registro de rol","Se registro el rol",frol)
            }
        }
        btnActualizar.setOnClickListener {
            if(fila>=0){
                idrol =  lblCodRol.getText().toString().toInt()
                name_rol =  txtRol.getText().toString()
                state =  if (chbEst.isChecked) 1 else 0
                objRol.idrol = idrol
                objRol.namerol = name_rol
                objRol.state = state
                AnctualizarRol(raiz.context, objRol, idrol.toLong())
                val fra = RolFragment()
                DialogoCRUD("Actualización de rol", "Se actualizó el rol",fra)
            }else{
                lstRol.requestFocus()
            }
        }

        btnEliminar.setOnClickListener {
            if(fila>=0){
                idrol = lblCodRol.getText().toString().toInt()
                objRol.idrol = idrol
                EliminarBrand(raiz.context,idrol.toLong())
                val fra =  RolFragment()
                DialogoCRUD("Eliminar el rol", "Se elimino el rol",fra)
            }else{
                lstRol.requestFocus()
            }
        }

        lstRol.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                //asignamos los valores a cada control
                lblCodRol.setText(""+(registroRol as ArrayList<Rol>).get(fila).idrol)
                txtRol.setText(""+(registroRol as ArrayList<Rol>).get(fila).namerol)
                if((registroRol as ArrayList<Rol>).get(fila).state != 0){
                    chbEst.setChecked(true)
                }else{
                    chbEst.setChecked(false)
                }

            }
        )
        return raiz
    }

    fun mostrarRol(context: Context){
        val call = rolService!!.MostrarRol()
        call!!.enqueue(object:Callback<List<Rol>?> {
            override fun onResponse(call: Call<List<Rol>?>, response: Response<List<Rol>?>) {
                if (response.isSuccessful){
                    registroRol=response.body()
                    lstRol.adapter=AdaptadorRol(context,registroRol)
                }
            }
            override fun onFailure(call: Call<List<Rol>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }
    fun registrar(context: Context, r:Rol){
        val call = rolService!!.RegistrarRol(r)
        call!!.enqueue(object : Callback<Rol?> {
            override fun onResponse(call: Call<Rol?>, response: Response<Rol?>) {
                if (response.isSuccessful){
                    objutil.MensajeToast(context,"Se registro el rol")
                }
            }

            override fun onFailure(call: Call<Rol?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }

    fun AnctualizarRol(context: Context, b: Rol, id: Long ){
        val call = rolService!!.ActualizarRol(id,b)
        call!!.enqueue(object : Callback<List<Rol>?>{
            override fun onResponse(call: Call<List<Rol>?>, response: Response<List<Rol>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente")
                }
            }

            override fun onFailure(call: Call<List<Rol>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }
    fun EliminarBrand(context: Context, id: Long){
        val call = rolService!!.EliminarRol(id)
        call!!.enqueue(object: Callback<List<Rol>?>{
            override fun onResponse(call: Call<List<Rol>?>, response: Response<List<Rol>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Rol>?>, t: Throwable) {
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}