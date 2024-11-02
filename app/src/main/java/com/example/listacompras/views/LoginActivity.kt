package com.example.listacompras.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listacompras.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater);
    }

    private val auth by lazy{
        FirebaseAuth.getInstance();
    }

    private val db by lazy{
        FirebaseFirestore.getInstance();
    }

    public var recover : Map<String,Any>? = null;
    public var user : String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(binding.root);

        recoverUserName(auth.currentUser?.uid.toString());

        binding.loginBotao.setOnClickListener {

            val email = binding.userEmailRecover.text.toString();
            val password = binding.userPasswordRecover.text.toString();

            auth.signInWithEmailAndPassword(email,password)

                .addOnSuccessListener {

                    Handler().postDelayed({
                        Toast.makeText(this, "Welcome ${user} !!", Toast.LENGTH_SHORT).show();

                    },500)


                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this,MainActivity::class.java);
                        startActivity(intent);
                        finish();
                    },2500)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro, please check email and password", Toast.LENGTH_SHORT).show()
                }
        }

        binding.siginBotao.setOnClickListener {
            var i = Intent(this,SignInActivity::class.java);
            startActivity(i);
        }
    }

    fun recoverUserName(userId : String)
    {

            db.collection(userId.toString()).document("DadosUser").get()
                .addOnSuccessListener {

                    recover = it.getData();

                    recover?.forEach { entry ->


                    if(entry.key.toString() == "nome")
                    {
                        user = entry.value.toString();
                    }
                    }

                }
    }
}