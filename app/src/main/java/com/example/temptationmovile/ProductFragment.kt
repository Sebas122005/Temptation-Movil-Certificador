package com.example.temptationmovile

import android.annotation.SuppressLint
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
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.temptationmovile.adaptadores.*
import com.example.temptationmovile.clases.*
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.*
import com.example.temptationmovile.utilidad.Util
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.example.temptationmovile.databinding.FragmentProductBinding
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
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding:FragmentProductBinding?=null
    private val binding get() = _binding!!
    private lateinit var adaptadorProduct: AdaptadorProduct
    private var llmanager:LinearLayoutManager?=null
    private val objproducto = Product()
    private var fila:Int?= null
    private var brandService: BrandService? = null
    private var colorService: ColorService? = null
    private var styleService: StyleService? = null
    private var sizeService: SizeService? = null
    private var categoryService: CategoryService? = null
    private var productService: ProductService? = null

    private var registroProducto: MutableList<Product>? = null
    private var registrocolor: List<Color>? = null
    private var registrostyle: List<Style>? = null
    private var registrosize: List<Size>? = null
    private var registrocategory: List<Category>? = null
    private var registroBrand: List<Brand>? = null

    var objutilidad =  Util()
    private var dialogo: AlertDialog.Builder? = null
    var ft: FragmentTransaction?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        var context=binding.root.context
        llmanager= LinearLayoutManager(context)
        binding.lstPro.isNestedScrollingEnabled = true

        registroBrand = ArrayList()
        registroProducto = ArrayList()
        registrocolor = ArrayList()
        registrosize = ArrayList()
        registrostyle = ArrayList()
        registrocategory = ArrayList()

        brandService = ApiUtil.brandservice
        productService = ApiUtil.productService
        categoryService = ApiUtil.categoryService
        sizeService = ApiUtil.sizeService
        styleService = ApiUtil.styleService
        colorService = ApiUtil.colorservice

        mostrarComboBrand(context)
        mostrarproduct(context)
        mostrarComboStyle(context)
        mostrarComboColor(context)
        mostrarComboCategory(context)
        mostrarcomboSize(context)


        binding.btnregistrarproduct.setOnClickListener {
            if(binding.txtnombreProd.text.toString() ==""){
                objutilidad.MensajeToast(context,"Ingresa el Nombre")
                binding.txtnombreProd.requestFocus()
            }else if(binding.txtdescripcionProd.text.toString() == ""){
                objutilidad.MensajeToast(context,"Ingresa la descripcion")
                binding.txtdescripcionProd.requestFocus()
            }else if(binding.txtpriceProd.text.toString()==""){
                objutilidad.MensajeToast(context,"Ingrese un precio")
                binding.txtpriceProd.requestFocus()
            }else if(binding.txtstockProd.text.toString()==""){
                objutilidad.MensajeToast(context,"Ingrese el stock")
                binding.txtstockProd.requestFocus()
            }else if(binding.cboBrand.selectedItemPosition==-1){
                objutilidad.MensajeToast(context,"Seleccionne una Marca")
                binding.cboBrand.requestFocus()
            }else{
                objproducto.idcat = (registrocategory as ArrayList<Category>).get(binding.cboCategory.selectedItemPosition).idcat
                objproducto.idsize = (registrosize as ArrayList<Size>).get(binding.cboSize.selectedItemPosition).idsize
                objproducto.idstyles = (registrostyle as ArrayList<Style>).get(binding.cboStyle.selectedItemPosition).idstyles
                objproducto.idbrand = (registroBrand as ArrayList<Brand>).get(binding.cboBrand.selectedItemPosition).idbrand
                objproducto.idcolor = (registrocolor as ArrayList<Color>).get(binding.cbocolor.selectedItemPosition).idcolor
                objproducto.name_p = binding.txtnombreProd.text.toString()
                objproducto.description = binding.txtdescripcionProd.text.toString()
                objproducto.price = binding.txtpriceProd.text.toString().toDouble()
                objproducto.stock = binding.txtstockProd.text.toString().toInt()
                objproducto.image_front = ""
                objproducto.image_back = ""
                objproducto.image_using = ""
                objproducto.state = if (binding.chkStateProd.isChecked) 1 else 0

                registrarProducto(context,objproducto)
                val fproducto = ProductFragment()
                DialogoCRUD("Registro de Producto", "Se registro el Producto Correctamente",fproducto)
            }
        }

        binding.btnactualizarproduct.setOnClickListener {
            if(fila !=null){
                objproducto.idcat = (registrocategory as ArrayList<Category>).get(binding.cboCategory.selectedItemPosition).idcat
                objproducto.idsize = (registrosize as ArrayList<Size>).get(binding.cboSize.selectedItemPosition).idsize
                objproducto.idstyles = (registrostyle as ArrayList<Style>).get(binding.cboStyle.selectedItemPosition).idstyles
                objproducto.idbrand = (registroBrand as ArrayList<Brand>).get(binding.cboBrand.selectedItemPosition).idbrand
                objproducto.idcolor = (registrocolor as ArrayList<Color>).get(binding.cbocolor.selectedItemPosition).idcolor
                objproducto.name_p = binding.txtnombreProd.text.toString()
                objproducto.description = binding.txtdescripcionProd.text.toString()
                objproducto.price = binding.txtpriceProd.text.toString().toDouble()
                objproducto.stock = binding.txtstockProd.text.toString().toInt()
                objproducto.image_front = ""
                objproducto.image_back = ""
                objproducto.image_using = ""
                objproducto.state = if (binding.chkStateProd.isChecked) 1 else 0
                objproducto.idproduc=binding.codProduct.text.toString().toInt()
                ActualizarProduct(context,objproducto,objproducto.idproduc.toLong())
                val fproducto = ProductFragment()
                DialogoCRUD("Actualizacion de Producto", "Se Actualizo el Producto Correctamente",fproducto)
            }else{
                objutilidad.MensajeToast(context,"Seleccione un elemento de la lista")
                binding.lstPro.requestFocus()
            }
        }
        binding.btnGenerarReporteproduct.setOnClickListener {
            val document = Document(PageSize.A4,50f,50f,50f,50f)
            val fileName = "mi_lista_productos.pdf"
            val filePath = requireContext().getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS)?.get(0)?.absolutePath + "/" + fileName
            val writer = PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            writer.open()
            val boldFont = Font(Font.FontFamily.TIMES_ROMAN, 22f, Font.BOLD, BaseColor.DARK_GRAY)
            val titleChunk = Chunk("Lista de Productos", boldFont)
            val titleParagraph = Paragraph(titleChunk)
            titleParagraph.alignment = Element.ALIGN_CENTER
            document.add(titleParagraph)
            document.add(Paragraph(""))
            registroProducto=reporte() as MutableList<Product>
            var tabla = PdfPTable(11)
            tabla.addCell("ID")
            tabla.addCell("Categoria")
            tabla.addCell("Talla")
            tabla.addCell("Estilo")
            tabla.addCell("Marca")
            tabla.addCell("Color")
            tabla.addCell("Nombre Producto")
            tabla.addCell("Descripcion")
            tabla.addCell("Precio")
            tabla.addCell("Stock")
            tabla.addCell("Estado")
            for (item in registroProducto!!) {
                var act=""
                var nombre=""
                tabla.addCell(item.idproduc.toString())
                tabla.addCell(item.idcat.toString())
                tabla.addCell(item.idsize.toString())
                tabla.addCell(item.idstyles.toString())
                tabla.addCell(item.idbrand.toString())
                tabla.addCell(item.idcolor.toString())
                tabla.addCell(item.name_p)
                tabla.addCell(item.description)
                tabla.addCell(item.price.toString())
                tabla.addCell(item.stock.toString())
                if (item.state==1){
                    act="Habilitado"
                }else{
                    act="Deshabilitado"
                }
                tabla.addCell(act)
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

        binding.btnReporteproductMinStock.setOnClickListener {
            if (binding.idStockMinimo.text.toString().length<1){
                Toast.makeText(context,"Debe ingresar un valor como stock mínimo",Toast.LENGTH_LONG).show()
            }else {
                val document = Document(PageSize.A4, 50f, 50f, 50f, 50f)
                val fileName = "mi_lista_productos_minimo_stock.pdf"
                val filePath =
                    requireContext().getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS)
                        ?.get(0)?.absolutePath + "/" + fileName
                val writer = PdfWriter.getInstance(document, FileOutputStream(filePath))
                document.open()
                writer.open()
                println("Abre documento")
                val boldFont =
                    Font(Font.FontFamily.TIMES_ROMAN, 22f, Font.BOLD, BaseColor.DARK_GRAY)
                val titleChunk = Chunk("Lista de Productos con Minimo Stock", boldFont)
                val titleParagraph = Paragraph(titleChunk)
                titleParagraph.alignment = Element.ALIGN_CENTER
                document.add(titleParagraph)
                document.add(Paragraph(""))
                registroProducto = reporte() as MutableList<Product>
                var tabla = PdfPTable(11)
                tabla.addCell("ID")
                tabla.addCell("Categoria")
                tabla.addCell("Talla")
                tabla.addCell("Estilo")
                tabla.addCell("Marca")
                tabla.addCell("Color")
                tabla.addCell("Nombre Producto")
                tabla.addCell("Descripcion")
                tabla.addCell("Precio")
                tabla.addCell("Stock")
                tabla.addCell("Estado")
                var cantReportMinStock = 0
                for (item in registroProducto!!){
                    if (item.stock <= binding.idStockMinimo.text.toString().toInt()) {
                        cantReportMinStock = cantReportMinStock + 1
                        var act = ""
                        var nombre = ""
                        tabla.addCell(item.idproduc.toString())
                        tabla.addCell(item.idcat.toString())
                        tabla.addCell(item.idsize.toString())
                        tabla.addCell(item.idstyles.toString())
                        tabla.addCell(item.idbrand.toString())
                        tabla.addCell(item.idcolor.toString())
                        tabla.addCell(item.name_p)
                        tabla.addCell(item.description)
                        tabla.addCell(item.price.toString())
                        tabla.addCell(item.stock.toString())
                        if (item.state == 1) {
                            act = "Habilitado"
                        } else {
                            act = "Deshabilitado"
                        }
                        tabla.addCell(act)
                    } else {
                        continue
                    }
                 }
                if (cantReportMinStock>0) {
                    document.add(tabla)
                    document.close()
                    writer.close()
                    val uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.provider",
                        File(filePath)
                    )
                    val openPdfIntent = Intent(Intent.ACTION_VIEW)
                    openPdfIntent.setDataAndType(uri, "application/pdf")
                    openPdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    val intentChooser = Intent.createChooser(openPdfIntent, "Abrir archivo PDF")
                    if (openPdfIntent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(intentChooser)
                    }
                }else{
                    Toast.makeText(context,"No se encontraron Productos con el valor stock minimo ingresado",Toast.LENGTH_LONG).show()
                }
            }
        }

        return  binding.root
    }

    fun reporte():List<Product>{
        val call = productService!!.MostrarProduct()
        call!!.enqueue(object :Callback<List<Product>>{
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroProducto = response.body() as MutableList<Product>
                }else{
                    println("Error")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
        return registroProducto!!
    }


    fun mostrarComboBrand(context: Context){
        val call = brandService!!.MostrarBrand()
        call!!.enqueue(object: Callback<List<Brand>?>{
            override fun onResponse(call: Call<List<Brand>?>, response: Response<List<Brand>?>) {
                if(response.isSuccessful){
                    registroBrand = response.body()
                    binding.cboBrand.adapter = AdaptadorComboBrand(context, registroBrand)
                }
            }
            override fun onFailure(call: Call<List<Brand>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun mostrarComboStyle(context: Context){
        val call = styleService!!.MostrarEstilo()
        call.enqueue(object : Callback<List<Style>?>{
            override fun onResponse(call: Call<List<Style>?>, response: Response<List<Style>?>) {
                if(response.isSuccessful){
                    registrostyle = response.body()
                    binding.cboStyle.adapter = AdaptadorComboEstilo(context,registrostyle)
                }
            }
            override fun onFailure(call: Call<List<Style>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }
    fun mostrarComboColor(context: Context){
        val call = colorService!!.MostrarColor()
        call.enqueue(object :Callback<List<Color>?>{
            override fun onResponse(call: Call<List<Color>?>, response: Response<List<Color>?>) {
                if(response.isSuccessful){
                    registrocolor = response.body()
                    binding.cbocolor.adapter = AdaptadorComboColor(context,registrocolor)
                }
            }
            override fun onFailure(call: Call<List<Color>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun mostrarComboCategory(context: Context){
        val call = categoryService!!.MostrarCategory()
        call.enqueue(object : Callback<List<Category>?>{
            override fun onResponse(
                call: Call<List<Category>?>,
                response: Response<List<Category>?>
            ) {
                if(response.isSuccessful){
                    registrocategory = response.body()
                    binding.cboCategory.adapter = AdaptadorComboCategory(context,registrocategory)
                }
            }
            override fun onFailure(call: Call<List<Category>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun mostrarcomboSize(context: Context){
        val call = sizeService!!.Mostrarsizes()
        call.enqueue(object : Callback<List<Size>?>{
            override fun onResponse(call: Call<List<Size>?>, response: Response<List<Size>?>) {
                if(response.isSuccessful){
                    registrosize = response.body()
                    binding.cboSize.adapter = AdaptadorComboSize(context,registrosize)
                }
            }
            override fun onFailure(call: Call<List<Size>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }
    fun onClickListener(pro:Product,pos:Int){
        fila = pos
        binding.codProduct.setText(pro.idproduc.toString())
        binding.txtnombreProd.setText(pro.name_p.toString())
        binding.txtdescripcionProd.setText(pro.description.toString())
        binding.txtpriceProd.setText(pro.price.toString())
        binding.txtstockProd.setText(pro.stock.toString())
        for (x in (registroBrand as ArrayList<Brand>).indices){
            if((registroBrand as ArrayList<Brand>).get(x).idbrand == pro.idbrand){
                binding.cboBrand.setSelection(x)
            }
        }

        for (x in (registrocolor as ArrayList<Color>).indices){
            if((registrocolor as ArrayList<Color>).get(x).idcolor == pro.idcolor){
                binding.cbocolor.setSelection(x)
            }
        }
        for (x in (registrocategory as ArrayList<Category>).indices){
            if((registrocategory as ArrayList<Category>).get(x).idcat == pro.idcat){
                binding.cboCategory.setSelection(x)
            }
        }
        for (x in (registrosize as ArrayList<Size>).indices){
            if((registrosize as ArrayList<Size>).get(x).idsize == pro.idsize){
                binding.cboSize.setSelection(x)
            }
        }
        for (x in (registrostyle as ArrayList<Style>).indices){
            if((registrostyle as ArrayList<Style>).get(x).idstyles == pro.idstyles){
                binding.cboStyle.setSelection(x)
            }
        }
        if(pro.state==1){
            binding.chkStateProd.setChecked(true)
        }else{
            binding.chkStateProd.setChecked(false)
        }
    }
    fun onClickDeleteChangued(pos:Int,pro: Product){
        if (pro.state==1){
            EliminarProduct(binding.root.context,pro.idproduc.toLong())
            DialogoCRUD("Producto Deshabilitado","Se deshabilito el estado del producto "+pro.name_p,ProductFragment())
        }else{
            pro.state=1
            ActualizarProduct(binding.root.context,pro,pro.idproduc.toLong())
            DialogoCRUD("Producto Habilitado","Se habilitó el estado del producto "+pro.name_p,ProductFragment())
        }
    }
    fun mostrarproduct(context: Context){
        val call = productService!!.MostrarProduct()
        call!!.enqueue(object :Callback<List<Product>>{
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroProducto = response.body() as MutableList<Product>
                    adaptadorProduct = AdaptadorProduct(lista=registroProducto!!,
                        onClickListener={b,pos->onClickListener(b,pos)},
                        onClickDeleteChangued={del,bra->onClickDeleteChangued(del,bra)})
                }else{
                    println("Error")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun registrarProducto(context: Context, p:Product){
        val call = productService!!.RegistrarProduct(p)
        call!!.enqueue(object :Callback<Product?>{
            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro el producto")
                }
            }

            override fun onFailure(call: Call<Product?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }
    fun ActualizarProduct(context: Context, p:Product, id: Long ){
        val call = productService!!.ActualizarProduct(id,p)
        call!!.enqueue(object : Callback<Product?>{
            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Producto")
                    fila=null
                }
            }

            override fun onFailure(call: Call<Product?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }
        })
    }

    fun EliminarProduct(context: Context, id: Long){
        val call = productService!!.EliminarProduct(id)
        call!!.enqueue(object: Callback<Product?>{
            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }
            override fun onFailure(call: Call<Product?>, t: Throwable) {
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}