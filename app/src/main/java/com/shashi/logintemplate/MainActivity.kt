package com.shashi.logintemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button_logout_main)
        button.setOnClickListener { logout() }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}