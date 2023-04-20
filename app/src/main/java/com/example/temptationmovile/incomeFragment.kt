package com.example.temptationmovile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.temptationmovile.adaptadores.AdaptadorComboProvider
import com.example.temptationmovile.adaptadores.AdaptadorIncome
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Income
import com.example.temptationmovile.clases.Product
import com.example.temptationmovile.clases.Provider
import com.example.temptationmovile.databinding.FragmentIncomeBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.IncomeService
import com.example.temptationmovile.servicios.ProviderService
import com.example.temptationmovile.utilidad.Util
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [incomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class incomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var listener:OnFragmentInteractionListener?=null
    private var _binding:FragmentIncomeBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapterIncome: AdaptadorIncome
    private var llmanager: LinearLayoutManager?=null
    private var providerService: ProviderService?=null
    private var incomeService: IncomeService?=null

    private var registroIncome: MutableList<Income>? = null
    private var registroProvider: List<Provider>? = null

    private val objprovider = Provider()
    private val objIncome = Income()

    private var fecha = Calendar.getInstance().time
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
    private var fechaAct = formatoFecha.format(fecha)
    private var fechita = fechaAct

    var objutilidad =  Util()
    private var dialogo: AlertDialog.Builder? = null
    var ft: FragmentTransaction?= null
    private var fila:Int?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIncomeBinding.inflate(inflater, container, false)
        var context = binding.root.context
        registroIncome = ArrayList()
        registroProvider = ArrayList()
        llmanager= LinearLayoutManager(context)
        providerService = ApiUtil.providerService
        incomeService = ApiUtil.incomeService

        mostrarComboProvider(context)
        mostrarincome(context)

        binding.txtfechacomprainco.setText(fechaAct)

        binding.btnregistrarincome.setOnClickListener {
            if(binding.cboprovidersincome.selectedItemPosition==-1){
                objutilidad.MensajeToast(context,"Seleccionne un Proveedor")
                binding.cboprovidersincome.requestFocus()
            }else{
                fechita = fechaAct
                objIncome.idprovider=(registroProvider as ArrayList<Provider>).get(binding.cboprovidersincome.selectedItemPosition).idprovider
                objIncome.dateinco=fechaAct

                registroIncome(context,objIncome)
                val fincome = incomeFragment()
                DialogoCRUD("Registro de Compra", "Se registro la Compra Correctamente",fincome)
            }
        }
        binding.btndetaInco.setOnClickListener {
            val fra = DetailIncomeFragment()
            ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.contenedor, fra, null)
            ft?.addToBackStack(null)
            ft?.commit()
        }
        binding.btnGenerarReporteincome.setOnClickListener {
            val document = Document(PageSize.A4,50f,50f,50f,50f)
            val fileName = "mi_lista_compra.pdf"
            val filePath = requireContext().getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS)?.get(0)?.absolutePath + "/" + fileName
            val writer = PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()
            writer.open()
            val boldFont = Font(Font.FontFamily.TIMES_ROMAN, 22f, Font.BOLD, BaseColor.DARK_GRAY)
            val titleChunk = Chunk("Lista de Compras", boldFont)
            val titleParagraph = Paragraph(titleChunk)
            titleParagraph.alignment = Element.ALIGN_CENTER
            document.add(titleParagraph)
            document.add(Paragraph(""))
            registroIncome=reporte() as MutableList<Income>
            var tabla = PdfPTable(4)
            tabla.addCell("ID Compra")
            tabla.addCell("Proveedor")
            tabla.addCell("Fecha")
            tabla.addCell("Estado")
            for (item in registroIncome!!) {
                var act=""
                var nombre=""
                tabla.addCell(item.idincome.toString())
                tabla.addCell(item.idprovider.toString())
                tabla.addCell(item.dateinco.toString())
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

        binding.bnGenerarCotizacion.setOnClickListener {
            val fra = GenerarCotizacionFragment()
            ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.contenedor, fra, null)
            ft?.addToBackStack(null)
            ft?.commit()
        }
        return binding.root
    }

    fun mostrarComboProvider(context: Context){
        val call = providerService!!.MostrarProvider()
        call!!.enqueue(object: Callback<List<Provider>?> {
            override fun onResponse(call: Call<List<Provider>?>, response: Response<List<Provider>?>) {
                if(response.isSuccessful){
                    registroProvider = response.body()
                    binding.cboprovidersincome.adapter = AdaptadorComboProvider(context, registroProvider)
                }
            }

            override fun onFailure(call: Call<List<Provider>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun registroIncome(context: Context, p: Income){
        val call = incomeService!!.RegistrarIncome(p)
        call!!.enqueue(object :Callback<Income?>{
            override fun onResponse(call: Call<Income?>, response: Response<Income?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro la Compra")
                }
            }

            override fun onFailure(call: Call<Income?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }
    fun reporte():List<Income>{
        val call = incomeService!!.MostrarIncomes()
        call!!.enqueue(object :Callback<List<Income>>{
            override fun onResponse(call: Call<List<Income>>, response: Response<List<Income>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroIncome = response.body() as MutableList<Income>?
                }else{
                    println("Error")
                }
            }
            override fun onFailure(call: Call<List<Income>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
        return registroIncome!!
    }
    interface OnFragmentInteractionListener{
        fun onFragmentInteraction(texto:Int)
    }

    fun onClickListener(brand: Income, pos: Int) {
        dialogo = AlertDialog.Builder(context)
        dialogo!!.setTitle("Registrar Detalle")
        dialogo!!.setMessage("Desea registrar detalle de esta Compra?")
        dialogo!!.setCancelable(true)
        dialogo!!.setPositiveButton("Ok") { dialogo, which ->
            listener?.onFragmentInteraction(brand.idincome)
            val fra = DetailIncomeFragment()
            ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.contenedor, fra, null)
            ft?.addToBackStack(null)
            ft?.commit()
        }
        dialogo!!.show()
    }

    fun mostrarincome(context: Context){
        val call = incomeService!!.MostrarIncomes()
        call!!.enqueue(object :Callback<List<Income>>{
            override fun onResponse(call: Call<List<Income>>, response: Response<List<Income>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroIncome = response.body() as MutableList<Income>
                    adapterIncome = AdaptadorIncome(registroIncome!!, ({er,pos->onClickListener(er,pos)}))
                    binding.lstincome.layoutManager=llmanager
                    binding.lstincome.adapter=adapterIncome
                }else{
                    println("Error")
                }
            }

            override fun onFailure(call: Call<List<Income>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener){
            listener=context
        }else{
            throw java.lang.RuntimeException(context.toString() + "must implement OnFragmentInteractionListener")
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener=null
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            incomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}