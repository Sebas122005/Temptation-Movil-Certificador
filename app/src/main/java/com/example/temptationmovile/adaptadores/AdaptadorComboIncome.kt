package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Income
import com.example.temptationmovile.clases.Product

class AdaptadorComboIncome (context: Context?, private val listaincome: List<Income>?): BaseAdapter(){
    private val layoutInflater: LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listaincome!!.size
    }

    override fun getItem(p0: Int): Any {
        return listaincome!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_combo_income,p2,false)
            val objincome =  getItem(p0) as Income
            val lblidincome = vista!!.findViewById<TextView>(R.id.lblidincomecombo)
            lblidincome.text = ""+objincome.idincome
        }
        return vista!!
    }
}