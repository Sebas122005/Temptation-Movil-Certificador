package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Person
import com.example.temptationmovile.clases.Rol

class AdaptadorComboRol(context: Context?, private val listaPerson: List<Rol>?): BaseAdapter() {
    private val layoutInflater: LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)
    }
    override fun getCount(): Int {
        return  listaPerson!!.size
    }

    override fun getItem(p0: Int): Any {
        return listaPerson!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_combo_rol,p2,false)
            val objrol = getItem(p0) as Rol
            val lblroPersoncomb = vista!!.findViewById<TextView>(R.id.lblroPersoncomb)
            lblroPersoncomb.text = ""+objrol.namerol.trim()
        }
        return vista!!
    }
}