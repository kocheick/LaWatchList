package com.example.lawatchlist.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lawatchlist.R
import com.example.lawatchlist.databinding.ActivityLoginBinding
import com.example.lawatchlist.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class LoginActivity : AppCompatActivity() {

    private var isLoggedIn = false
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.loginButton.setOnClickListener {
            logIn()

        }
    }

    private fun logIn() {
        with(binding){
            val uName = usernameEditText.text.toString()
            val uPwd = passwordEditText.text.toString()

            if (uName.count() > 1 && uPwd.count() > 3) {
                isLoggedIn = true

                getSharedPreferences("state", Context.MODE_PRIVATE).edit()
                    .putBoolean("loggedStatus", isLoggedIn).apply()

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()


            } else Snackbar.make(loginContainer,
                R.string.login_denied, Snackbar.LENGTH_SHORT).show()
        }


    }


    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("state", Context.MODE_PRIVATE) ?: return
        isLoggedIn = sharedPreferences.getBoolean("loggedStatus", isLoggedIn)
        if (isLoggedIn) startActivity(Intent(this, MainActivity::class.java))
    }

}