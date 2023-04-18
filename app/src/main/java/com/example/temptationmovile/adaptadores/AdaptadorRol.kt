package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Rol

class AdaptadorRol(context:Context?,private val listRol: List<Rol>?):BaseAdapter() {

   private val layoutInflater:LayoutInflater
   init {
       layoutInflater= LayoutInflater.from(context)
   }
    override fun getCount(): Int {
        return listRol!!.size
    }

    override fun getItem(position: Int): Any {
        return listRol!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var vista = convertView
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_lista_rol,parent,false);
            val objRol =  getItem(position) as Rol
            //creamos los controladores
            val lstcodRol = vista!!.findViewById<TextView>(R.id.lstcodrol)
            val lstnamerol = vista!!.findViewById<TextView>(R.id.lstnamerol)
            val lststate_rol = vista!!.findViewById<TextView>(R.id.lststate_rol)

            lstcodRol.text =""+objRol.idrol
            lstnamerol.text = ""+objRol.namerol
            if (objRol.state == 1){
                lststate_rol.text = "Habilitado"
            }else{
                lststate_rol.text = "Desabilitado"
            }
        }
        return vista!!;
    }

}