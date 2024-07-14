package com.androidrider.quizmint.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.androidrider.quizmint.Model.UserModel
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.ActivitySignUpScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpScreen : AppCompatActivity() {

    lateinit var binding: ActivitySignUpScreenBinding
    private lateinit var dialog: Dialog

    private lateinit var auth: FirebaseAuth
    private lateinit var name:String
    private lateinit var email:String
    private lateinit var password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, SignInScreen::class.java))
            finish()
        }

        signUp() // Calling Function
    }


    /***************************** New Code ****************************/
    private fun signUp() {
        binding.btnSignUp.setOnClickListener {

            showDialog()
            name = binding.name.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        //EmailVerification
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show()
                                saveData()
                                openEmailApp() // Open email app after sending verification email
                                dialog.dismiss()
                            }
                            ?.addOnFailureListener {
                                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                            }

                        // Clear EditText fields after successful sign-up
                        binding.name.text.clear()
                        binding.email.text.clear()
                        binding.password.text.clear()

                    } else {
                        dialog.dismiss()
                        Toast.makeText(this,"Sign Up failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun openEmailApp() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveData(){

        val user = UserModel(name, email, password, "")

        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser!!.uid).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"Data saved!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                dialog.dismiss()
                Toast.makeText(this,"Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun showDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    /*No need this onStart doing its work*/
//    fun updateUI(){
//        Toast.makeText(this,"Sign Up Successful!",Toast.LENGTH_SHORT).show()
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }


}
