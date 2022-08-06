package com.ebm.chatmessagingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ebm.chatmessagingapp.R
import com.ebm.chatmessagingapp.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {


    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if(!isLetters(name)) {
                Toast.makeText(this@SignUp, "Name must include only letters!", Toast.LENGTH_SHORT).show()
            } else {
                if(name != "") {
                    if(email != "") {
                        if(!email.contains("@", ignoreCase = true)){
                            Toast.makeText(this@SignUp, "Invalid email form!", Toast.LENGTH_SHORT).show()
                        } else {
                            if(password != "") {
                                if(password.length < 6) {
                                    Toast.makeText(this@SignUp, "Password must be longer that 6 character!", Toast.LENGTH_SHORT).show()
                                } else {
                                    signUp(name, email, password)
                                }
                            } else {
                                Toast.makeText(this@SignUp, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@SignUp, "Email cannot be empty!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignUp, "Name cannot be empty!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun isLetters(string: String): Boolean {
        for (c in string)
        {
            if (c !in 'A'..'Z' && c !in 'a'..'z' && c != ' ') {
                return false
            }
        }
        return true
    }

    private fun signUp(name: String, email: String, password: String) {
        // logic of creating user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for jumping to Home
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignUp, "Some error occurred!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}