package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonClick = findViewById<Button>(R.id.login_login_button)
        buttonClick.setOnClickListener {
            if (findViewById<EditText>(R.id.user_name_login).text.toString() != "" &&
                findViewById<EditText>(R.id.password_login).text.toString() != ""
            ) {
                val username = findViewById<EditText>(R.id.user_name_login).text.toString()
                val password = findViewById<EditText>(R.id.password_login).text.toString()
                fetchJsonData(username!!, password!!)
            } else {
                Toast.makeText(
                    this,
                    "You have to write a username and/or password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun fetchJsonData(username: String, password: String) {
        val url = "https://fakestoreapi.com/users/1"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //println (body)
                val gson = GsonBuilder().create()
                val userData = gson.fromJson(body, User::class.java)
                println(userData)
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.i("lucho", "$e")
            }

        })
    }
}