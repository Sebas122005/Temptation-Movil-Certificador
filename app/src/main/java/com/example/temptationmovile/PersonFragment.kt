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

    private lateinit var spinner_Rol:Spinner
    private lateinit var txtNombre_person:EditText
    private lateinit var txtApellidos_person:EditText
    private lateinit var txtDateb_person:EditText
    private lateinit var txtDni_person:EditText
    private lateinit var rbtGroupSx:RadioGroup
    private lateinit var rbtGenM:RadioButton
    private lateinit var rbtGenF:RadioButton
    private lateinit var txtDireccion_person:EditText
    private lateinit var txtUsuario_person:EditText
    private lateinit var chbestado_person:CheckBox
    private lateinit var lblid_person:TextView
    private lateinit var btnregistrar_person:Button
    private lateinit var btnactualizar_person:Button
    private lateinit var btneliminar_person:Button
    private lateinit var lstPerson:ListView


    val format = SimpleDateFormat("yyyy-MM-dd") // crear el formato de fecha

    private val objPerson=Person()
    private val objRol=Rol()
    private var idperson=0
    private var idrol=Rol()
    private var name = ""
    private var lastname=""
    private var date_b=Date()
    private var dni=""
    private var gender=""
    private var address=""
    private var username=""
    private var password=""
    private var state=0
    private var key=""
    private var fila=-1


    private var personService:PersonService?=null
    private var rolService:RolService?=null

    private var registroPerson:List<Person>?=null
    private var registroRol:List<Rol>?=null

    private var indiceRol = 0

    var objutilidad =  Util()

    private var dialogo: AlertDialog.Builder? = null
    var ft: FragmentTransaction?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz= inflater.inflate(R.layout.fragment_person, container, false)
        lblid_person=raiz.findViewById(R.id.lblid_person)
        spinner_Rol=raiz.findViewById(R.id.spinner_Rol)
        txtNombre_person=raiz.findViewById(R.id.txtNombre_person)
        txtApellidos_person=raiz.findViewById(R.id.txtApellidos_person)
        txtDateb_person=raiz.findViewById(R.id.txtDateb_person)
        txtDni_person=raiz.findViewById(R.id.txtDni_person)
        rbtGroupSx=raiz.findViewById(R.id.rbtGroupSx)
        rbtGenM=raiz.findViewById(R.id.rbtGenM)
        rbtGenF=raiz.findViewById(R.id.rbtGenF)
        txtDireccion_person=raiz.findViewById(R.id.txtDireccion_person)
        txtUsuario_person=raiz.findViewById(R.id.txtUsuario_person)
        chbestado_person=raiz.findViewById(R.id.chbestado_person)
        btnregistrar_person=raiz.findViewById(R.id.btnregistrar_person)
        btnactualizar_person=raiz.findViewById(R.id.btnactualizar_person)
        btneliminar_person=raiz.findViewById(R.id.btneliminar_person)
        lstPerson=raiz.findViewById(R.id.lstPerson)
        lstPerson.isNestedScrollingEnabled = true

        registroPerson=ArrayList()
        registroRol=ArrayList()
        rolService=ApiUtil.rolService
        personService=ApiUtil.personService
        mostrarUsers(raiz.context)
        mostrarComboRol(raiz.context)
        btnregistrar_person.setOnClickListener{
            if (spinner_Rol.selectedItemPosition==-1){
                objutilidad.MensajeToast(raiz.context,"Seleccione el Rol")
                spinner_Rol.requestFocus()
            }else if (txtNombre_person.text.toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese el Nombre")
                txtNombre_person.requestFocus()
            }else if (txtApellidos_person.text.toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese el Apellido")
                txtApellidos_person.requestFocus()
            }else if (txtDateb_person.text.toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese Fecha de Nacimiento")
                txtDateb_person.requestFocus()
            }else if (txtDni_person.text.toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese el DNI")
                txtDni_person.requestFocus()
            }else if (txtUsuario_person.text.toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese el Usuario")
                txtUsuario_person.requestFocus()
            }else if (txtDireccion_person.text.toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese la dirección")
                txtDireccion_person.requestFocus()
            }else if (!!rbtGenF.isChecked and rbtGenM.isChecked){
                objutilidad.MensajeToast(raiz.context,"Seleccione su genero")
                rbtGroupSx.requestFocus()
            }else{
                objPerson.name=txtNombre_person.text.toString()
                objPerson.lastname=txtApellidos_person.text.toString()
                val modfecha=SimpleDateFormat("yyyy-MM-dd")
                println("Fecha  "+modfecha.format(format.parse(txtDateb_person.text.toString())))
                objPerson.date_b= modfecha.format(format.parse(txtDateb_person.text.toString()))// analizar la fecha y convertirla en un objeto DatetxtDateb_person.text.
                objPerson.gender=if (rbtGenM.isChecked)"M" else "F"
                objPerson.username=txtUsuario_person.text.toString()
                objPerson.password=txtUsuario_person.text.toString()
                objPerson.address=txtDireccion_person.text.toString()
                objPerson.state=if (chbestado_person.isChecked) 1 else 0
                println("Rol : "+spinner_Rol.selectedItemPosition)
                objPerson.idrol=(registroRol as ArrayList<Rol>).get(spinner_Rol.selectedItemPosition).idrol
                objPerson.dni=txtDni_person.text.toString()
                registrarUsuario(raiz.context,objPerson)
                DialogoCRUD("Registro de Usuario","Se registro el usuario",PersonFragment())
            }
        }

        btnactualizar_person.setOnClickListener{
            if(fila >=0) {
                if (spinner_Rol.selectedItemPosition == -1) {
                    objutilidad.MensajeToast(raiz.context, "Seleccione el Rol")
                    spinner_Rol.requestFocus()
                } else if (txtNombre_person.text.toString() == "") {
                    objutilidad.MensajeToast(raiz.context, "Ingrese el Nombre")
                    txtNombre_person.requestFocus()
                } else if (txtApellidos_person.text.toString() == "") {
                    objutilidad.MensajeToast(raiz.context, "Ingrese el Apellido")
                    txtApellidos_person.requestFocus()
                } else if (txtDateb_person.text.toString() == "") {
                    objutilidad.MensajeToast(raiz.context, "Ingrese Fecha de Nacimiento")
                    txtDateb_person.requestFocus()
                } else if (txtDni_person.text.toString() == "") {
                    objutilidad.MensajeToast(raiz.context, "Ingrese el DNI")
                    txtDni_person.requestFocus()
                } else if (txtUsuario_person.text.toString() == "") {
                    objutilidad.MensajeToast(raiz.context, "Ingrese el Usuario")
                    txtUsuario_person.requestFocus()
                } else if (txtDireccion_person.text.toString() == "") {
                    objutilidad.MensajeToast(raiz.context, "Ingrese la dirección")
                    txtDireccion_person.requestFocus()
                }else if (!!rbtGenF.isChecked and rbtGenM.isChecked) {
                    objutilidad.MensajeToast(raiz.context, "Seleccione su genero")
                    rbtGroupSx.requestFocus()
                } else {
                    objPerson.name = txtNombre_person.text.toString()
                    objPerson.lastname = txtApellidos_person.text.toString()
                    val modfecha = SimpleDateFormat("yyyy-MM-dd")
                    println("Fecha  " + modfecha.format(format.parse(txtDateb_person.text.toString())))
                    objPerson.date_b =
                        modfecha.format(format.parse(txtDateb_person.text.toString()))// analizar la fecha y convertirla en un objeto DatetxtDateb_person.text.
                    idperson=lblid_person.text.toString().toInt()
                    objPerson.gender=if (rbtGenM.isChecked)"M" else "F"
                    objPerson.username = txtUsuario_person.text.toString()
                    objPerson.password = txtUsuario_person.text.toString()
                    objPerson.address = txtDireccion_person.text.toString()
                    objPerson.state = if (chbestado_person.isChecked()) 1 else 0
                    println("Rol : " + spinner_Rol.selectedItemPosition)
                    objPerson.idrol = (registroRol as ArrayList<Rol>).get(spinner_Rol.selectedItemPosition).idrol
                    objPerson.dni = txtDni_person.text.toString()
                    objPerson.key=""
                    ActualizarUsuario(raiz.context, objPerson,idperson.toLong())
                    DialogoCRUD("Actualización de Usuario", "Se actualizo el usuario", PersonFragment())
                }
            }else{
                objutilidad.MensajeToast(raiz.context,"Seleccione un elemento de la lista")
                lstPerson.requestFocus()
            }
        }

        btneliminar_person.setOnClickListener {
            if (fila>=0){
                objPerson.idperson=lblid_person.text.toString().toInt()
                EliminarUsuario(raiz.context,objPerson.idperson.toLong())
                DialogoCRUD("Usuario eliminado","se elimino al usuario",PersonFragment())
            }else{
                objutilidad.MensajeToast(raiz.context,"Seleccione un elemento de la lista")
                lstPerson.requestFocus()
            }
        }

        lstPerson.setOnItemClickListener { adapterView, view, i, l ->
            fila = i
            lblid_person.text = (registroPerson as ArrayList<Person>).get(fila).idperson.toString()
            txtNombre_person.setText(""+(registroPerson as ArrayList<Person>).get(fila).name)
            txtApellidos_person.setText(""+(registroPerson as ArrayList<Person>).get(fila).lastname)
            txtDateb_person.setText(""+(registroPerson as ArrayList<Person>).get(fila).date_b)
            txtDni_person.setText(""+(registroPerson as ArrayList<Person>).get(fila).dni)
            var sx=(registroPerson as ArrayList<Person>).get(fila).gender.toString().trim()
            if (sx=="M"){
                rbtGenM.isChecked=true
            }else if (sx=="F"){
                rbtGenF.isChecked=true
            }
            txtDireccion_person.setText(""+(registroPerson as ArrayList<Person>).get(fila).address)
            txtUsuario_person.setText(""+(registroPerson as ArrayList<Person>).get(fila).username)
            if(((registroPerson as ArrayList<Person>).get(fila).state)==1)chbestado_person.isChecked=true else chbestado_person.isChecked=false
            for ( x in (registroRol as ArrayList<Rol>).indices){
                if ((registroRol as ArrayList<Rol>).get(x).idrol==(registroPerson as ArrayList<Person>).get(fila).idrol){
                    indiceRol=x
                }
            }
            spinner_Rol.setSelection(indiceRol)
        }

        return raiz
    }

    fun mostrarComboRol(context: Context){
        val call=rolService!!.MostrarRol()
        call.enqueue(object : Callback<List<Rol>?> {
            override fun onResponse(call: Call<List<Rol>?>, response: Response<List<Rol>?>) {
                if(response.isSuccessful){
                    registroRol=response.body()
                    spinner_Rol.adapter=AdaptadorComboRol(context,registroRol)
                }
            }

            override fun onFailure(call: Call<List<Rol>?>, t: Throwable) {
                Log.e("Error : ",t.message.toString())
            }
        })
    }

    fun mostrarUsers(context: Context){
        val call = personService!!.MostrarUsuarios()
        call!!.enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroPerson = response.body()
                    lstPerson.adapter = AdaptadorPerson(context,registroPerson)
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
        call!!.enqueue(object : Callback<List<Person>?> {
            override fun onResponse(call: Call<List<Person>?>, response: Response<List<Person>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Usuario")
                }
            }
            override fun onFailure(call: Call<List<Person>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }
        })
    }

    fun EliminarUsuario(context: Context, id: Long){
        val call = personService!!.EliminarUsuario(id)
        call!!.enqueue(object: Callback<List<Person>?> {
            override fun onResponse(call: Call<List<Person>?>, response: Response<List<Person>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }
            override fun onFailure(call: Call<List<Person>?>, t: Throwable) {
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