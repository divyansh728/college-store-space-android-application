package com.example.college_storespace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_initial.*

class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        crdstdmt.setOnClickListener {
            val intent = Intent(this, CrdviewActivity::class.java)
            startActivity(intent)
        }
        crddoubt.setOnClickListener {
            val intent = Intent(this, AskDoubtActivity::class.java)
            startActivity(intent)
        }














    }

}