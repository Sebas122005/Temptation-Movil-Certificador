package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Category

class AdaptadorComboCategory(context: Context, private val listacategory: List<Category>?):BaseAdapter() {
    private val layoutInflater : LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listacategory!!.size
    }

    override fun getItem(p0: Int): Any {
        return listacategory!![p0]
    }

    override fun getItemId(p0: Int): Long {
       return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista==null){
            vista = layoutInflater.inflate(R.layout.elemento_combo_category,p2,false)
            val objcategory = getItem(p0) as Category
            val lblnamecatcombo = vista!!.findViewById<TextView>(R.id.lblnamecatcomb)
            lblnamecatcombo.text = ""+objcategory.name_cat
        }
        return  vista!!
    }
}