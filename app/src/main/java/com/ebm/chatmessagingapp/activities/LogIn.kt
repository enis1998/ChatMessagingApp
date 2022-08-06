package com.ebm.chatmessagingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ebm.chatmessagingapp.R
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if(email != "") {
                if(!email.contains("@", ignoreCase = true)){
                    Toast.makeText(this@LogIn, "Invalid email form!", Toast.LENGTH_SHORT).show()
                } else {
                    if(password != "") {
                        login(email,password)
                    } else {
                        Toast.makeText(this@LogIn, "Password area cannot be empty!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@LogIn, "Email area cannot be empty!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun login(email: String, password: String) {
        // login for logging user

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, code for logging in user
                    val intent = Intent(this@LogIn, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LogIn, "Email or Password is wrong!", Toast.LENGTH_SHORT).show()
                }
            }

    }
}