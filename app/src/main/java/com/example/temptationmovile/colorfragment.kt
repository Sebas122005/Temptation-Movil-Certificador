package com.example.temptationmovile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import com.example.temptationmovile.databinding.ColorfragmentBinding
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.adaptadores.AdaptadorColor
import com.example.temptationmovile.clases.Color
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.ColorService
import com.example.temptationmovile.utilidad.Util
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [colorfragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class colorfragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var txtcolor: EditText
    private lateinit var chbestado: CheckBox
    private lateinit var lblidcolor: TextView
    private lateinit var btnregistrar_color: Button
    private lateinit var btnactualizar_color: Button
    private lateinit var btneliminar_color: Button
    private lateinit var btnsalir_color: Button
    private lateinit var lstcolor: ListView

    //creamos un objeto de la clase categoria
    val objcolor = Color()
    //declaramos variables
    private var cod = 0
    private var nom = ""
    private var est = 1
    private var fila = -1

    //declaramos el servicio
    private var colorservice :ColorService?=null

    //creamos arraylist de categoria
    private var registrocolor:List<Color>?=null

    //creamos un objeto de la clase utilidad
    var objutilidad = Util()

    //creamos transicion para fragmento
    var ft: FragmentTransaction?= null

    private var dialogo: AlertDialog.Builder? = null
    private var _binding: ColorfragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz = inflater.inflate(R.layout.colorfragment, container, false)
        //
        txtcolor = raiz.findViewById(R.id.txtcolor)
        chbestado = raiz.findViewById(R.id.chkStateColor)
        lblidcolor = raiz.findViewById(R.id.lblidcolor)
        btnregistrar_color = raiz.findViewById(R.id.btnregistrar_color)
        btnactualizar_color = raiz.findViewById(R.id.btnactualizar_color)
        btneliminar_color = raiz.findViewById(R.id.btneliminar_color)
        lstcolor = raiz.findViewById(R.id.lstcolor)
        lstcolor.isNestedScrollingEnabled = true

        //
        registrocolor = ArrayList()

        //
        colorservice =ApiUtil.colorservice

        //
        mostrarColor(raiz.context)

        //
        btnregistrar_color.setOnClickListener {
            if(txtcolor.getText().toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese el color")
                txtcolor.requestFocus()
            }else{
                //capturando valores
                nom = txtcolor.getText().toString()
                est = if(chbestado.isChecked){
                    1
                }else{
                    0
                }
                //enviamos los valores a la clase
                objcolor.name_col = nom
                objcolor.state = est
                registrarColor(raiz.context,objcolor)
                DialogoCRUD("Registro de Color","Se registró el nuevo Color correctamente"
                ,colorfragment())
            }
        }

        btnactualizar_color.setOnClickListener {
            if (fila >= 0) {
                cod = lblidcolor.getText().toString().toInt()
                nom = txtcolor.getText().toString()
                est = if (chbestado.isChecked) {
                    1
                } else {
                    0
                }
                objcolor.idcolor = cod
                objcolor.name_col = nom
                objcolor.state = est
                actualizarColor(raiz.context, objcolor, cod.toLong())
                //objutilidad.limpiar(raiz.findViewById<View>(R.id.frmCategoria) as ViewGroup)
                val fcolor = colorfragment()
                DialogoCRUD("Actualizacion del Color", "Se actualizo el color", fcolor)
            } else {
                objutilidad.MensajeToast(raiz.context, "Seleccione un elemento de la lista")
                lstcolor.requestFocus()
            }
        }

        btneliminar_color.setOnClickListener {
            /*if (fila >= 0) {
                cod = lblidcolor.getText().toString().toInt()
                objcolor.idcolor = cod
                eliminarColor(raiz.context, cod.toLong())
                //objutilidad.limpiar(raiz.findViewById<View>(R.id.frmCategoria) as ViewGroup)
                val fcolor = colorfragment()
                DialogoCRUD("Eliminacion de Categoria", "Se elimino la categoria", fcolor)
            } else {
                objutilidad.MensajeToast(raiz.context, "Seleccione un elemento de la lista")
                lstcolor.requestFocus()
            }*/
            val document = Document(PageSize.A4,50f,50f,50f,50f)
            val fileName = "mi_lista_colores.pdf"
            val filePath = requireContext().getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS)?.get(0)?.absolutePath + "/" + fileName
            val writer = PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            writer.open()
            val boldFont = Font(Font.FontFamily.TIMES_ROMAN, 22f, Font.BOLD, BaseColor.DARK_GRAY)
            val titleChunk = Chunk("Lista de Colores", boldFont)
            val titleParagraph = Paragraph(titleChunk)
            titleParagraph.alignment = Element.ALIGN_CENTER
            document.add(titleParagraph)
            document.add(Paragraph(""))
            registrocolor=reporte()
            var tabla = PdfPTable(3)
            tabla.addCell("ID")
            tabla.addCell("Color")
            tabla.addCell("Estado")
            for (item in registrocolor!!) {
                var act=""
                if (item.state==1){
                    act="Habilitado"
                }else{
                    act="Deshabilitado"
                }
                tabla.addCell(item.idcolor.toString())
                tabla.addCell(item.name_col.toString())
                tabla.addCell(act.toString())
            }
            document.add(tabla)
            document.close()
            writer.close()
            val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", File(filePath))
            val openPdfIntent = Intent(Intent.ACTION_VIEW)
            openPdfIntent.setDataAndType(uri, "application/pdf")
            openPdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val intentChooser = Intent.createChooser(openPdfIntent, "Abrir archivo PDF")
            if (openPdfIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intentChooser)
            }


        }

        lstcolor.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                //asignamos los valores a cada control
                lblidcolor.setText(""+(registrocolor as ArrayList<Color>).get(fila).idcolor)
                txtcolor.setText(""+(registrocolor as ArrayList<Color>).get(fila).name_col)
                if((registrocolor as ArrayList<Color>).get(fila).state != 0){
                    chbestado.setChecked(true)
                }else{
                    chbestado.setChecked(false)
                }

            }
        )
        return raiz
    }

    fun reporte():List<Color>{
        val call = colorservice!!.MostrarColor()
        call!!.enqueue(object :Callback<List<Color>?>{
            override fun onResponse(
                call: Call<List<Color>?>,
                response: Response<List<Color>?>)
            {
                if(response.isSuccessful){
                    registrocolor = response.body()
                }
            }
            override fun onFailure(call: Call<List<Color>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }
        })
        return registrocolor!!
    }

    fun mostrarColor(contex:Context){
        val call = colorservice!!.MostrarColor()
        call!!.enqueue(object :Callback<List<Color>?>{
            override fun onResponse(
                call: Call<List<Color>?>,
                response: Response<List<Color>?>)
            {
                if(response.isSuccessful){
                    registrocolor = response.body()
                    lstcolor.adapter = AdaptadorColor(contex,registrocolor)
                }
            }
            override fun onFailure(call: Call<List<Color>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }
        })
    }

    fun registrarColor(context:Context,c: Color){
        val call = colorservice!!.RegistrarColor(c)
        call!!.enqueue(object:Callback<Color?>{
            override fun onResponse(call: Call<Color?>, response: Response<Color?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context,"Se registro el Color")
                }
            }

            override fun onFailure(call: Call<Color?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }

    fun actualizarColor(context:Context,co: Color,id:Long){
        val call = colorservice!!.ActualizarColor(id,co)
        call!!.enqueue(object:Callback<List<Color>?>{
            override fun onResponse(call: Call<List<Color>?>, response: Response<List<Color>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje", "Se actualizo correctamente")
                }
            }

            override fun onFailure(call: Call<List<Color>?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }

    fun eliminarColor(context:Context,id:Long){
        val call = colorservice!!.EliminarColor(id)
        call!!.enqueue(object:Callback<List<Color>?>{
            override fun onResponse(call: Call<List<Color>?>, response: Response<List<Color>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Color>?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}