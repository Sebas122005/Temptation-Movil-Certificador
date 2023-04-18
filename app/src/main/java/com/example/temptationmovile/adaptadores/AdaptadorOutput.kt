package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Output
import com.example.temptationmovile.clases.Product

class AdaptadorOutput(context: Context?, private val listoutput: List<Output>?): BaseAdapter() {

    private val layoutInflater: LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)

    }

    override fun getCount(): Int {
        return listoutput!!.size
    }

    override fun getItem(p0: Int): Any {
        return listoutput!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_lista_output,p2,false)
            val objoutput = getItem(p0) as Output
            val lblidoutput = vista!!.findViewById<TextView>(R.id.lblidoutput)
            val lblidprod = vista!!.findViewById<TextView>(R.id.lblidprod)
            val lblcant = vista!!.findViewById<TextView>(R.id.lblcant)
            val lbldestino = vista!!.findViewById<TextView>(R.id.lbldestino)

            lblidoutput.text = ""+objoutput.idout
            lblidprod.text = ""+objoutput.idproduc
            lblcant.text = ""+objoutput.quantity
            lbldestino.text = ""+objoutput.destino

        }
        return vista!!
    }

}