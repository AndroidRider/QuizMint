package com.androidrider.quizmint.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androidrider.quizmint.Model.UserModel
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.ActivitySignUpScreenBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpScreen : AppCompatActivity() {

    lateinit var binding: ActivitySignUpScreenBinding
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, SignInScreen::class.java))
            finish()
        }

        signUp() // Calling Function
    }

    private fun showDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun signUp() {
        binding.btnSignUp.setOnClickListener {

            showDialog()

            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                return@setOnClickListener
            }

            Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = UserModel(name, email, password, "")
                    Firebase.database.reference.child("Users")
                        .child(Firebase.auth.currentUser!!.uid).setValue(user)
                        .addOnSuccessListener {
                            dialog.dismiss()
                            Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            dialog.dismiss()
                            Toast.makeText(this, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                        }

                    // Clear EditText fields after successful sign-up
                    binding.name.text.clear()
                    binding.email.text.clear()
                    binding.password.text.clear()

                } else {
                    dialog.dismiss()
                    Toast.makeText(this, "Sign Up failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
