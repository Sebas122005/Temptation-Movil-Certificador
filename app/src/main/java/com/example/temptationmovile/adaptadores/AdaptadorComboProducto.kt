package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Product

class AdaptadorComboProducto(context: Context?, private val listaproducto: List<Product>?): BaseAdapter() {
    private val layoutInflater: LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listaproducto!!.size
    }

    override fun getItem(p0: Int): Any {
        return listaproducto!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_combo_producto,p2,false)
            val objprod =  getItem(p0) as Product
            val lblnameprodcomb = vista!!.findViewById<TextView>(R.id.lblnameprodcomb)
            lblnameprodcomb.text = ""+objprod.name_p
        }
        return vista!!
    }
}