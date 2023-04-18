package com.example.temptationmovile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CargaActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Aqui se infica que primero carge la Mainactivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}