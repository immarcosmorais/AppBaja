package com.example.appbaja.activitys

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.appbaja.R
import com.example.appbaja.config.getFirebaseAuth
import com.example.appbaja.config.getStorageReference
import com.example.appbaja.helpers.*
import com.example.appbaja.model.Usuario
import com.example.marcos.bajaapp.helper.codificarBase64
import com.example.marcos.bajaapp.helper.validarPermissoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_singup.*
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.io.ByteArrayOutputStream
import kotlin.coroutines.EmptyCoroutineContext


class SingupActivity : AppCompatActivity() {

    private val PERMISSOES_NECESSARIAS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private lateinit var dialog:AlertDialog
    private lateinit var autenticacao: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var imagem: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)

        validarPermissoes(PERMISSOES_NECESSARIAS, this, 1)
        configuraSpinner(this, spnCurso)

        imvCamera.setOnClickListener {
            abreCamera()
        }

        imvGaleria.setOnClickListener {
            abreGaleria()
        }

        btnSingup.setOnClickListener {
            inscreva(getUsuarioActivity())
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            try {
                when (requestCode){
                    SELECAO_CAMERA -> imagem = data!!.extras.get("data") as Bitmap
                    SELECAO_GALERIA -> {
                        var local = data!!.data
                        imagem = MediaStore.Images.Media.getBitmap(contentResolver, local)
                    }
                }
                if (imagem != null){
                    civImageAvatar.setImageBitmap(imagem)
                }
            }catch (e: Exception){
                msgToast(this, getString(R.string.msg_error_carrega_arquivo))
            }
        }
    }

    private fun inscreva(usuario: Usuario) {
        if (validaCampos()) {
            if (verificaSenhas()){
                cadastroUsuario(usuario)
            }
        }
    }

    private fun cadastroUsuario(usuario: Usuario) {
        setProgressDialog(getString(R.string.text_autenticando))
        autenticacao = getFirebaseAuth()!!
        storageReference = getStorageReference()!!
        btnSingup.isEnabled = false
        autenticacao!!.createUserWithEmailAndPassword(usuario.email, usuario.senha).addOnCompleteListener {
            if (it.isSuccessful){

                try {
                    salvaImagemFirebase(usuario)
                }catch (e: Exception){
                    cadastroFalhou(e.message.toString())
                }

                try {
                    usuario.salvar()
                }catch (e: Exception){
                    cadastroFalhou(e.message.toString())
                }

                dialog.cancel()
                cadastroSucesso(getString(R.string.msg_sucesso_usuario_salvo))

            }else{
                var excecao = ""
                try {
                    throw it.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    excecao = getString(R.string.msg_error_senha_fraca)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    excecao = getString(R.string.msg_error_email_input)
                } catch (e: FirebaseAuthUserCollisionException) {
                    excecao = getString(R.string.msg_error_conta_existente)
                } catch (e: Exception) {
                    excecao = getString(R.string.msg_error_cadastro_usuario) + ": " + e.message
                    e.printStackTrace()
                }
                cadastroFalhou(excecao)
            }
        }
    }

    fun salvaImagemFirebase(usuario: Usuario){
        if (imagem != null) {
            val baos = ByteArrayOutputStream()
            imagem!!.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val dadosImagem = baos.toByteArray()

            //Salvar imagem no firebase
            val imagemRef = storageReference?.child("imagens")?.child("perfil")?.child(usuario.id + ".jpeg")

            val uploadTask = imagemRef!!.putBytes(dadosImagem)

//            uploadTask.addOnFailureListener {
//                msgToast(this, getString(R.string.msg_error_salvar_imagem))
//            }.addOnSuccessListener {
//                msgToast(this, getString(R.string.msg_sucesso_salvar_imagem))
//            }
        }
    }

    private fun cadastroFalhou(msg: String) {
        msgToast(this, msg)
        btnSingup.isEnabled = true
    }

    private fun cadastroSucesso(msg: String) {
        msgToast(this, msg)
        btnSingup.isEnabled = true
        finish()
    }


    private fun verificaSenhas(): Boolean {
        if (edtSenha.text.toString().isEmpty() && edtConfirmaSenha.text.toString().isEmpty()) {
            edtSenha.error = getString(R.string.msg_error_senha_fraca)
            return false
        } else if (edtSenha.text.length < 4 || edtConfirmaSenha.text.length < 4) {
            edtSenha.error = getString(R.string.msg_error_senha_fraca)
            return false
        } else if (edtSenha.text.toString() != edtConfirmaSenha.text.toString()) {
            edtConfirmaSenha.error = getString(R.string.msg_error_senha_diferentes)
            return false
        }
        return true
    }

    private fun validaCampos(): Boolean {
        var flag = true;

        if (edtNome.text.toString().isEmpty() || edtNome.text.toString().length < 4) {
            edtNome.error = getString(R.string.msg_error_campo_preenchido)
            flag = false
        } else {
            edtNome.error = null
        }

        if (edtSobrenome.text.toString().isEmpty() || edtSobrenome.text.toString().length < 4) {
            edtSobrenome.error = getString(R.string.msg_error_campo_preenchido)
            flag = false
        } else {
            edtSobrenome.error = null
        }

        if (edtEmail.text.toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches()) {
            edtEmail.error = getString(R.string.msg_error_email_input)
            flag = false
        } else {
            edtEmail.error = null
        }

        if (edtPeriodo.text.toString().isEmpty()) {
            edtPeriodo.error = getString(R.string.msg_error_campo_preenchido)
            flag = false
        } else {
            edtPeriodo.error = null
        }

        if (edtSenha.text.isEmpty()) {
            edtSenha.error = getString(R.string.msg_error_campo_preenchido)
            flag = false
        } else {
            edtPeriodo.error = null
        }

        return flag
    }

    private fun getUsuarioActivity(): Usuario {
        var periodo = 0
        if (edtPeriodo.text.toString() != "") {
            periodo = edtPeriodo.text.toString().toInt()
        }
        return Usuario(
            codificarBase64(edtEmail.text.toString()),
            edtNome.text.toString(),
            edtSobrenome.text.toString(),
            spnCurso.selectedItemPosition,
            periodo,
            edtEmail.text.toString(),
            edtSenha.text.toString()
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permissao in grantResults) {
            if (permissao == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao()
            }
        }
    }

    private fun alertaValidacaoPermissao() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.title_permissao))
        builder.setMessage(getString(R.string.msg_alerta_conceder_permisao_app))
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.text_confirmar) { dialog, which ->
            finish()
        }
        var dialog = builder.create()
        dialog.show()
    }

    private fun abreGaleria() {
        try {
            var i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (i.resolveActivity(packageManager) != null) {
                startActivityForResult(i, SELECAO_GALERIA)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            msgToast(this, getString(R.string.msg_erro_abre_galeria))
        }

    }

    private fun abreCamera() {
        try {
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (i.resolveActivity(packageManager) != null) {
                startActivityForResult(i, SELECAO_CAMERA)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            msgToast(this, getString(R.string.msg_erro_abre_camera))
        }

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
