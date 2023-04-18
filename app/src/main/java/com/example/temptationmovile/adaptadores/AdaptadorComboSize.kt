package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Size

class AdaptadorComboSize(context: Context?, private val listasize: List<Size>?):BaseAdapter() {
    private val layoutInflater: LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listasize!!.size
    }

    override fun getItem(p0: Int): Any {
        return  listasize!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista ==null){
            vista = layoutInflater.inflate(R.layout.elemento_combo_size,p2,false)
            val objsize = getItem(p0) as Size
            val lblnamesizecomb = vista!!.findViewById<TextView>(R.id.lblnamesizecomb)
            lblnamesizecomb.text = ""+objsize.name_size
        }
        return vista!!
    }
}