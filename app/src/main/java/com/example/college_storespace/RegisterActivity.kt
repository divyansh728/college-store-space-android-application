package com.example.college_storespace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        register()
    }

    private fun register(){
        regbtn.setOnClickListener {
            if (TextUtils.isEmpty(regeml.text.toString())){
                regeml.setError("Please enter your Email")
                return@setOnClickListener
            }else if (TextUtils.isEmpty(regpaswd.text.toString())){
                regpaswd.setError("Please enter your Password")
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(regeml.text.toString(),regpaswd.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val currentUser = auth.currentUser
                        Toast.makeText(this@RegisterActivity, "Registration is Successful", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this@RegisterActivity, "Registration failed try again", Toast.LENGTH_LONG).show()
                    }
                }

        }
    }
}