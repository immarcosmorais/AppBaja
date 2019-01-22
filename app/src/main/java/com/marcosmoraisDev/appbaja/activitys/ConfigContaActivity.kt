package com.marcosmoraisDev.appbaja.activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import com.marcosmoraisDev.appbaja.R
import com.marcosmoraisDev.appbaja.adapter.AdapterConfigConta
import com.marcosmoraisDev.appbaja.helpers.msgToast
import com.marcosmoraisDev.appbaja.model.ItemConfigConta
import com.marcosmoraisDev.appbaja.model.RecyclerItemClickListener
import kotlinx.android.synthetic.main.activity_config_conta.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class ConfigContaActivity : AppCompatActivity() {

    private val LISTA_ITENS = mutableListOf<ItemConfigConta>(
        ItemConfigConta(
            R.drawable.ic_person_24dp,
            "Dados usuário"
        ),
        ItemConfigConta(
            R.drawable.ic_vpn_key_24dp,
            "Alterar Senha"
        )
    )
    private lateinit var adapter: AdapterConfigConta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_conta)

        adapter = AdapterConfigConta(LISTA_ITENS)

        configuraToolbar()
        configuraRecyclerview()
        configuraEventoClick()

    }

    private fun configuraEventoClick() {
        rcvItensConfigConta.addOnItemTouchListener(
            RecyclerItemClickListener(applicationContext, rcvItensConfigConta,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        Toast.makeText(applicationContext, "Item pressionado", Toast.LENGTH_LONG).show()
                    }

                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        Toast.makeText(applicationContext, "Item longo pressionado", Toast.LENGTH_LONG).show()
                    }

                })
        )
    }

    private fun configuraRecyclerview() {
        var layoutManager = LinearLayoutManager(applicationContext)
        rcvItensConfigConta.layoutManager = layoutManager
        rcvItensConfigConta.setHasFixedSize(true)
        rcvItensConfigConta.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        rcvItensConfigConta.adapter = adapter
    }

    private fun configuraToolbar() {
        //Configurando Toolbar
        tobPrincipal.title = getString(R.string.title_conta)
        setSupportActionBar(tobPrincipal)
        //Botão voltar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    //Button back
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
        return when (item!!.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}
