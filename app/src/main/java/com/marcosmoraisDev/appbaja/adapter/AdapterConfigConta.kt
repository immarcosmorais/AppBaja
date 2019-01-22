package com.marcosmoraisDev.appbaja.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.marcosmoraisDev.appbaja.R
import com.marcosmoraisDev.appbaja.model.ItemConfigConta

class AdapterConfigConta(var itens: MutableList<ItemConfigConta>) : RecyclerView.Adapter<AdapterConfigConta.MyViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        var item = LayoutInflater.from(p0.context).inflate(R.layout.layout_lista_config_conta, p0, false)
        return MyViewHolder(item)
    }

    override fun getItemCount(): Int {
        return itens.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        var item = itens[p1]
        p0.imagem.setImageResource(item.imagem)
        p0.titulo.text = item.titulo
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagem:ImageView = itemView.findViewById(R.id.imvIcone)
        var titulo:TextView = itemView.findViewById(R.id.imvTitulo)

    }

}
