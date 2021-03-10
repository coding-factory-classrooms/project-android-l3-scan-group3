package com.example.scanfood.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.scanfood.R
import com.example.scanfood.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.loginButton.setOnClickListener{
            if(validLogin(binding.usernameEditText.text.toString(), binding.passwordEditText.text.toString())) {
                Toast.makeText(this@LoginActivity,
                    " Login OK ! Navigation vers la liste de films",
                    Toast.LENGTH_SHORT).show()
              //  val intent  = Intent(this, nomDeLaClasse)
              //  startActivity(intent)
            }else {
                Toast.makeText(this@LoginActivity,
                    " Utilisateur / mot de passe incorrect",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validLogin(username: String, password: String): Boolean =
        username == "kotlin" && password == "rocks"

}