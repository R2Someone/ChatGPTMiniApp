package com.example.chatgptmini

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val responseView: TextView = findViewById(R.id.responseText)
        responseView.text = "Welcome to ChatGPT Mini App!"
    }
}
