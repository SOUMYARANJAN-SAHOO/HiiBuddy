package com.soumyaranjan.hiibuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.soumyaranjan.hiibuddy.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {

    lateinit var binding : ActivitySignUpBinding

    lateinit var mAuth : FirebaseAuth
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //initialising the firebase auth
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        //navigating to the sign up screen
        binding.btnSignUp.setOnClickListener {

            var name = binding.edtName.text.toString()
            var email = binding.edtEmail.text.toString()
            var password = binding.edtPassword.text.toString()

            makeSignUp(name, email, password)
        }
    }

    private fun makeSignUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //adding the user to the database
                   addToDatabase(name, email, mAuth.currentUser?.uid!!)

                    // Sign in success, update UI with the signed-in user's information
                    val toHome = Intent(this@SignUp, HomeScreen::class.java)
                    finish()
                    startActivity(toHome)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addToDatabase(name: String, email: String, uId: String){
        val user = User(name, email, uId)
        database.child("users").child(uId).setValue(user)
    }
}