package com.example.temptationmovile

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.temptationmovile.databinding.ActivityMainBinding
import com.example.temptationmovile.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        /*binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_rol,R.id.nav_size
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //CREAMOS CONSTANTE PARA PODER MANEJAR LAS OPCIONES DEL MENU
        val id = item.itemId


        return when (id) {
            R.id.jmiInicio -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_inicio = HomeFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_inicio).commit()
                true
            }

            R.id.jmibrand -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_brand = brandFragmene()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_brand).commit()
                true
            }
            R.id.jmicolor -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_color = colorfragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_color).commit()
                true
            }
            R.id.jmicategory -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_category = CategoryFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_category).commit()
                true
            }
            R.id.jmiproduct -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_prod = ProductFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_prod).commit()
                true
            }
            R.id.jmirol -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_rol = RolFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_rol).commit()
                true
            }
            R.id.jmisize -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_size = SizeFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor, frag_size)
                    .commit()
                true
            }
            R.id.jmistyle -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_style = StyleFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_style).commit()
                true
            }
            R.id.jmiperson -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_pers = PersonFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor, frag_pers)
                    .commit()
                true
            }
            R.id.jmiprovider -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_prov = ProvidersFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_prov).commit()
                true
            }
            R.id.jmiincome -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_income = incomeFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_income).commit()
                true
            }
            R.id.jmidetailincome -> {
                //creamos una constante del fragmento que vamos a cambiar
                val frag_detail = DetailIncomeFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_detail).commit()
                true
            }
            R.id.jmioutput -> {
                val frag_output = OutputFragment()
                //EL CONTENEDOR SERÁ REEMPLAZADO POR EL FRAGMENTO REQUERIDO, QUE EN ESTE CASO ES EL 'FRAGMENTO INICIO'
                supportFragmentManager.beginTransaction().replace(R.id.contenedor,frag_output).commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}