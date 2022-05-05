package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder


class RegisterActivity: AppCompatActivity() {

    companion object{
        const val NEW_USER_KEY = "new_user"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.title = "Register Page"

        val registerButtonClick = findViewById<Button>(R.id.register_button_register)
        registerButtonClick.setOnClickListener{
            val username = findViewById<EditText>(R.id.user_name_register).text.toString()
            val email = findViewById<EditText>(R.id.email_register).text.toString()
            val password =findViewById<EditText>(R.id.password_register).text.toString()
            val phone = findViewById<EditText>(R.id.phone_num_register).text.toString()

            val geolocation = Geolocation("-456235465","56465456465")
            val name = Name(findViewById<EditText>(R.id.first_name_register).text.toString(), findViewById<EditText>(R.id.last_name_register).text.toString())
            val address = Address(findViewById<EditText>(R.id.city_register).text.toString(), findViewById<EditText>(R.id.street_register).text.toString(),
                Integer.parseInt(findViewById<EditText>(R.id.number_register).text.toString()), findViewById<EditText>(R.id.postal_code_register).text.toString(),
                geolocation)

            val newUser = User (email, username, password, name, address, phone, 40)
            val gson = GsonBuilder().create()
            val jsonNewUser = gson.toJson(newUser)
            //println(jsonNewUser)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(NEW_USER_KEY, jsonNewUser)
            //Log.i("lucho", "register: $jsonNewUser")
            startActivity(intent)
        }
    }

}



