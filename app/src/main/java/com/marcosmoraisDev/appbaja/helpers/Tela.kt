package com.marcosmoraisDev.appbaja.helpers

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.view.View
import android.view.inputmethod.InputMethodManager


// Telas
fun chamaActivity(atualActivity: Activity, proximaActivity: Class<*>, finalizar: Boolean) {
    var intent = Intent(atualActivity.applicationContext, proximaActivity)
    atualActivity.startActivity(intent)
    if (finalizar) atualActivity.finish()
}

fun chamaFragment(manager: FragmentManager , fragment: Fragment){
    var fragmentTransaction = manager.beginTransaction()
    fragmentTransaction.replace(com.marcosmoraisDev.appbaja.R.id.frameContainer, fragment)
    fragmentTransaction.commit()
}

fun msgToast(atualActivity: Activity, msg: String) {
    Toast.makeText(atualActivity.applicationContext, msg, Toast.LENGTH_LONG).show()
}

fun configuraSpinner(atualActivity: Activity, spinner: Spinner) {
    val listaItens = arrayOf("Eng. de Mecânica", "Eng. de Software", "Eng. Civil", "Eng. de Produção")
    val adapter = ArrayAdapter(atualActivity.applicationContext, com.marcosmoraisDev.appbaja.R.layout.layout_spinner, listaItens)
//    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = adapter
}

val SELECAO_CAMERA = 100
val SELECAO_GALERIA = 200