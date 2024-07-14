package com.androidrider.quizmint.Activity

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var dialog: Dialog
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnResetPassword.setOnClickListener {
            resetPassword()
        }

    }


    /***************************** New Code ****************************/


    private fun resetPassword(){

        val email = binding.edtEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter your email!", Toast.LENGTH_SHORT).show()
            return
        }
        showDialog()

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(this, "please check your email!", Toast.LENGTH_SHORT).show()
                binding.tvCheckEmailMessage.visibility=View.VISIBLE
                binding.edtEmail.text.clear()
                binding.tvCheckEmailMessage.text = "Check your Email, Password rest email sent to '$email', successfully! ."
                dialog.dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_loading)
        dialog.setCancelable(false)
        dialog.show()
    }

}
