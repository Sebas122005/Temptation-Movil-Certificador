package com.example.temptationmovile.adaptadores

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Style

class AdaptadorComboEstilo(context: Context?,private val listaEstilo: List<Style>?): BaseAdapter() {
    private val layoutInflater: LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listaEstilo!!.size
    }

    override fun getItem(p0: Int): Any {
        return listaEstilo!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista ==null){
            vista = layoutInflater.inflate(R.layout.elemento_combo_style,p2,false)
            val objstyle = getItem(p0) as Style
            val lblnamestylecom = vista!!.findViewById<TextView>(R.id.lblnamestylecomb)
            lblnamestylecom.text = ""+objstyle.name_sty
        }
        return vista!!
    }
}