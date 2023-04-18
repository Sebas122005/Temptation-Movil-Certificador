package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Income


class AdaptadorIncome(context: Context?, private val listIncome:List<Income>): BaseAdapter() {
    private val layoutInflater: LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)

    }

    override fun getCount(): Int {
        return listIncome!!.size
    }

    override fun getItem(p0: Int): Any {
        return listIncome!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_lista_income,p2,false)
            val objincome = getItem(p0) as Income
            val lstidincome = vista!!.findViewById<TextView>(R.id.lstidincome)
            val lstnomproviders = vista!!.findViewById<TextView>(R.id.lstnomproviders)
            val lstfechaincome = vista!!.findViewById<TextView>(R.id.lstfechaincome)



            lstidincome.text =""+objincome.idincome
            lstnomproviders.text = ""+objincome.idprovider
            lstfechaincome.text = ""+objincome.dateinco
        }
        return vista!!
    }


}