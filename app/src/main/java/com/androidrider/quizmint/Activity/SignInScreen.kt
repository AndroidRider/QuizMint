package com.androidrider.quizmint.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.ActivitySignInScreenBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInScreen : AppCompatActivity() {

    lateinit var binding: ActivitySignInScreenBinding
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpScreen::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        signIn() // Calling Function
    }

    private fun showDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_loading)
        dialog.setCancelable(false)
        dialog.show()
    }


    private fun signIn() {
        binding.btnSignIn.setOnClickListener {

            showDialog()
            if (binding.email.text.toString().isEmpty() ||
                binding.password.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Firebase.auth.signInWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        dialog.dismiss()
                        Toast.makeText(this, "SignIn Successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        // Clear EditText fields
                        binding.email.text.clear()
                        binding.password.text.clear()
                    } else {
                        dialog.dismiss()
                        Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
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
