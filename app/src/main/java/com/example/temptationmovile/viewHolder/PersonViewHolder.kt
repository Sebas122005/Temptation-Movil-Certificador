package com.example.temptationmovile.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Person
import com.example.temptationmovile.databinding.ElementoListaBrandBinding
import com.example.temptationmovile.databinding.ElementoListaPersonBinding

class PersonViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding= ElementoListaPersonBinding.bind(view)

    fun render(
        person: Person,
        onClickListener: (Person, Int) -> Unit,
        onClickDeleteChangued: (Int, Person) -> Unit
    ){
        binding.txtdniPerson.text=person.dni.toString()
        binding.txtnamelPerson.text=person.name+person.lastname.toString()
        binding.txtRolPerson.text=person.idrol.toString()
        binding.txtUserPerson.text=person.username.toString()

        if (person.state==1){
            binding.txtEstadoPerson.text="Habilitado"
            binding.idbtnEstadoPersona.text="Deshabilitar"
        }else{
            binding.txtEstadoPerson.text="Deshabilitado"
            binding.idbtnEstadoPersona.text="Habilitar"
        }
        binding.idbtnEstadoPersona.setOnClickListener { onClickDeleteChangued(adapterPosition,person) }
        itemView.setOnClickListener { onClickListener(person,adapterPosition) }
    }
}