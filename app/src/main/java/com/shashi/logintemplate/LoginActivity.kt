package com.shashi.logintemplate

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.shashi.logintemplate.databinding.ActivityLoginBinding
import com.shashi.logintemplate.login.SigninFragment
import com.shashi.logintemplate.login.SignupFragment

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private var isSignupClicked = false

    lateinit var buttonSignup: Button

    lateinit var textViewHeader: TextView
    lateinit var textViewFooter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolBar: Toolbar = binding.toolbarLogin
        setSupportActionBar(toolBar)

        initViews()

        //Setting default login method to signin
        buttonClicked()
    }

    private fun initViews() {

        buttonSignup = binding.buttonSignupLogin
        buttonSignup.setOnClickListener { buttonClicked() }

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

}