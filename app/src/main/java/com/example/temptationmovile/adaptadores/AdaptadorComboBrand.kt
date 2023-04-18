package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Brand

class AdaptadorComboBrand (contex:Context?,private val listbrand:List<Brand>?): BaseAdapter(){
    private val layoutInflater: LayoutInflater

    init {
        layoutInflater = LayoutInflater.from(contex)
    }

    override fun getCount(): Int {
       return listbrand!!.size
    }

    override fun getItem(p0: Int): Any {
        return  listbrand!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong();
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_combo_brand,p2,false)
            val objbrand = getItem(p0) as Brand
            val lblNameBrand = vista!!.findViewById<TextView>(R.id.lblNamebrand)
            lblNameBrand.text= ""+objbrand.name_brand
        }
        return vista!!
    }
}