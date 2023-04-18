package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.DetailIncome
import com.example.temptationmovile.clases.Income

class AdaptadorDetailIncome (context: Context?, private val listDetailIncome:List<DetailIncome>): BaseAdapter() {
    private val layoutInflater: LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)

    }

    override fun getCount(): Int {
        return listDetailIncome.size
    }

    override fun getItem(p0: Int): Any {
        return listDetailIncome!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_lista_detailincome,p2,false)
            val objdetailincome = getItem(p0) as DetailIncome
            val lstiddetincome = vista!!.findViewById<TextView>(R.id.lstiddetailincomeE)
            val lstidincome = vista!!.findViewById<TextView>(R.id.lstidincomeE)
            val lstidproduct = vista!!.findViewById<TextView>(R.id.lstidproductE)
            val lstpricebuy = vista!!.findViewById<TextView>(R.id.lstprice_buyE)
            val lstquantity = vista!!.findViewById<TextView>(R.id.lstquantityE)

            lstiddetincome.text =""+objdetailincome.iddetincome
            lstidincome.text =""+objdetailincome.idincome
            lstidproduct.text =""+objdetailincome.idproduc
            lstpricebuy.text = ""+objdetailincome.price_buy
            lstquantity.text = ""+objdetailincome.quantity
        }
        return vista!!
    }
}