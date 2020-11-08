package com.shashi.logintemplate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shashi.logintemplate.databinding.ActivityLoginBinding
import com.shashi.logintemplate.login.SigninFragment
import com.shashi.logintemplate.login.SignupFragment


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private var isSignupClicked = false

    lateinit var buttonSignup: Button

    lateinit var textViewHeader: TextView
    lateinit var textViewFooter: TextView

    lateinit var signInButton: SignInButton

    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 100

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        val toolBar: Toolbar = binding.toolbarLogin
        setSupportActionBar(toolBar)

        initViews()

        //Setting default login method to signin
        buttonClicked()
    }

    private fun initViews() {

        buttonSignup = binding.buttonSignupLogin
        buttonSignup.setOnClickListener { buttonClicked() }

        signInButton = binding.googleSigninLogin
        signInButton.setOnClickListener { googleSignInClicked() }

        textViewHeader = binding.textViewHeaderLogin
        textViewFooter = binding.textViewFooterLogin

    }

    private fun buttonClicked() {
        changeViewText()
        showFragment(isSignupClicked)
    }

    private fun showFragment(signupClicked: Boolean) {
        val fragment: Fragment

        if (signupClicked) {
            isSignupClicked = false
            fragment = SignupFragment()
        } else {
            isSignupClicked = true
            fragment = SigninFragment()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout_login, fragment)
            .commit()
    }

    private fun changeViewText() {
        if (isSignupClicked) {
            textViewHeader.text = getString(R.string.signup)
            textViewFooter.text = getString(R.string.already_have_an_account)
            buttonSignup.text = getString(R.string.signin)
        } else {
            textViewHeader.text = getString(R.string.signin)
            textViewFooter.text = getString(R.string.don_t_have_an_account_yet)
            buttonSignup.text = getString(R.string.signup)
        }
    }

    private fun googleSignInClicked() {
        processRequest()
        createIntent()
    }

    private fun processRequest() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun createIntent() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Error. Please try again", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    startActivity(Intent(this@LoginActivity, GoogleSignInActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Firebase Error. Please try again", Toast.LENGTH_SHORT)
                        .show()
                }

            }
    }

}