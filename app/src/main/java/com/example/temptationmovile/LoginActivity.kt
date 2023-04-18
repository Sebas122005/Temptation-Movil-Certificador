package com.example.temptationmovile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.temptationmovile.clases.ResponseLogin
import com.example.temptationmovile.databinding.ActivityLoginBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.PersonService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val objtutilidad = Util()
    private var personService: PersonService = ApiUtil.personService!!
    //declaramos variables
    private var usu = ""
    private var cla =""
    val objPerson = ResponseLogin();
    var objutilidad  =  Util();

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnIngresar = findViewById<Button>(binding.btningresar.id)


        btnIngresar.setOnClickListener{
            fun salir(username: String){
                val intenmain = Intent(this, MainActivity::class.java).apply {
                    putExtra("username", username);
                }

                startActivity(intenmain)
                this.finish()
            }

            if(binding.txtusername.getText().toString()==""){
                objtutilidad.MensajeToast(this, "Ingrese el Usuario");
                binding.txtusername.requestFocus()
            }else if(binding.txtpassword.getText().toString()==""){
                objtutilidad.MensajeToast(this, "Ingrese la contraseña");
                binding.txtusername.requestFocus()
            }else{
                usu = binding.txtusername.getText().toString();
                cla = binding.txtpassword.getText().toString();

                objPerson.username = usu
                objPerson.password = cla
                personService.login(objPerson)!!.enqueue(object : Callback<ResponseLogin> {
                    override fun onResponse(
                        call: Call<ResponseLogin>,
                        response: Response<ResponseLogin>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(
                                applicationContext,
                                "Bienvenido a la aplicacion Temptation usuario: "+ objPerson.username,
                                Toast.LENGTH_SHORT
                            ).show()
                            salir(objPerson.username);
                        }else{
                            Toast.makeText(
                                applicationContext,
                                "Usuario no Encontrado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                        println("Error: ${t.message}")
                    }

                })

            }

        }


        val btnsalir = findViewById<Button>(binding.btnsalir.id);

        btnsalir.setOnClickListener{
            objtutilidad.salirSistema(this);
        }
    }
}