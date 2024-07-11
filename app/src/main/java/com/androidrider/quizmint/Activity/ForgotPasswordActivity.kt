package com.androidrider.quizmint.Activity

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var dialog: Dialog

    private val firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resetPassword()


    }


    private fun resetPassword() {
        binding.btnResetPassword.setOnClickListener {

            showDialog()

            val email = binding.edtEmail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter your email!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                return@setOnClickListener
            }

            // Reset password logic
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    dialog.dismiss()
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Check your email to reset your password.",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.edtEmail.text.clear()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to send reset email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }


    private fun showDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_loading)
        dialog.setCancelable(false)
        dialog.show()
    }


}
