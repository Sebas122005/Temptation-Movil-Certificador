package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Product

class AdaptadorProduct(context: Context?, private val listProduct: List<Product>?): BaseAdapter() {

    private val layoutInflater: LayoutInflater
    init {
        layoutInflater = LayoutInflater.from(context)

    }

    override fun getCount(): Int {
        return  listProduct!!.size
    }

    override fun getItem(p0: Int): Any {
        return listProduct!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_lista_producto,p2,false)
            val objproduct = getItem(p0) as Product
            val lblidprode = vista!!.findViewById<TextView>(R.id.lblidproducE)
            val lblname_p = vista!!.findViewById<TextView>(R.id.lblname_p_E)
            val lblpriceE = vista!!.findViewById<TextView>(R.id.lblpriceE)
            val lblstockE = vista!!.findViewById<TextView>(R.id.lblstockE)
            val lblstateE = vista!!.findViewById<TextView>(R.id.lblstateE)



            lblidprode.text = ""+objproduct.idproduc
            lblname_p.text = ""+objproduct.name_p
            lblpriceE.text = ""+objproduct.price
            lblstockE.text = ""+objproduct.stock
            if(objproduct.state == 1)
            {
                lblstateE.text = "Habilitado"
            }else{
                lblstateE.text = "Deshabilitado"
            }
        }
        return vista!!
    }
}