package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var usersList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Login Page"

        val loginButtonClick = findViewById<Button>(R.id.login_login_button)
        loginButtonClick.setOnClickListener {
            if (findViewById<EditText>(R.id.user_name_login).text.toString() != "" &&
                findViewById<EditText>(R.id.password_login).text.toString() != ""
            ) {
                val username = findViewById<EditText>(R.id.user_name_login).text.toString()
                val password = findViewById<EditText>(R.id.password_login).text.toString()
                fetchJsonData(username, password, "https://fakestoreapi.com/users")
            } else {
                Toast.makeText(
                    this,
                    "You have to write a username and/or password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val registerButtonClick = findViewById<Button>(R.id.register_button_login)
        registerButtonClick.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchJsonData(username: String, password: String, url: String) {
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.run {
            newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    Log.i("lucho", "$e")

                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        //println("users: $body")
                        val gson = GsonBuilder().create()
                        usersList = gson.fromJson(body, Users::class.java)
                        //println(usersList)
                        val userIndex = findIndexUsername(usersList, username)
                        //println("index:$userIndex")
                        runOnUiThread{if (userIndex != null){
                            if (usersList[userIndex].password == password){
                                //println("You are in")
                                Toast.makeText(this@MainActivity,"You are in",Toast.LENGTH_LONG).show()
                            }}
                        }
                    }
                }
            })
        }
    }
    private fun findIndexUsername(arr: ArrayList<User>, item: String): Int? {
        return (arr.indices)
            .firstOrNull { i: Int -> item == arr[i].username }
    }
}