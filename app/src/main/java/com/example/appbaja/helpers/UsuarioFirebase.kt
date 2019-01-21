package com.example.marcos.bajaapp.helper

import com.example.appbaja.config.getFirebaseAuth

fun getIdUsuarioFirebase():String{
    var id = getFirebaseAuth()!!.currentUser!!.email
    return codificarBase64(id!!)
}