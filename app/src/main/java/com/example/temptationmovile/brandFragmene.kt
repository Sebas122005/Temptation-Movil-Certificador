package com.example.temptationmovile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.temptationmovile.clases.Brand
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.adaptadores.Adaptadorbrand
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.BrandService
import com.example.temptationmovile.utilidad.Util
import com.itextpdf.text.pdf.PdfWriter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileOutputStream
import androidx.core.content.ContextCompat.getExternalFilesDirs
import android.os.Environment
import android.net.Uri
import java.io.File
import android.content.pm.PackageManager
import android.graphics.fonts.FontFamily
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.example.temptationmovile.databinding.BrandFragmentBinding

class brandFragmene : Fragment() {

    private var _binding: BrandFragmentBinding?=null
    private val binding get()=_binding!!
    var brandservice: BrandService ? = null
    private var registrobrand: MutableList<Brand>?=null
    var objutilidad =  Util()
    private var fila:Int?=null
    val objbrand=Brand()
    private lateinit var adapterBrand:Adaptadorbrand
    private var llmanager:LinearLayoutManager?=null

    //creamos transicion para fragmento
    var ft: FragmentTransaction?= null
    private var dialogo: AlertDialog.Builder? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= BrandFragmentBinding.inflate(inflater,container,false)
        var context=binding.root.context
        llmanager= LinearLayoutManager(context)
        registrobrand = ArrayList()
        brandservice = ApiUtil.brandservice
        mostrarBrand(context)



        binding.btnregistrar.setOnClickListener {
            if (binding.txtnombreBrand.getText().toString() == "") {
                objutilidad.MensajeToast(context, "Ingrese el Nombre")
                binding.txtnombreBrand.requestFocus()
            } else {
                //envienadoo los valores
                objbrand.name_brand = binding.txtnombreBrand.text.toString()
                objbrand.state = if (binding.chkStatebrand.isChecked) 1 else 0
                Log.e(objbrand.name_brand, (objbrand.state).toString())
                registrarbrand(context, objbrand)
                DialogoCRUD("Registro de Marca",
                    "Se registró la marca correctamente",
                    brandFragmene()
                )
                llmanager!!.scrollToPositionWithOffset(registrobrand!!.size,10)
            }
        }

        binding.btnactualizar.setOnClickListener {
            if(fila!=null){
                objbrand.idbrand = binding.lblidbrand.text.toString().toInt()
                objbrand.name_brand = binding.txtnombreBrand.text.toString()
                objbrand.state =  if (binding.chkStatebrand.isChecked) 1 else 0
                ActualizarBrand(context, objbrand, objbrand.idbrand.toLong())
                val fbramd = brandFragmene()
                DialogoCRUD("Actualizacion de la Marca", "Se actualizó la Marca",fbramd)
                llmanager!!.scrollToPositionWithOffset(fila!!.toInt(),10)
            }else{
                binding.lstbrand.requestFocus()
            }
        }

