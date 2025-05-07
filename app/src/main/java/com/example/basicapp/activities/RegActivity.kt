package com.example.basicapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.basicapp.UserDbHelper
import com.example.basicapp.R
import com.example.basicapp.User

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reg)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userEmail: EditText = findViewById(R.id.user_email)
        val userLogin: EditText = findViewById(R.id.user_login)
        val userPass: EditText = findViewById(R.id.user_pass)
        val submitButton: Button = findViewById(R.id.submit_button)
        val goToAuth: TextView = findViewById(R.id.go_to_auth)

        goToAuth.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }


        submitButton.setOnClickListener{

            val email = userEmail.text.toString().trim()
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()
            var errorMessage = ""

            if (email.isEmpty()){
                userEmail.setHintTextColor(Color.RED)
                errorMessage += "Поле 'E-mail' не может быть пустым.\n"
            } else userEmail.setHintTextColor(Color.parseColor("#6a6a6a"))

            if (login.isEmpty()){
                userLogin.setHintTextColor(Color.RED)
                errorMessage += "Поле 'Логин' не может быть пустым.\n"
            } else userLogin.setHintTextColor(Color.parseColor("#6a6a6a"))

            if (pass.isEmpty()){
                userPass.setHintTextColor(Color.RED)
                errorMessage += "Поле 'Пароль' не может быть пустым.\n"
            } else userPass.setHintTextColor(Color.parseColor("#6a6a6a"))

            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            } else {

                val user = User(email, login, pass)
                val db = UserDbHelper(this, null)

                if (db.getEmail(email)){
                    userEmail.setHintTextColor(Color.RED)
                    Toast.makeText(this, "Пользователь c таким email уже зарегистрирован", Toast.LENGTH_SHORT).show()
                } else {
                    db.addUser(user)
                    Toast.makeText(this, "Пользователь $login зарегистрирован!", Toast.LENGTH_SHORT)
                        .show()

                    userEmail.text.clear()
                    userLogin.text.clear()
                    userPass.text.clear()
                }
            }


        }
    }
}