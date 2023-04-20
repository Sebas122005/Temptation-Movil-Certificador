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
import androidx.recyclerview.widget.LinearLayoutManager
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

class colorfragment : Fragment() {

    private var _binding:ColorfragmentBinding?=null
    private val binding get() = _binding!!
    private var colorservice :ColorService?=null
    private var registrocolor:MutableList<Color>?=null
    var objutilidad = Util()
    private var fila:Int?=null
    val objcolor = Color()
    private lateinit var adaptadorColor: AdaptadorColor
    private var llmanager:LinearLayoutManager?=null

    var ft: FragmentTransaction?= null
    private var dialogo: AlertDialog.Builder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ColorfragmentBinding.inflate(inflater, container, false)
        var context=binding.root.context
        llmanager= LinearLayoutManager(context)
        registrocolor = ArrayList()
        colorservice =ApiUtil.colorservice
        mostrarColor(context)

        binding.btnregistrarColor.setOnClickListener {
            if(binding.txtcolor.getText().toString()==""){
                objutilidad.MensajeToast(context,"Ingrese el color")
                binding.txtcolor.requestFocus()
            }else{
                //capturando valores
                var est = if(binding.chkStateColor.isChecked){
                    1
                }else{
                    0
                }
                //enviamos los valores a la clase
                objcolor.name_col = binding.txtcolor.text.toString()
                objcolor.state = est
                registrarColor(context,objcolor)
                DialogoCRUD("Registro de Color","Se registró el nuevo Color correctamente"
                ,colorfragment())
            }
        }

        binding.btnactualizarColor.setOnClickListener {
            if (fila !=null) {
                var est = if (binding.chkStateColor.isChecked) {
                    1
                } else {
                    0
                }
                objcolor.idcolor = binding.lblidcolor.getText().toString().toInt()
                objcolor.name_col = binding.txtcolor.text.toString()
                objcolor.state = est
                actualizarColor(context, objcolor, objcolor.idcolor.toLong())
                val fcolor = colorfragment()
                DialogoCRUD("Actualizacion del Color", "Se actualizo el color", fcolor)
            } else {
                objutilidad.MensajeToast(context, "Seleccione un elemento de la lista")
                binding.lstcolor.requestFocus()
            }
        }

        binding.btneliminarColor.setOnClickListener {
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
            registrocolor=reporte()as MutableList<Color>
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
        return binding.root
    }
    fun onClickListener(col:Color,pos:Int){
        fila=pos
        binding.lblidcolor.text=col.idcolor.toString()
        binding.txtcolor.setText(col.name_col.toString())
        if (col.state==1) {
            binding.chkStateColor.setChecked(true)
        }else {
            binding.chkStateColor.setChecked(false)
        }
    }
    fun onClickDeleteChangued(pos:Int,col:Color){
        if (col.state==1) {
            eliminarColor(binding.root.context, col.idcolor.toLong())
            DialogoCRUD("Marca Deshabilitado","Se deshabilito el estado de la Marca "+col.name_col,colorfragment())
            //llmanager!!.scrollToPositionWithOffset(pos,10)
        }else{
            col.state=1
            actualizarColor(binding.root.context,col,col.idcolor.toLong())
            DialogoCRUD("Marca Habilitado","Se habilitó el estado de la Marca"+col.name_col,colorfragment())
            //llmanager!!.scrollToPositionWithOffset(pos,10)
        }
    }

    fun reporte():List<Color>{
        val call = colorservice!!.MostrarColor()
        call!!.enqueue(object :Callback<List<Color>?>{
            override fun onResponse(
                call: Call<List<Color>?>,
                response: Response<List<Color>?>)
            {
                if(response.isSuccessful){
                    registrocolor = response.body() as MutableList<Color>
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
                    registrocolor = response.body() as MutableList<Color>
                    adaptadorColor = AdaptadorColor(lista=registrocolor!!,
                        onClickListener={b,pos->onClickListener(b,pos)},
                        onClickDeleteChangued={del,bra->onClickDeleteChangued(del,bra)})
                    binding.lstcolor.layoutManager=llmanager
                    binding.lstcolor.adapter=adaptadorColor
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
        call!!.enqueue(object:Callback<Color?>{
            override fun onResponse(call: Call<Color?>, response: Response<Color?>) {
                if(response!!.isSuccessful){
                    Log.e("mensaje", "Se actualizo correctamente")
                    fila=null
                }
            }
            override fun onFailure(call: Call<Color?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }
        })
    }

    fun eliminarColor(context:Context,id:Long){
        val call = colorservice!!.EliminarColor(id)
        call!!.enqueue(object:Callback<Color?>{
            override fun onResponse(call: Call<Color?>, response: Response<Color?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<Color?>, t: Throwable) {
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