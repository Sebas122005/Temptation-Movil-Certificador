package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Provider

class AdaptadorComboProvider(contex: Context?, private val lstproviders:List<Provider>?): BaseAdapter(){
    private val layoutInflater: LayoutInflater

    init {
        layoutInflater = LayoutInflater.from(contex)
    }

    override fun getCount(): Int {
        return lstproviders!!.size
    }

    override fun getItem(p0: Int): Any {
        return  lstproviders!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong();
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_combo_providers,p2,false)
            val objprovider = getItem(p0) as Provider
            val lblnomprovider = vista!!.findViewById<TextView>(R.id.lblnomprovider)
            lblnomprovider.text= ""+objprovider.name_prov
        }
        return vista!!
    }
}