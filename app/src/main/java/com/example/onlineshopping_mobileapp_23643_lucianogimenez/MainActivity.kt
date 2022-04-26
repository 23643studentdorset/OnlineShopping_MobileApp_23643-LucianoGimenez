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


class MainActivity : AppCompatActivity() {

    var usersList = ArrayList<User>()
    private lateinit var usersListGson: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Login Page"

        val bundle : Bundle ?= intent.extras
        val jsonNewUser = bundle?.getString(RegisterActivity.NEW_USER_KEY)
        //Log.i("lucho", "Pre if main $jsonNewUser")

        if (jsonNewUser != null){
            val gson = GsonBuilder().create()
            val newUser = gson.fromJson(jsonNewUser, User::class.java)
            //Log.i("lucho", "object $newUser")
            usersList.add(newUser)
        }

        val loginButtonClick = findViewById<Button>(R.id.login_login_button)
        loginButtonClick.setOnClickListener {
            if (findViewById<EditText>(R.id.user_name_login).text.toString() != "" &&
                findViewById<EditText>(R.id.password_login).text.toString() != ""
            ) {
                val username = findViewById<EditText>(R.id.user_name_login).text.toString()
                val password = findViewById<EditText>(R.id.password_login).text.toString()
                fetchJsonData(username, password)
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

    private fun fetchJsonData(username: String, password: String) {
        val url = "https://fakestoreapi.com/users"
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
                        usersListGson = gson.fromJson(body, Users::class.java)
                        //println(usersList)
                        usersList.addAll(usersListGson)
                        val userIndex = findIndexUsername(usersList, username)
                        //println("index:$userIndex")
                        runOnUiThread{if (userIndex != null){
                            if (usersList[userIndex].password == password){
                                //println("You are in")
                                Toast.makeText(this@MainActivity,"You are in",Toast.LENGTH_LONG).show()
                                val intentShop = Intent(this@MainActivity, ShopActivity::class.java)
                                startActivity(intentShop)
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
