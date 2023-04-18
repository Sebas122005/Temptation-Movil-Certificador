package com.example.temptationmovile.utilidad

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.ViewGroup
import android.widget.*

class Util {
    //creamos una variable de tipo alertDialog
    private var dialogo: AlertDialog.Builder?=null

    //creamos un procedimiento para salir
    fun salirSistema(context: Context){
        dialogo = AlertDialog.Builder(context)
        dialogo!!.setTitle("Saliendo del sistema")
        dialogo!!.setMessage("¿Estas seguro que quieres salir del sistema?")
        dialogo!!.setCancelable(false)
        dialogo!!.setPositiveButton("Si"){
                dialogo,which->(context as Activity).finish()
        }
        dialogo!!.setNegativeButton("No"){
                dialogo,which->
        }
        dialogo!!.show()
    }


    //creamos procedimiento para mostrar los mensajes toast
    fun MensajeToast(context: Context, men: String){
        Toast.makeText(context, men, Toast.LENGTH_SHORT).show()

    }

    fun limpiar(viewGroup: ViewGroup){
        var i = 0
        val count = viewGroup.childCount
        while (i<count){
            val view = viewGroup.getChildAt(i);
            if(view is EditText){
                view.setText("")
            }
            //radio
            if(view is RadioGroup){
                (view.getChildAt(0)as RadioButton).isChecked = false;
            }
            //para spinner
            if(view is Spinner){
                view.setSelection(0);
            }
            if(view is CheckBox){
                view.isChecked = false
            }
            if(view is ViewGroup && view.childCount>0){
                limpiar(view as ViewGroup)
            }
        }
    }
}