        binding.btnGenerarReporteMarca.setOnClickListener {
            val document = Document(PageSize.A4,50f,50f,50f,50f)
            val fileName = "mi_lista_marca.pdf"
            val filePath = requireContext().getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS)?.get(0)?.absolutePath + "/" + fileName
            val writer = PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            writer.open()
            val boldFont = Font(Font.FontFamily.TIMES_ROMAN, 22f, Font.BOLD,BaseColor.DARK_GRAY)
            val titleChunk = Chunk("Lista de Marcas", boldFont)
            val titleParagraph = Paragraph(titleChunk)
            titleParagraph.alignment = Element.ALIGN_CENTER
            document.add(titleParagraph)
            document.add(Paragraph(""))
            registrobrand= reporte() as MutableList<Brand>?
            var tabla = PdfPTable(3)
            tabla.addCell("ID")
            tabla.addCell("Nombre de Marca")
            tabla.addCell("Estado")
            for (item in (registrobrand as List<Brand>)) {
                var act=""
                if (item.state==1){
                    act="Habilitado"
                }else{
                    act="Deshabilitado"
                }
                tabla.addCell(item.idbrand.toString())
                tabla.addCell(item.name_brand.toString())
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
    fun reporte(): List<Brand>? {
        val call = brandservice!!.MostrarBrand()
        call!!.enqueue(object : Callback<List<Brand>?> {
            override fun onResponse(
                call: Call<List<Brand>?>,
                response: Response<List<Brand>?>
            ) {
                if(response.isSuccessful){
                    registrobrand = response.body() as MutableList<Brand>?
                }
            }
            override fun onFailure(call: Call<List<Brand>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
        return registrobrand!!
    }

    //CREAMOS LA FUNCION PARA MOSTRAR LAS CATEGORIAS
    fun mostrarBrand(context: Context){
        val call = brandservice!!.MostrarBrand()
        call!!.enqueue(object : Callback<List<Brand>?> {
            override fun onResponse(
                call: Call<List<Brand>?>,
                response: Response<List<Brand>?>
            ) {
                if(response.isSuccessful){
                    registrobrand = response.body() as MutableList<Brand>?
                    adapterBrand= Adaptadorbrand(listbrand=registrobrand!!,
                        onClickListener={b,pos->onClickListener(b,pos)},
                        onClickDeleteChangued={del,bra->onClickDeleteChangued(del,bra)})
                    binding.lstbrand.layoutManager=llmanager
                    binding.lstbrand.adapter=adapterBrand
                }
            }
            override fun onFailure(call: Call<List<Brand>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }
        })
    }
    fun onClickListener(brand:Brand,pos: Int){
        fila=pos
        binding.lblidbrand.text=brand.idbrand.toString()
        binding.txtnombreBrand.setText(brand.name_brand.toString())
        if (brand.state==1) {
            binding.chkStatebrand.setChecked(true)
        }else {
            binding.chkStatebrand.setChecked(false)
        }
    }
    private fun onClickDeleteChangued(pos:Int,brand:Brand){
        println(brand.name_brand.toString()+"< >"+pos )
        if (brand.state==1) {
            EliminarBrand(binding.root.context, brand.idbrand.toLong())
            DialogoCRUD("Marca Deshabilitado","Se deshabilito el estado de la Marca "+brand.name_brand,brandFragmene())
            //llmanager!!.scrollToPositionWithOffset(pos,10)
        }else{
            brand.state=1
            ActualizarBrand(binding.root.context,brand,brand.idbrand.toLong())
            DialogoCRUD("Marca Habilitado","Se habilitó el estado de la Marca"+brand.name_brand,brandFragmene())
            //llmanager!!.scrollToPositionWithOffset(pos,10)
        }
    }
    fun registrarbrand(context: Context, c:Brand){
        val call = brandservice!!.RegistrarBrand(c)
        call!!.enqueue(object :Callback<Brand?>{
            override fun onResponse(call: Call<Brand?>, response: Response<Brand?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro la marca")
                    adapterBrand.notifyItemInserted(registrobrand!!.size)
                }
            }
            override fun onFailure(call: Call<Brand?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }
        })
    }

    fun ActualizarBrand(context: Context, b:Brand, id: Long){
        val call = brandservice!!.ActualizarBrand(id,b)
        call!!.enqueue(object : Callback<Brand?>{
            override fun onResponse(call: Call<Brand?>, response: Response<Brand?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente")
                }
            }
            override fun onFailure(call: Call<Brand?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }
        })
    }
    fun EliminarBrand(context: Context, id: Long){
        val call = brandservice!!.EliminarBrand(id)
        call!!.enqueue(object: Callback<Brand?>{
            override fun onResponse(call: Call<Brand?>, response: Response<Brand?>){
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }
            override fun onFailure(call: Call<Brand?>, t: Throwable) {
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}