package com.androidrider.quizmint.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.androidrider.quizmint.Model.UserModel
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.ActivitySignInScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignInScreen : AppCompatActivity() {

    lateinit var binding: ActivitySignInScreenBinding
    private lateinit var dialog: Dialog

    private lateinit var email:String
    private lateinit var password:String

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpScreen::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        signIn() // Calling Function
    }


    /***************************** New Code ****************************/


    private fun signIn() {

        binding.btnSignIn.setOnClickListener {
            showDialog()

            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                return@setOnClickListener
            }

            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val verification = auth.currentUser?.isEmailVerified
                        if (verification == true) {
                            dialog.dismiss()
                            updateUI()
                            updatePassword()
                        } else {
                            Toast.makeText(this, "please verify your email", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        dialog.dismiss()
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthInvalidUserException -> "User not found. Please sign up."
                            is FirebaseAuthInvalidCredentialsException -> "Invalid credentials. Please try again."
                            else -> "Sign In failed: ${task.exception?.localizedMessage}"
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun updatePassword(){

        val updatePassword=password

        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid).child("password").setValue(updatePassword)
            .addOnSuccessListener {
                Toast.makeText(this,"Data saved!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                dialog.dismiss()
                Toast.makeText(this,"Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI(){
        Toast.makeText(this, "Sign In Successful!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    private fun showDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_loading)
        dialog.setCancelable(false)
        dialog.show()
    }

}
