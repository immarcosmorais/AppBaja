package com.example.appbaja.helpers

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_singup.*

// Telas
fun chamaTela(atualActivity: Activity, proximaActivity: Class<*>, finalizar: Boolean) {
    var intent = Intent(atualActivity.applicationContext, proximaActivity)
    atualActivity.startActivity(intent)
    if (finalizar) atualActivity.finish()
}

fun msgToast(atualActivity: Activity, msg: String) {
    Toast.makeText(atualActivity.applicationContext, msg, Toast.LENGTH_LONG).show()
}

fun configuraSpinner(atualActivity: Activity, spinner: Spinner) {
    val listaItens = arrayOf("Eng. de Mecânica", "Eng. de Software", "Eng. Civil", "Eng. de Produção")
    val adapter = ArrayAdapter(atualActivity.applicationContext, R.layout.simple_spinner_dropdown_item, listaItens)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = adapter
}

val SELECAO_CAMERA = 100
val SELECAO_GALERIA = 200