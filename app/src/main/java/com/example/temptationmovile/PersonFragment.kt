package com.example.temptationmovile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.temptationmovile.adaptadores.AdaptadorComboRol
import com.example.temptationmovile.adaptadores.AdaptadorPerson
import com.example.temptationmovile.adaptadores.AdaptadorProduct
import com.example.temptationmovile.clases.Person
import com.example.temptationmovile.clases.Product
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.PersonService
import com.example.temptationmovile.servicios.RolService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.temptationmovile.databinding.FragmentPersonBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PersonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonFragment : Fragment() {

    private var _binding:FragmentPersonBinding?=null
    private val binding get() = _binding!!
    private lateinit var adaptadorPerson: AdaptadorPerson
    private var llmanager:LinearLayoutManager?=null

    val format = SimpleDateFormat("yyyy-MM-dd") // crear el formato de fecha
    private var fila:Int?=null

    private var personService:PersonService?=null
    private var rolService:RolService?=null

    private var registroPerson:MutableList<Person>?=null
    private var registroRol:List<Rol>?=null

    var objutilidad =  Util()
    var objPerson=Person()

    private var dialogo: AlertDialog.Builder? = null
    var ft: FragmentTransaction?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentPersonBinding.inflate(inflater, container, false)
        var context=binding.root.context
        llmanager=LinearLayoutManager(context)

        registroPerson=ArrayList()
        registroRol=ArrayList()
        rolService=ApiUtil.rolService
        personService=ApiUtil.personService
        mostrarUsers(context)
        mostrarComboRol(context)
        binding.btnregistrarPerson.setOnClickListener{
            if (binding.spinnerRol.selectedItemPosition==-1){
                objutilidad.MensajeToast(context,"Seleccione el Rol")
                binding.spinnerRol.requestFocus()
            }else if (binding.txtNombrePerson.text.toString()==""){
                objutilidad.MensajeToast(context,"Ingrese el Nombre")
                binding.txtNombrePerson.requestFocus()
            }else if (binding.txtApellidosPerson.text.toString()==""){
                objutilidad.MensajeToast(context,"Ingrese el Apellido")
                binding.txtApellidosPerson.requestFocus()
            }else if (binding.txtDatebPerson.text.toString()==""){
                objutilidad.MensajeToast(context,"Ingrese Fecha de Nacimiento")
                binding.txtDatebPerson.requestFocus()
            }else if (binding.txtDniPerson.text.toString()==""){
                objutilidad.MensajeToast(context,"Ingrese el DNI")
                binding.txtDniPerson.requestFocus()
            }else if (binding.txtUsuarioPerson.text.toString()==""){
                objutilidad.MensajeToast(context,"Ingrese el Usuario")
                binding.txtUsuarioPerson.requestFocus()
            }else if (binding.txtDireccionPerson.text.toString()==""){
                objutilidad.MensajeToast(context,"Ingrese la dirección")
                binding.txtDireccionPerson.requestFocus()
            }else if (!!binding.rbtGenF.isChecked and binding.rbtGenM.isChecked){
                objutilidad.MensajeToast(context,"Seleccione su genero")
                binding.rbtGroupSx.requestFocus()
            }else{
                objPerson.name=binding.txtNombrePerson.text.toString()
                objPerson.lastname=binding.txtApellidosPerson.text.toString()
                val modfecha=SimpleDateFormat("yyyy-MM-dd")
                println("Fecha  "+modfecha.format(format.parse(binding.txtDatebPerson.text.toString())))
                objPerson.date_b= modfecha.format(format.parse(binding.txtDatebPerson.text.toString()))// analizar la fecha y convertirla en un objeto DatetxtDateb_person.text.
                objPerson.gender=if (binding.rbtGenM.isChecked)"M" else "F"
                objPerson.username=binding.txtUsuarioPerson.text.toString()
                objPerson.password=binding.txtUsuarioPerson.text.toString()
                objPerson.address=binding.txtDireccionPerson.text.toString()
                objPerson.state=if (binding.chbestadoPerson.isChecked) 1 else 0
                objPerson.idrol=(registroRol as ArrayList<Rol>).get(binding.spinnerRol.selectedItemPosition).idrol
                objPerson.dni=binding.txtDniPerson.text.toString()
                registrarUsuario(context,objPerson)
                DialogoCRUD("Registro de Usuario","Se registro el usuario",PersonFragment())
            }
        }

        binding.btnactualizarPerson.setOnClickListener{
            if(fila !=null) {
                if (binding.spinnerRol.selectedItemPosition==-1){
                    objutilidad.MensajeToast(context,"Seleccione el Rol")
                    binding.spinnerRol.requestFocus()
                }else if (binding.txtNombrePerson.text.toString()==""){
                    objutilidad.MensajeToast(context,"Ingrese el Nombre")
                    binding.txtNombrePerson.requestFocus()
                }else if (binding.txtApellidosPerson.text.toString()==""){
                    objutilidad.MensajeToast(context,"Ingrese el Apellido")
                    binding.txtApellidosPerson.requestFocus()
                }else if (binding.txtDatebPerson.text.toString()==""){
                    objutilidad.MensajeToast(context,"Ingrese Fecha de Nacimiento")
                    binding.txtDatebPerson.requestFocus()
                }else if (binding.txtDniPerson.text.toString()==""){
                    objutilidad.MensajeToast(context,"Ingrese el DNI")
                    binding.txtDniPerson.requestFocus()
                }else if (binding.txtUsuarioPerson.text.toString()==""){
                    objutilidad.MensajeToast(context,"Ingrese el Usuario")
                    binding.txtUsuarioPerson.requestFocus()
                }else if (binding.txtDireccionPerson.text.toString()==""){
                    objutilidad.MensajeToast(context,"Ingrese la dirección")
                    binding.txtDireccionPerson.requestFocus()
                }else if (!!binding.rbtGenF.isChecked and binding.rbtGenM.isChecked){
                    objutilidad.MensajeToast(context,"Seleccione su genero")
                    binding.rbtGroupSx.requestFocus()
                }else{
                    objPerson.name=binding.txtNombrePerson.text.toString()
                    objPerson.lastname=binding.txtApellidosPerson.text.toString()
                    val modfecha=SimpleDateFormat("yyyy-MM-dd")
                    println("Fecha  "+modfecha.format(format.parse(binding.txtDatebPerson.text.toString())))
                    objPerson.date_b= modfecha.format(format.parse(binding.txtDatebPerson.text.toString()))// analizar la fecha y convertirla en un objeto DatetxtDateb_person.text.
                    objPerson.gender=if (binding.rbtGenM.isChecked)"M" else "F"
                    objPerson.username=binding.txtUsuarioPerson.text.toString()
                    objPerson.password=binding.txtUsuarioPerson.text.toString()
                    objPerson.address=binding.txtDireccionPerson.text.toString()
                    objPerson.state=if (binding.chbestadoPerson.isChecked) 1 else 0
                    objPerson.idrol=(registroRol as ArrayList<Rol>).get(binding.spinnerRol.selectedItemPosition).idrol
                    objPerson.dni=binding.txtDniPerson.text.toString()
                    objPerson.idperson=binding.lblidPerson.text.toString().toInt()
                    ActualizarUsuario(context, objPerson,objPerson.idperson.toLong())
                    DialogoCRUD("Actualización de Usuario", "Se actualizo el usuario", PersonFragment())
                }
            }else{
                objutilidad.MensajeToast(context,"Seleccione un elemento de la lista")
                binding.lstPerson.requestFocus()
            }
        }
        return binding.root
    }

    fun mostrarComboRol(context: Context){
        val call=rolService!!.MostrarRol()
        call.enqueue(object : Callback<List<Rol>?> {
            override fun onResponse(call: Call<List<Rol>?>, response: Response<List<Rol>?>) {
                if(response.isSuccessful){
                    registroRol=response.body()
                    binding.spinnerRol.adapter=AdaptadorComboRol(context,registroRol)
                }
            }

            override fun onFailure(call: Call<List<Rol>?>, t: Throwable) {
                Log.e("Error : ",t.message.toString())
            }
        })
    }
    fun onClickListener(person:Person,pos:Int){
        fila=pos
        binding.txtApellidosPerson.setText(person.lastname.toString())
        binding.txtDatebPerson.setText(person.date_b.toString())
        binding.txtNombrePerson.setText(person.name.toString())
        binding.lblidPerson.setText(person.idperson.toString())
        binding.txtDniPerson.setText(person.dni.toString())
        binding.txtDireccionPerson.setText(person.address.toString())
        binding.txtUsuarioPerson.setText(person.username.toString())
        if (person.gender.toString()=="M"){
            binding.rbtGenM.isChecked=true
        }else{
            binding.rbtGenF.isChecked=true
        }
        if(person.state==1)binding.chbestadoPerson.isChecked=true else binding.chbestadoPerson.isChecked=false
        for ( x in (registroRol as ArrayList<Rol>).indices){
            if ((registroRol as ArrayList<Rol>).get(x).idrol==person.idrol){
                binding.spinnerRol.setSelection(x)
            }
        }
    }
    fun onClickDeleteChangued(del:Int,person:Person){
        if (person.state==1){
            EliminarUsuario(binding.root.context,person.idperson.toLong())
            DialogoCRUD("Usuario Deshabilitado","Se deshabilito el estado del Usuario "+person.name,PersonFragment())
        }else{
            person.state=1
            ActualizarUsuario(binding.root.context,person,person.idperson.toLong())
            DialogoCRUD("Usuario Habilitado","Se habilitó el estado del Usuario "+person.name,PersonFragment())
        }
    }
    fun mostrarUsers(context: Context){
        val call = personService!!.MostrarUsuarios()
        call!!.enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroPerson = response.body() as MutableList<Person>
                    adaptadorPerson = AdaptadorPerson(lista=registroPerson!!,
                        onClickListener={person,pos->onClickListener(person,pos)},
                        onClickDeleteChangued={del,person->onClickDeleteChangued(del,person)})
                }else{
                    println("Error")
                }
            }
            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }
        })
    }

    fun registrarUsuario(context: Context, p: Person){
        val call = personService!!.RegistrarUsuario(p)
        call!!.enqueue(object : Callback<Person?> {
            override fun onResponse(call: Call<Person?>, response: Response<Person?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro el usuario")
                }
            }

            override fun onFailure(call: Call<Person?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }
    fun ActualizarUsuario(context: Context, p: Person, id: Long ){
        val call = personService!!.ActualizarUsuario(id,p)
        call!!.enqueue(object : Callback<Person?> {
            override fun onResponse(call: Call<Person?>, response: Response<Person?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Usuario")
                    fila=null
                }
            }
            override fun onFailure(call: Call<Person?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }
        })
    }

    fun EliminarUsuario(context: Context, id: Long){
        val call = personService!!.EliminarUsuario(id)
        call!!.enqueue(object: Callback<Person?> {
            override fun onResponse(call: Call<Person?>, response: Response<Person?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }
            override fun onFailure(call: Call<Person?>, t: Throwable) {
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
}