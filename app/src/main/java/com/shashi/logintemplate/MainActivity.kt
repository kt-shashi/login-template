package com.shashi.logintemplate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    lateinit var firebaseFirestore: FirebaseFirestore
    val COLLECTION_NAME = "users"

    lateinit var textInputLayoutName: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseFirestore = FirebaseFirestore.getInstance()

        initViews()
        checkIfDataAvaiable()
    }

    private fun initViews() {

        textInputLayoutName = findViewById(R.id.text_input_layout_display_name_main)

        val button = findViewById<Button>(R.id.button_logout_main)
        button.setOnClickListener { logout() }

    }

    private fun checkIfDataAvaiable() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            firebaseFirestore.collection(COLLECTION_NAME)
                .document(user.uid)
                .get()
                .addOnSuccessListener { documentSnapshot -> //Check if the document exists
                    if (documentSnapshot.exists()) {

                        var userName = documentSnapshot.getString("name")

                        if (userName!!.isEmpty()) {
                            userName = ""
                        }

                        showData(userName)

                    } else {
                        startActivity(Intent(this, ProfileActivity::class.java))
                        finish()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun showData(userName: String) {
        textInputLayoutName.editText!!.setText(userName)
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}