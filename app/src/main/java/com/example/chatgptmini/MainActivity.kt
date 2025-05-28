package com.example.chatgptmini

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputText = findViewById<EditText>(R.id.inputText)
        val sendButton = findViewById<Button>(R.id.sendButton)
        val responseText = findViewById<TextView>(R.id.responseText)

        sendButton.setOnClickListener {
            val userInput = inputText.text.toString()
            sendToChatGPT(userInput) { response ->
                runOnUiThread {
                    responseText.text = response
                }
            }
        }
    }

    private fun sendToChatGPT(prompt: String, callback: (String) -> Unit) {
        val apiKey = "YOUR_OPENAI_API_KEY"
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = RequestBody.create(mediaType, """{
          "model": "gpt-3.5-turbo",
          "messages": [{"role": "user", "content": "$prompt"}]
        }""")

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Request failed: " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        callback("Unexpected code: ${response.code}")
                    } else {
                        val json = JSONObject(it.body?.string())
                        val result = json.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content")
                        callback(result)
                    }
                }
            }
        })
    }
}
