package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Color


class AdaptadorColor(contex:Context?,private val listacolor:List<Color>?):BaseAdapter() {

    private val layoutInflater: LayoutInflater
    init{
        layoutInflater = LayoutInflater.from(contex)
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
            vista=layoutInflater.inflate(R.layout.elemento_lista_color,p2,false)
            val objcolor = getItem(p0) as Color

            //creamos los controles
            val lstidcolor = vista!!.findViewById<TextView>(R.id.lstidcolor)
            val lstname_color = vista!!.findViewById<TextView>(R.id.lstname_color)
            val lststatecolor = vista!!.findViewById<TextView>(R.id.lststatecolor)

            //agregamos los valores a los controladores
            lstidcolor.text = ""+objcolor.idcolor
            lstname_color.text = ""+objcolor.name_col
            if(objcolor.state == 1)
            {
                lststatecolor.text = "Habilitado"
            }else{
                lststatecolor.text = "Deshabilitado"
            }

        }
        return vista!!
    }
}