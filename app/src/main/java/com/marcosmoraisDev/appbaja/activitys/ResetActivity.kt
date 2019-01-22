package com.marcosmoraisDev.appbaja.activitys

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.marcosmoraisDev.appbaja.R
import com.marcosmoraisDev.appbaja.config.getFirebaseAuth
import com.marcosmoraisDev.appbaja.helpers.msgToast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset.*

class ResetActivity : AppCompatActivity() {

    private lateinit var autenticacao: FirebaseAuth
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        this.autenticacao = getFirebaseAuth()!!

        btnRecuperaSenha.setOnClickListener {
            if (verificaCampos()){
                recuperaEmail()
            }
        }

    }

    private fun recuperaEmail() {
        setProgressDialog(getString(R.string.text_autenticando))
        btnRecuperaSenha.isEnabled = false
        autenticacao.sendPasswordResetEmail(edtEmail.text.toString()).addOnCompleteListener {
            if (it.isSuccessful){
                resetSucesso(getString(R.string.msg_sucesso_recupera_senha))
            }else{
                var execao = ""
                try {
                     throw it.exception!!
                }catch (e: Exception){
                    execao = e.message.toString()
                    e.printStackTrace()
                }
                resetFalhou(getString(R.string.msg_error_recupera_senha) + " : " + execao)
            }
        }
        dialog.cancel()
    }

    private fun verificaCampos(): Boolean {
        var flag = true
        if (edtEmail.text.toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches()) {
            edtEmail.error = getString(R.string.msg_error_email_input)
            flag = false
        } else {
            edtEmail.error = null
        }
        return flag
    }

    private fun resetSucesso(msg:String){
        msgToast(this, msg)
        finish()
    }

    private fun resetFalhou(msg:String){
        msgToast(this, msg)
        btnRecuperaSenha.isEnabled = true
    }

    @SuppressLint("ResourceAsColor")
    fun setProgressDialog(msg: String) {

        val llPadding = 30
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(this)
        tvText.text = msg
        tvText.setTextColor(R.color.colorAccent)
        tvText.textSize = 20f
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setView(ll)

        dialog = builder.create()
        dialog.show()
        val window = dialog.getWindow()
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.getWindow().getAttributes())
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.getWindow().attributes = layoutParams
        }
    }
}
