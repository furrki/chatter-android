package com.chatter.furrki.chatter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.parse.ParseUser


class RegisterActivity : AppCompatActivity() {

    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var repassword: EditText
    lateinit var email: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        email = findViewById(R.id.email)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        repassword = findViewById(R.id.rePassword)

        val btn = findViewById<Button>(R.id.regBtn)
        btn.setOnClickListener {
            if(password.text.toString() == repassword.text.toString()) {
                val user = ParseUser()
                user.username = username.text.toString()
                user.setPassword(password.text.toString())
                user.email = email.text.toString()

                user.signUpInBackground { e ->
                    if (e == null) {
                        toast("Registered!")
                        val intent = Intent(this,MainActivity::class.java)
                        intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
                        startActivity(intent)
                    } else {
                        toast("An error occured.")
                    }
                }
            } else {
                toast("Passwords dont match!")
            }
        }

    }

}
