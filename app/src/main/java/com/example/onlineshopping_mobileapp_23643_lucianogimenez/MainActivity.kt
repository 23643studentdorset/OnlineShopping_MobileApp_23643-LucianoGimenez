package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class MainActivity : AppCompatActivity() {

    var usersList = ArrayList<User>()
    private lateinit var usersListGson: ArrayList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Login Page"
        val sharedPreferences = getSharedPreferences("MY_APP_PREFERENCES",Context.MODE_PRIVATE)!!
        val intentShop = Intent(this, ShopActivity::class.java)

        val sharedToken = sharedPreferences.getString("Token", "No Login")
        val sharedId = sharedPreferences.getInt("id", -1)
        if (sharedToken != null && sharedId != -1){
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
            println("sharedId:$sharedId , sharedToken:$sharedToken")
            startActivity(intentShop)
        }

        val bundle: Bundle? = intent.extras
        val jsonNewUser = bundle?.getString(RegisterActivity.NEW_USER_KEY)
        //Log.i("lucho", "Pre if main $jsonNewUser")

        if (jsonNewUser != null) {
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
                LogIn(username, password, sharedPreferences, intentShop)
            } else {
                Toast.makeText(this, "You have to write a username and/or password", Toast.LENGTH_SHORT).show()
            }
        }

        val registerButtonClick = findViewById<Button>(R.id.register_button_login)
        registerButtonClick.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun LogIn(username: String, password: String, sharedPreferences: SharedPreferences, intentShop: Intent) {
        val urlUsers = "https://fakestoreapi.com/users"
        val urlLogIn = "https://fakestoreapi.com/auth/login"
        val client = OkHttpClient()
        val gson = GsonBuilder().create()

        /*  Get request for users and store ID */
        val requestUsers = Request
            .Builder()
            .url(urlUsers)
            .build()

        client.run {
            newCall(requestUsers).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    Log.i("lucho", "$e")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        //println("users: $body")
                        usersListGson = gson.fromJson(body, Users::class.java)
                        //println(usersList)
                        usersList.addAll(usersListGson)
                        val userIndex = findIndexUsername(usersList, username)
                        //println("index:$userIndex")
                        runOnUiThread {
                            if (userIndex != null) {
                                val userid = usersList[userIndex].id
                                with(sharedPreferences.edit()) {
                                    putInt("id", userid)
                                    apply()
                                }
                            }
                        }
                    }
                }
            })
        }

        /*  Post request to get and store token  */
        val userLogin = UserLogin(username, password)
        val jsonLoginUser = gson.toJson(userLogin)
        //println(jsonLoginUser)
        val request = Request.Builder()
            .url(urlLogIn)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .post(jsonLoginUser.toRequestBody())
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                println("error: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //println("success:${response.code},  $body")
                if ((body != "username or password is incorrect")) {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "status: ${response.code}",
                            Toast.LENGTH_LONG
                        ).show()
                        val tokenObj = gson.fromJson(body, Token::class.java)
                        //println(tokenObj.token)
                        with(sharedPreferences.edit()) {
                            putString("Token", tokenObj.token)
                            apply()
                        }
                        startActivity(intentShop)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Username or Password is incorrect",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    private fun findIndexUsername(arr: ArrayList<User>, item: String): Int? {
        return (arr.indices)
            .firstOrNull { i: Int -> item == arr[i].username }
    }
}


