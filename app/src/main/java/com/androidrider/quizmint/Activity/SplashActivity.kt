package com.androidrider.quizmint.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.androidrider.quizmint.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)




        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SignInScreen::class.java)
            startActivity(intent)
            finish()
        },500)



    }

}