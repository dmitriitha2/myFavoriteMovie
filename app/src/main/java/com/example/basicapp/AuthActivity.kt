package com.example.basicapp

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

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)
        val submitButton: Button = findViewById(R.id.submit_button_auth)

        val goToReg: TextView = findViewById(R.id.go_to_reg)

        goToReg.setOnClickListener {
            val intent = Intent(this, RegActivity::class.java)
            startActivity(intent)
            finish()
        }

        submitButton.setOnClickListener{

            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()
            var errorMessage = ""

            if (login.isEmpty()){
                userLogin.setHintTextColor(Color.RED)
                errorMessage += "Поле 'Логин' не может быть пустым.\n"
            } else userLogin.setHintTextColor(Color.parseColor("#6a6a6a"))

            if (pass.isEmpty()){
                userPass.setHintTextColor(Color.RED)
                errorMessage += "Поле 'Пароль' не может быть пустым.\n"
            }
            else userPass.setHintTextColor(Color.parseColor("#6a6a6a"))

            if (errorMessage.isNotEmpty())
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            else {
                val db = DbHelper(this, null)
                try {
                    val isAuth = db.getUser(login, pass)

                    if (isAuth) {
                        Toast.makeText(this, "Успешно авторизовано", Toast.LENGTH_SHORT).show()
                        userLogin.text.clear()
                        userPass.text.clear()
                        val intent = Intent(this, MoviesActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else
                        Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                }

            }



        }

    }
}