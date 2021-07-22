package com.example.college_storespace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      auth = FirebaseAuth.getInstance()
        login()
        regtxt.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
            finish()
        }

        btngo.setOnClickListener {
            startActivity(Intent(this@MainActivity, VideoUploadActivity::class.java))
        }
        homebtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, MyRecyclerViewActivity::class.java))
        }
        btnload.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoadActivity::class.java))
        }
    }
    private fun login(){

        lgnbtn.setOnClickListener {


            if (TextUtils.isEmpty(lgneml.text.toString())) {
                lgneml.setError("Please enter yout Email")
                return@setOnClickListener
            }
            else if (TextUtils.isEmpty(lgnpaswd.text.toString())){
            lgnpaswd.setError("Please enter your Password")
            return@setOnClickListener
        }
            auth.signInWithEmailAndPassword(lgneml.text.toString(),lgnpaswd.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@MainActivity,VideoUploadActivity::class.java))
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Login failed try again", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}