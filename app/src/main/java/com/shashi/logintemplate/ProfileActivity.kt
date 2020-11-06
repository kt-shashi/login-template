package com.shashi.logintemplate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseFirestore: FirebaseFirestore
    private val COLLECTION_NAME = "users"

    lateinit var buttonSave: Button
    lateinit var textInputLayoutName: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseFirestore = FirebaseFirestore.getInstance()

        initViews()
    }

    private fun initViews() {
        buttonSave = findViewById(R.id.button_save_profile)
        textInputLayoutName = findViewById(R.id.text_input_layout_display_name_profile)

        buttonSave.setOnClickListener { saveClicked() }
    }

    private fun saveClicked() {

        val userName = textInputLayoutName.editText?.text.toString()
        if (!isNameValid(userName)) {
            return
        }

        saveUsernameInFirestore(userName)

    }

    private fun saveUsernameInFirestore(userName: String) {
        val userId: String = FirebaseAuth.getInstance().currentUser!!.uid

        val documentReference = firebaseFirestore
            .collection(COLLECTION_NAME)
            .document(userId)

        val userData: MutableMap<String, Any> = HashMap()
        userData["name"] = userName

        documentReference.set(userData)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    private fun isNameValid(userName: String): Boolean {
        if (userName.isEmpty()) {
            textInputLayoutName.error = "Cannot be empty"
            return false
        } else {
            textInputLayoutName.error = null
        }
        return true
    }

}