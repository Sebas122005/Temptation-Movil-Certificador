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
import com.example.temptationmovile.adaptadores.AdaptadorRol
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.RolService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.temptationmovile.databinding.FragmentRolBinding


class RolFragment : Fragment() {

    private var _binding:FragmentRolBinding?=null
    private val binding get() = _binding!!
    private lateinit var adaptadorRol: AdaptadorRol
    private var llmanager:LinearLayoutManager?=null

    val objRol= Rol()

    private var fila:Int?=null

    private var rolService:RolService?=null
    private var registroRol:MutableList<Rol>?=null
    var objutil = Util()

    var ft:FragmentTransaction?=null

    private var dialogo: AlertDialog.Builder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentRolBinding.inflate(inflater,container,false)
        //creamos los controles
        var context=binding.root.context
        llmanager= LinearLayoutManager(context)
        registroRol = ArrayList()
        rolService = ApiUtil.rolService
        mostrarRol()

        binding.btnregistrarRol.setOnClickListener {
            if (binding.txtRol.getText().toString() == "") {
                objutil.MensajeToast(context, "Ingrese el Nombre")
                binding.txtRol.requestFocus()
            } else {
                //envienadoo los valores
                objRol.namerol= binding.txtRol.text.toString()
                objRol.state = if (binding.chbestadoRol.isChecked) 1 else 0
                registrar(context, objRol)
                //actualizamos el rol
                val frol = RolFragment()
                DialogoCRUD("Registro de rol","Se registro el rol",frol)
            }
        }
        binding.btnactualizarRol.setOnClickListener {
            if(fila!=null){
                objRol.idrol = binding.lblidRol.text.toString().toInt()
                objRol.namerol = binding.txtRol.text.toString()
                objRol.state = if (binding.chbestadoRol.isChecked) 1 else 0
                AnctualizarRol(objRol, objRol.idrol.toLong())
                val fra = RolFragment()
                DialogoCRUD("Actualización de rol", "Se actualizó el rol",fra)
            }else{
                binding.lstRol.requestFocus()
            }
        }

        binding.btneliminarRol.setOnClickListener {
            if(fila!=null){
                objRol.idrol = binding.lblidRol.text.toString().toInt()
                EliminarRol(objRol.idrol.toLong())
                val fra =  RolFragment()
                DialogoCRUD("Eliminación del rol", "Se elimino el rol",fra)
            }else{
                binding.lstRol.requestFocus()
            }
        }
        return binding.root
    }

    fun onClickListener(rol:Rol,pos:Int){
        binding.txtRol.setText(rol.namerol.toString())
        binding.lblidRol.setText(rol.idrol.toString())
        if (rol.state==1) {
            binding.chbestadoRol.setChecked(true)
        }else {
            binding.chbestadoRol.setChecked(false)
        }
    }
    fun onClickDeleteChangued(pos:Int,rol:Rol){
        if (rol.state==1){
            EliminarRol(rol.idrol.toLong())
            DialogoCRUD("Rol Deshabilitado","Se deshabilito el estado del rol  "+rol.namerol,RolFragment())
        }else{
            rol.state=1
            AnctualizarRol(rol,rol.idrol.toLong())
            DialogoCRUD("Rol Habilitado","Se habilitó el estado del rol "+rol.namerol,RolFragment())
        }
    }
    fun mostrarRol(){
        val call = rolService!!.MostrarRol()
        call!!.enqueue(object:Callback<List<Rol>?> {
            override fun onResponse(call: Call<List<Rol>?>, response: Response<List<Rol>?>) {
                if (response.isSuccessful){
                    registroRol=response.body() as MutableList<Rol>
                    adaptadorRol=AdaptadorRol(lista=registroRol!!,
                        onClickListener={b,pos->onClickListener(b,pos)},
                        onClickDeleteChangued={del,bra->onClickDeleteChangued(del,bra)})
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

    fun AnctualizarRol(b: Rol, id: Long ){
        val call = rolService!!.ActualizarRol(id,b)
        call!!.enqueue(object : Callback<Rol?>{
            override fun onResponse(call: Call<Rol?>, response: Response<Rol?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente")
                    fila=null
                }
            }

            override fun onFailure(call: Call<Rol?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }
    fun EliminarRol( id: Long){
        val call = rolService!!.EliminarRol(id)
        call!!.enqueue(object: Callback<Rol?>{
            override fun onResponse(call: Call<Rol?>, response: Response<Rol?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<Rol?>, t: Throwable) {
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