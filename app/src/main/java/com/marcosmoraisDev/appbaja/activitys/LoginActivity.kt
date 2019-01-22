package com.marcosmoraisDev.appbaja.activitys

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.marcosmoraisDev.appbaja.R
import com.marcosmoraisDev.appbaja.config.getFirebaseAuth
import com.marcosmoraisDev.appbaja.helpers.chamaActivity
import com.marcosmoraisDev.appbaja.helpers.msgToast
import com.marcosmoraisDev.appbaja.model.Usuario
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import android.widget.LinearLayout
import android.view.WindowManager
import android.support.v7.app.AlertDialog
import android.widget.TextView
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ProgressBar
import com.marcosmoraisDev.appbaja.R.color.*
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var dialog:AlertDialog
    private lateinit var autenticacao: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.autenticacao = getFirebaseAuth()!!

        //  Botões
        txvSingup.setOnClickListener {
            chamaActivity(this, SingupActivity::class.java, false)
        }

        txvEsqueceuSenha.setOnClickListener {
            chamaActivity(this, ResetActivity::class.java, false)
        }

        btnLogin.setOnClickListener {
            loga(getUsuarioActivity())
        }

    }

    private fun validaCampos(): Boolean {
        var flag = true
        if (edtEmail.text.toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches()) {
            edtEmail.error = getString(R.string.msg_error_email_input)
            flag = false
        } else {
            edtEmail.error = null
        }
        if (edtSenha.text.toString().isEmpty() || edtSenha.text.toString().length < 4) {
            edtSenha.error = getString(R.string.msg_error_senha_fraca)
            flag = false
        } else {
            edtSenha.error = null
        }
        return flag
    }

    private fun loga(usuario: Usuario) {
        if (validaCampos()) {
            logaUsuario(usuario)
        }
    }

    private fun logaUsuario(usuario: Usuario) {

        setProgressDialog(getString(R.string.text_autenticando))
        btnLogin.isEnabled = false
        autenticacao.signInWithEmailAndPassword(usuario.email, usuario.senha).addOnCompleteListener {
            if (it.isSuccessful) {
                loginSucesso(getString(R.string.msg_sucesso_autentica_usuario))
            } else {
                var excecao = ""
                try {
                    throw  it.exception!!
                } catch (e: FirebaseAuthInvalidUserException) {
                    excecao = getString(R.string.msg_exececao_cadastro_usuario)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    excecao = getString(R.string.msg_exececao_cadastro_usuario_corresponde)
                } catch (e: Exception) {
                    excecao = getString(R.string.msg_error_cadastro_usuario) + ": " + e.message
                }
                loginFalhou(excecao)
            }
            dialog.cancel()
        }

    }

    private fun loginFalhou(msg: String) {
        msgToast(this, msg)
        btnLogin.isEnabled = true
    }

    private fun loginSucesso(msg: String) {
        msgToast(this, msg)
        chamaActivity(this, MainActivity::class.java, true)
    }

    private fun getUsuarioActivity(): Usuario {
        return Usuario(edtEmail.text.toString(), edtSenha.text.toString())
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
        tvText.setTextColor(colorAccent)
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

    private fun verificaUsuario(){
        var usuarioAtual = autenticacao.currentUser
        if (usuarioAtual != null){
            chamaActivity(this, MainActivity::class.java, true)
        }
    }

    override fun onStart() {
        super.onStart()
        verificaUsuario()
    }

}
