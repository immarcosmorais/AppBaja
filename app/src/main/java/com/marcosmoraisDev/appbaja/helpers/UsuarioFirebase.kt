package com.marcosmoraisDev.marcos.bajaapp.helper

import com.marcosmoraisDev.appbaja.config.getFirebaseAuth

fun getIdUsuarioFirebase():String{
    var id = getFirebaseAuth()!!.currentUser!!.email
    return codificarBase64(id!!)
}