package com.example.temptationmovile.adaptadores

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Color

class AdaptadorComboColor(context: Context?, private val listacolor: List<Color>?):BaseAdapter() {
    private val layoutInflater: LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listacolor!!.size
    }

    override fun getItem(p0: Int): Any {
       return listacolor!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_combo_color,p2,false)
            val objcolor =  getItem(p0) as Color
            val lblnamecolorcomb = vista!!.findViewById<TextView>(R.id.lblnamecolcomb)
            lblnamecolorcomb.text = ""+objcolor.name_col
        }
        return vista!!
    }
}