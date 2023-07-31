package com.soumyaranjan.hiibuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.soumyaranjan.hiibuddy.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding

    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //initialising the firebase auth
        mAuth = FirebaseAuth.getInstance()

        //navigating to the sign up screen
        binding.btnSignUp.setOnClickListener {
            val toSignUp = Intent(this@Login, SignUp::class.java)
            startActivity(toSignUp)
        }
        
        binding.btnLogin.setOnClickListener { 
            var email = binding.edtEmail.text.toString()
            var password = binding.edtPassword.text.toString()
            
            makeLogin(email, password)
        }
    }

    private fun makeLogin(email : String, password : String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val toHome = Intent(this@Login, HomeScreen::class.java)
                    finish()
                    startActivity(toHome)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Incorrect Credential!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}