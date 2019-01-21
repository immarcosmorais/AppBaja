package com.example.marcos.bajaapp.helper

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog

fun validarPermissoes(permissioes: Array<String>, activity:Activity, requestCode:Int):Boolean{
    if (Build.VERSION.SDK_INT > 22){
        var listaPermissoes:MutableList<String> = mutableListOf()
        for (permissao in permissioes){
            var flag = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED
            if (!flag) listaPermissoes.add(permissao)
        }
        if (listaPermissoes.isEmpty()) return true

        //Solicitando permissões
        ActivityCompat.requestPermissions(activity, listaPermissoes.toTypedArray(), requestCode)
    }
    return true
}