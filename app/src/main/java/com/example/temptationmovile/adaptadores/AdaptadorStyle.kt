package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Style

class AdaptadorStyle(contex:Context?,private val listastyle:List<Style>?):BaseAdapter() {

    private val layoutInflater: LayoutInflater
    init{
        layoutInflater = LayoutInflater.from(contex)
    }
    override fun getCount(): Int {
        return listastyle!!.size
    }

    override fun getItem(p0: Int): Any {
        return listastyle!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista=layoutInflater.inflate(R.layout.elemento_lista_style,p2,false)
            val objstyle = getItem(p0) as Style

            //creamos los controles
            val lstidstyle = vista!!.findViewById<TextView>(R.id.lstidstyle)
            val lstname_style = vista!!.findViewById<TextView>(R.id.lstname_style)
            val lststate_style = vista!!.findViewById<TextView>(R.id.lststate_style)

            //agregamos los valores a los controladores
            lstidstyle.text = ""+objstyle.idstyles
            lstname_style.text = ""+objstyle.name_sty
            if(objstyle.state == 1)
            {
                lststate_style.text = "Habilitado"
            }else{
                lststate_style.text = "Deshabilitado"
            }

        }
        return vista!!
    }


}