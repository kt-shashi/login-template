package com.shashi.logintemplate

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : AppCompatActivity() {

    lateinit var firebaseFirestore: FirebaseFirestore
    private val COLLECTION_NAME = "users"

    lateinit var circleImageView: CircleImageView

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

        circleImageView = findViewById(R.id.display_image_main)

        val button = findViewById<Button>(R.id.button_logout_main)
        button.setOnClickListener { logout() }

    }

    private fun checkIfDataAvaiable() {
        val userId = getUserID()

        firebaseFirestore.collection(COLLECTION_NAME)
            .document(userId)
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

    private fun getUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    private fun showData(userName: String) {
        textInputLayoutName.editText!!.setText(userName)
        showProfileImage()
    }

    private fun showProfileImage() {
        val userId = getUserID()

        val storageReference =
            FirebaseStorage.getInstance().getReference("profile_pictures/$userId.jpg")

        val ONE_MEGABYTE = 1024 * 1024.toLong()
        storageReference.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener {

                val bm = BitmapFactory.decodeByteArray(it, 0, it.size)
                val dm = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(dm)
                circleImageView.minimumHeight = dm.heightPixels
                circleImageView.minimumWidth = dm.widthPixels
                circleImageView.setImageBitmap(bm)

            }.addOnFailureListener {
                // Handle any errors
                Toast.makeText(this, "Could not load Image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}