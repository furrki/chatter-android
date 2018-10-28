package com.chatter.furrki.chatter

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.parse.*
import com.parse.ParseUser
import com.parse.LogInCallback

class MainActivity : AppCompatActivity() {
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var register: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = ParseUser.getCurrentUser()
        if(user != null && user.isAuthenticated){
            val intent = Intent(this,HomeActivity::class.java)
            intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }

        username = findViewById(R.id.loginUsername)
        password = findViewById(R.id.loginPassword)
        register = findViewById(R.id.registerText)


        val button = findViewById<Button>(R.id.loginBtn)
        button.setOnClickListener{
            ParseUser.logInInBackground(username.text.toString(), password.text.toString()) { user, e ->
                if (user != null) {
                    val intent = Intent(this,HomeActivity::class.java)
                    intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
                    startActivity(intent)
                } else {
                    e.printStackTrace()
                }
            }
        }

        register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)

            startActivity(intent)
        }

    }
}
fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
