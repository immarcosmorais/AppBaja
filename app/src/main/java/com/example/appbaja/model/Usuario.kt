package com.example.appbaja.model

import com.example.appbaja.config.getFirebaseDatabase
import com.google.firebase.database.Exclude


class Usuario{
    @get:Exclude
    var id = ""
    var nome = ""
    var sobrenome = ""
    var curso = -1
    var periodo = -1
    var email = ""
    @get:Exclude
    var senha = ""

    constructor(id: String, nome: String, sobrenome: String, curso: Int, periodo: Int, email: String, senha: String) {
        this.id = id
        this.nome = nome
        this.sobrenome = sobrenome
        this.curso = curso
        this.periodo = periodo
        this.email = email
        this.senha = senha
    }

    constructor(email: String, senha: String) {
        this.email = email
        this.senha = senha
    }

    fun salvar(){
        var firebaseRef = getFirebaseDatabase()
        var usuario = firebaseRef.child("usuario").child(this.id)
        usuario.setValue(this)
    }

}