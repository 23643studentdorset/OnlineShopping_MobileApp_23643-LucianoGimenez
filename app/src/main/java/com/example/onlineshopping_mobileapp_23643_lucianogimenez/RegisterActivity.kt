package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.random.Random


class RegisterActivity: AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.title = "Register Page"

        val bundle: Bundle? = intent.extras
        val lastUser = bundle!!.getInt(LAST_USER_ID_KEY)
        val intentMainActivity = Intent(this, MainActivity::class.java)

        val registerButtonClick = findViewById<Button>(R.id.register_button_register)
        registerButtonClick.setOnClickListener{
            val username = findViewById<EditText>(R.id.user_name_register).text.toString()
            val email = findViewById<EditText>(R.id.email_register).text.toString()
            val password =findViewById<EditText>(R.id.password_register).text.toString()
            val phone = findViewById<EditText>(R.id.phone_num_register).text.toString()



            val geolocation = Geolocation(Random.nextDouble(-90.00, 90.00).toString()
                                        ,Random.nextDouble(-180.00, 180.00).toString())
            val name = Name(findViewById<EditText>(R.id.first_name_register).text.toString(), findViewById<EditText>(R.id.last_name_register).text.toString())
            val address = Address(findViewById<EditText>(R.id.city_register).text.toString(), findViewById<EditText>(R.id.street_register).text.toString(),
                Integer.parseInt(findViewById<EditText>(R.id.number_register).text.toString()), findViewById<EditText>(R.id.postal_code_register).text.toString(),
                geolocation)
            val newUser = User (email, username, password, name, address, phone, lastUser)
            val gson = GsonBuilder().create()
            val jsonNewUser = gson.toJson(newUser)
            //println("jsonNewUser: $jsonNewUser")
            postNewUser(jsonNewUser)
            intentMainActivity.putExtra(NEW_USER_KEY, jsonNewUser)
            //Log.i("lucho", "register: $jsonNewUser")
            Handler(Looper.getMainLooper()).postDelayed(
                {startActivity(intentMainActivity)},10000)
        }
    }
    private fun postNewUser(jsonNewUser: String){
        val url = "https://fakestoreapi.com/users"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .post(jsonNewUser.toRequestBody())
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                println("error: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                println("response:${response.body?.string()}")
                if (response.code == 200){
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "User Created",Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}



