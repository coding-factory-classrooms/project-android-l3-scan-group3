package com.example.scanfood.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.example.scanfood.R
import com.example.scanfood.databinding.ActivityLoginBinding
import androidx.lifecycle.ViewModel
import androidx.activity.viewModels


private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    private val model: LoginViewModel by viewModels()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.getState().observe(this, Observer { updateUI(it!!) })

        model.updateLogin(
            binding.usernameEditText.text.toString(),
            binding.passwordEditText.text.toString()
        )


        binding.loginButton.setOnClickListener {
            model.login(binding.usernameEditText.text.toString(), binding.passwordEditText.text.toString())
        }
        binding.usernameEditText.doAfterTextChanged { text ->
            model.updateLogin(
                binding.usernameEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
        binding.passwordEditText.doAfterTextChanged { text ->
            model.updateLogin(
                binding.usernameEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
    }

    private fun updateUI(state: LoginViewModelState) {
        Log.i(TAG, "updateUI : state=$state")
        when (state) {
            is LoginViewModelState.Failure -> {
                binding.loginButton.isEnabled = state.loginButtonEnabled
                Toast.makeText(this@LoginActivity, "Login NOK", Toast.LENGTH_SHORT).show()
            }
            is LoginViewModelState.Loading -> {
                binding.loginButton.isEnabled = state.loginButtonEnabled
                Toast.makeText(this@LoginActivity, "Loading..", Toast.LENGTH_SHORT).show()
            }
            LoginViewModelState.Success -> {
                binding.loginButton.isEnabled = state.loginButtonEnabled
                Toast.makeText(this@LoginActivity, "Login OK", Toast.LENGTH_SHORT).show()
                navigateToMovieList()
            }
            is LoginViewModelState.UpdateLogin -> {
                binding.loginButton.isEnabled = state.loginButtonEnabled
            }
        }
    }

    private fun navigateToMovieList() {
      /*  val intent = Intent(this, MovieListActivity::class.java)
        startActivity(intent)
        finish()*/
    }
}