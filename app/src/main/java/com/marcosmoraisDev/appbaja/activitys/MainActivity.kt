package com.marcosmoraisDev.appbaja.activitys

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.marcosmoraisDev.appbaja.R
import com.marcosmoraisDev.appbaja.config.getFirebaseAuth
import com.marcosmoraisDev.appbaja.helpers.chamaActivity
import com.marcosmoraisDev.appbaja.helpers.msgToast
import com.google.firebase.auth.FirebaseAuth
import com.marcosmoraisDev.appbaja.fragment.ContatosFragment
import com.marcosmoraisDev.appbaja.fragment.EquipeFragment
import com.marcosmoraisDev.appbaja.fragment.EventosFragment
import com.marcosmoraisDev.appbaja.fragment.NavegacaoFragment
import com.marcosmoraisDev.appbaja.helpers.chamaFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var autenticacao: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        //Meu codigo
        this.autenticacao = getFirebaseAuth()!!
        chamaFragment(supportFragmentManager, EventosFragment())

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {

            R.id.nav_eventos -> {
                chamaFragment(supportFragmentManager, EventosFragment())
            }

            R.id.nav_navegacao -> {
                chamaFragment(supportFragmentManager, NavegacaoFragment())
            }

            R.id.nav_contatos -> {
                chamaFragment(supportFragmentManager, ContatosFragment())
            }

            R.id.nav_equipe -> {
                chamaFragment(supportFragmentManager, EquipeFragment())
            }

            R.id.nav_conta -> {
                chamaActivity(this, ConfigContaActivity::class.java, false)
            }

            R.id.nav_definicoes -> {
                msgToast(this, "Implementando...")
            }

            // Comunicação
            R.id.nav_share -> {
                msgToast(this, "Implementando...")
            }

            R.id.nav_send -> {
                msgToast(this, "Implementando...")
            }

            R.id.nav_sair -> {
                deslogaUsuario()
                chamaActivity(this, LoginActivity::class.java, true)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun deslogaUsuario() {
        try {
            autenticacao.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
