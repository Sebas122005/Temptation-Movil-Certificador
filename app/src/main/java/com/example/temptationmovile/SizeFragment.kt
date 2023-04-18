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
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.adaptadores.AdaptadorRol
import com.example.temptationmovile.adaptadores.AdaptadorSize
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.clases.Size
import com.example.temptationmovile.databinding.FragmentRolBinding
import com.example.temptationmovile.databinding.FragmentSizeBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.RolService
import com.example.temptationmovile.servicios.SizeService
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
 * Use the [SizeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SizeFragment : Fragment() {


    private lateinit var txtSiz: EditText
    private lateinit var chbEst: CheckBox
    private lateinit var lblCodSiz: TextView
    private lateinit var btnRegistra_siz: Button
    private lateinit var btnActualizar_siz: Button
    private lateinit var btnEliminar_siz: Button
    private lateinit var lstSize: ListView

    val objSiz= Size()
    private var idsize = 0
    private var namesize=""
    private var state=1
    private var fila=-1

    private var sizeService: SizeService?=null
    private var registroSize:List<Size>?=null
    var objutil = Util()

    var ft: FragmentTransaction?=null
    private var dialogo: AlertDialog.Builder? = null

    private var _binding: FragmentSizeBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz=inflater.inflate(R.layout.fragment_size,container,false)
        //creamos los controles
        txtSiz=raiz.findViewById(R.id.txtSize)
        chbEst=raiz.findViewById(R.id.chbestado_size)
        lblCodSiz=raiz.findViewById(R.id.lblidsize)
        btnRegistra_siz=raiz.findViewById(R.id.btnregistrar_size)
        btnActualizar_siz=raiz.findViewById(R.id.btnactualizar_size)
        btnEliminar_siz=raiz.findViewById(R.id.btneliminar_size)
        lstSize=raiz.findViewById(R.id.lstsize)
        lstSize.isNestedScrollingEnabled = true

        registroSize = ArrayList()

        sizeService = ApiUtil.sizeService


        mostrarSize(raiz.context)


        btnRegistra_siz.setOnClickListener {
            if (txtSiz.getText().toString() == "") {
                objutil.MensajeToast(raiz.context, "Ingrese el Nombre")
                txtSiz.requestFocus()
            } else {
                namesize = txtSiz.getText().toString()
                state = if (chbEst.isChecked) 1 else 0
                //envienadoo los valores
                objSiz.name_size= namesize
                objSiz.state = state
                Log.e(objSiz.name_size, (objSiz.state).toString())
                registrar(raiz.context, objSiz)
                Log.e(objSiz.name_size, (objSiz.state).toString())

                //actualizamos el rol
                val fsiz = SizeFragment()
                DialogoCRUD("Registro de Talla","Se registro la talla",fsiz)
            }
        }
        btnActualizar_siz.setOnClickListener {
            if(fila>=0){
                idsize =  lblCodSiz.getText().toString().toInt()
                namesize =  txtSiz.getText().toString()
                state =  if (chbEst.isChecked) 1 else 0
                objSiz.idsize = idsize
                objSiz.name_size = namesize
                objSiz.state = state
                AnctualizarSize(raiz.context, objSiz, idsize.toLong())
                val fra = SizeFragment()
                DialogoCRUD("Actualización de talla", "Se actualizo la talla",fra)
            }else{
                lstSize.requestFocus()
            }
        }

        btnEliminar_siz.setOnClickListener {
            /*if(fila>=0){
                idsize = lblCodSiz.getText().toString().toInt()
                objSiz.idsize = idsize
                EliminarSize(raiz.context,idsize.toLong())
                val fra =  SizeFragment()
                DialogoCRUD("Eliminar la talla", "Se elimino la talla",fra)
            }else{
                lstSize.requestFocus()
            }*/
            val document = Document(PageSize.A4,50f,50f,50f,50f)
            val fileName = "mi_lista_tallas.pdf"
            val filePath = requireContext().getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS)?.get(0)?.absolutePath + "/" + fileName
            val writer = PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            writer.open()
            val boldFont = Font(Font.FontFamily.TIMES_ROMAN, 22f, Font.BOLD, BaseColor.DARK_GRAY)
            val titleChunk = Chunk("Lista de Tallas", boldFont)
            val titleParagraph = Paragraph(titleChunk)
            titleParagraph.alignment = Element.ALIGN_CENTER
            document.add(titleParagraph)
            document.add(Paragraph(""))
            registroSize=reporte()
            var tabla = PdfPTable(3)
            tabla.addCell("ID")
            tabla.addCell("Talla")
            tabla.addCell("Estado")
            for (item in registroSize!!) {
                var act=""
                if (item.state==1){
                    act="Habilitado"
                }else{
                    act="Deshabilitado"
                }
                tabla.addCell(item.idsize.toString())
                tabla.addCell(item.name_size.toString())
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

        lstSize.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                //asignamos los valores a cada control
                lblCodSiz.setText(""+(registroSize as ArrayList<Size>).get(fila).idsize)
                txtSiz.setText(""+(registroSize as ArrayList<Size>).get(fila).name_size)
                if((registroSize as ArrayList<Size>).get(fila).state != 0){
                    chbEst.setChecked(true)
                }else{
                    chbEst.setChecked(false)
                }

            }
        )
        return raiz
    }

    fun reporte():List<Size>{
        val call = sizeService!!.Mostrarsizes()
        call.enqueue(object: Callback<List<Size>?> {
            override fun onResponse(call: Call<List<Size>?>, response: Response<List<Size>?>) {
                if (response.isSuccessful){
                    registroSize=response.body()
                }
            }
            override fun onFailure(call: Call<List<Size>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
        return registroSize!!
    }

    fun mostrarSize(context: Context){
        val call = sizeService!!.Mostrarsizes()
        call.enqueue(object: Callback<List<Size>?> {
            override fun onResponse(call: Call<List<Size>?>, response: Response<List<Size>?>) {
                if (response.isSuccessful){
                    registroSize=response.body()
                    lstSize.adapter= AdaptadorSize(context,registroSize)
                }
            }
            override fun onFailure(call: Call<List<Size>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }
    fun registrar(context: Context, s: Size){
        val call = sizeService!!.Registrarsize(s)
        call!!.enqueue(object : Callback<Size?> {
            override fun onResponse(call: Call<Size?>, response: Response<Size?>) {
                if (response.isSuccessful){
                    objutil.MensajeToast(context,"Se registro la Talla")
                }
            }

            override fun onFailure(call: Call<Size?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }

    fun AnctualizarSize(context: Context, b: Size, id: Long ){
        val call = sizeService!!.Actualizarsize(id,b)
        call!!.enqueue(object : Callback<List<Size>?>{
            override fun onResponse(call: Call<List<Size>?>, response: Response<List<Size>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente")
                }
            }

            override fun onFailure(call: Call<List<Size>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }
    fun EliminarSize(context: Context, id: Long){
        val call = sizeService!!.Eliminarrsize(id)
        call!!.enqueue(object: Callback<List<Size>?>{
            override fun onResponse(call: Call<List<Size>?>, response: Response<List<Size>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Size>?>, t: Throwable) {
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