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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.temptationmovile.adaptadores.AdaptadorCategory
import com.example.temptationmovile.clases.Category
import com.example.temptationmovile.databinding.CategoryFragmentBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.CategoryService
import com.example.temptationmovile.utilidad.Util
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class CategoryFragment : Fragment() {

    private var _binding:CategoryFragmentBinding?=null
    private val binding get() = _binding!!
    private var categoryService: CategoryService?=null
    private var registroCategory:MutableList<Category>?=null
    var objutilidad = Util()
    private var fila:Int?=null
    val objcategory = Category()
    private lateinit var adapterCategory:AdaptadorCategory
    private var llmanager:LinearLayoutManager?=null
    var ft: FragmentTransaction?= null
    private var dialogo: AlertDialog.Builder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = CategoryFragmentBinding.inflate(inflater, container, false)
        //
        var context=binding.root.context
        llmanager= LinearLayoutManager(context)
        registroCategory = ArrayList()
        categoryService = ApiUtil.categoryService

        mostrarCategory(context)

        binding.btnregistrarCat.setOnClickListener {
            if(binding.txtcat.getText().toString()==""){
                objutilidad.MensajeToast(context,"Ingrese la categoria")
                binding.txtcat.requestFocus()
            }else{
                var est = if(binding.chkStateCategory.isChecked){
                    1
                }else{
                    0
                }
                objcategory.name_cat = binding.txtcat.getText().toString()
                objcategory.state = est
                registrarCategory(context,objcategory)
                //
                DialogoCRUD("Registro de Categoria","Se registró la nueva Categoria correctamente",
                CategoryFragment())
            }
        }

        binding.btnactualizarCat.setOnClickListener {
            if (fila != null) {
                var est = if (binding.chkStateCategory.isChecked) {
                    1
                } else {
                    0
                }
                objcategory.idcat = binding.lblidcat.getText().toString().toInt()
                objcategory.name_cat = binding.txtcat.text.toString()
                objcategory.state = est
                actualizarCategory(context, objcategory, objcategory.idcat.toLong())
                //objutilidad.limpiar(raiz.findViewById<View>(R.id.frmCategoria) as ViewGroup)
                val fcategoria = CategoryFragment()
                DialogoCRUD("Actualizacion de Categoria", "Se actualizo la categoria", fcategoria)
            } else {
                objutilidad.MensajeToast(context, "Seleccione un elemento de la lista")
                binding.lstcat.requestFocus()
            }
        }

        binding.btneliminarCat.setOnClickListener {
            val document = Document(PageSize.A4,50f,50f,50f,50f)
            val fileName = "mi_lista_categoria.pdf"
            val filePath = requireContext().getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS)?.get(0)?.absolutePath + "/" + fileName
            val writer = PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            writer.open()
            val boldFont = Font(Font.FontFamily.TIMES_ROMAN, 22f, Font.BOLD, BaseColor.DARK_GRAY)
            val titleChunk = Chunk("Lista de Categorias", boldFont)
            val titleParagraph = Paragraph(titleChunk)
            titleParagraph.alignment = Element.ALIGN_CENTER
            document.add(titleParagraph)
            document.add(Paragraph(""))
            registroCategory=reporte() as MutableList<Category>?
            var tabla = PdfPTable(3)
            tabla.addCell("ID")
            tabla.addCell("Nombre de Categoria")
            tabla.addCell("Estado")
            for (item in registroCategory!!) {
                var act=""
                if (item.state==1){
                    act="Habilitado"
                }else{
                    act="Deshabilitado"
                }
                tabla.addCell(item.idcat.toString())
                tabla.addCell(item.name_cat.toString())
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

    fun onClickListener(cat:Category,pos:Int){
        fila=pos
        binding.lblidcat.text=cat.idcat.toString()
        binding.txtcat.setText(cat.name_cat.toString())
        if (cat.state==1){
            binding.chkStateCategory.setChecked(true)
        }else{
            binding.chkStateCategory.setChecked(false)
        }

    }
    fun onClickDeleteChangued(pos:Int,cat:Category){
        if (cat.state==1){
            eliminarCategory(binding.root.context,cat.idcat.toLong())
            DialogoCRUD("Categoria Deshabilitado","Se deshabilito el estado de la categoria "+cat.name_cat,CategoryFragment())
        }else{
            cat.state=1
            actualizarCategory(binding.root.context,cat,cat.idcat.toLong())
            DialogoCRUD("Categoria Habilitado","Se habilitó el estado de la categoria "+cat.name_cat,CategoryFragment())
        }
    }


    fun reporte():List<Category>{
        val call = categoryService!!.MostrarCategory()
        call!!.enqueue(object : Callback<List<Category>?>{
            override fun onResponse(
                call: Call<List<Category>?>,
                response: Response<List<Category>?>
            ) {
                if(response.isSuccessful){
                    registroCategory = response.body() as MutableList<Category>?
                }
            }
            override fun onFailure(call: Call<List<Category>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
        return registroCategory!!
    }


    fun mostrarCategory(contex:Context){
        val call = categoryService!!.MostrarCategory()
        call!!.enqueue(object : Callback<List<Category>?>{
            override fun onResponse(
                call: Call<List<Category>?>,
                response: Response<List<Category>?>
            ) {
                if(response.isSuccessful){
                    registroCategory = response.body() as MutableList<Category>?
                    adapterCategory = AdaptadorCategory(lista=registroCategory!!,
                        onClickListener={b,pos->onClickListener(b,pos)},
                        onClickDeleteChangued={del,bra->onClickDeleteChangued(del,bra)})
                    binding.lstcat.layoutManager=llmanager
                    binding.lstcat.adapter=adapterCategory
                }
            }

            override fun onFailure(call: Call<List<Category>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })

    }

    fun registrarCategory(context: Context,ca:Category){
        val call = categoryService!!.RegistrarCategory(ca)
        call!!.enqueue(object :Callback<Category?>{
            override fun onResponse(call: Call<Category?>, response: Response<Category?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context,"Se registro la categoria")
                }
            }

            override fun onFailure(call: Call<Category?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }

    fun actualizarCategory(context: Context,ca:Category,id:Long){
        val call = categoryService!!.ActualizarCategory(id,ca)
        call!!.enqueue(object :Callback<Category>{
            override fun onResponse(
                call: Call<Category>?,
                response: Response<Category>?
            ) {
                if(response!!.isSuccessful){
                    Log.e("mensaje", "Se actualizo correctamente")
                    fila=null
                }
            }

            override fun onFailure(call: Call<Category>?, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }
        })
    }
    fun eliminarCategory(context: Context,id:Long){
        val call = categoryService!!.EliminarCategory(id)
        call!!.enqueue(object :Callback<Category>{
            override fun onResponse(
                call: Call<Category>?,
                response: Response<Category>?
            ) {
                if(response!!.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }
            override fun onFailure(call: Call<Category>?, t: Throwable) {
                Log.e("Error: ",t.message!!)
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