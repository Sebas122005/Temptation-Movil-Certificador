package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Category

class AdaptadorCategory(context:Context?, private val listcategory:List<Category>?):BaseAdapter(){

    private val layoutInflater: LayoutInflater
    init {
        layoutInflater =  LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listcategory!!.size
    }

    override fun getItem(p0: Int): Any {
        return listcategory!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_lista_category,p2,false);
            val objcategory =  getItem(p0) as Category
            //creamos los controladores
            val lstidcat = vista!!.findViewById<TextView>(R.id.lstidcat)
            val lstname_cat = vista!!.findViewById<TextView>(R.id.lstname_cat)
            val lststate_cat = vista!!.findViewById<TextView>(R.id.lststate_cat)

            lstidcat.text =""+objcategory.idcat
            lstname_cat.text = ""+objcategory.name_cat
            if (objcategory.state == 1){
                lststate_cat.text = "Habilitado"
            }else{
                lststate_cat.text = "Desabilitado"
            }
        }
        return vista!!;
    }

}