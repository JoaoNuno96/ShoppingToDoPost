package com.example.listacompras.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listacompras.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivitySignInBinding.inflate(layoutInflater);
    }

    private val auth by lazy{
        FirebaseAuth.getInstance();
    }

    private val db by lazy{
        FirebaseFirestore.getInstance();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(binding.root);

        binding.sign.setOnClickListener {

            val email = binding.userEmailRecover.text.toString();
            val password = binding.userPasswordRecover.text.toString();

            auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener {

                    saveUser(email,password);

                    Toast.makeText(this, "Success! User Registered!", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                              val intent = Intent(this,LoginActivity::class.java);
                              startActivity(intent);
                              finish();
                          },2500)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error, please check your email and password", Toast.LENGTH_SHORT).show()
                }

        }
    }

    fun saveUser(email : String,password : String)
    {
        val uid = auth.currentUser?.uid;

        val data = mapOf(
            "email" to email,
            "password" to password
        )

        db.collection(uid.toString())
            .document("DadosUser")
            .set(data)
            .addOnSuccessListener{

            }
            .addOnFailureListener {

            }

    }



}