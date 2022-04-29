package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import okhttp3.*
import java.io.IOException

class ProfileActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = "Profile Page"

        fetchUser()


        findViewById<Button>(R.id.shop_button_profile).setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.cart_button_profile).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.orders_button_profile).setOnClickListener {
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchUser(){
        val url = "https://fakestoreapi.com/users/5"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.run {
            newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("lucho", "$e")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        val body = response.body?.string()
                        println(body)
                        val gson = GsonBuilder().create()
                        val user = gson.fromJson(body, User::class.java)
                        //println(cartsList)
                        runOnUiThread {
                            val avatarPhoto = findViewById<ImageView>(R.id.photo)
                            Picasso.get()
                                .load("https://thispersondoesnotexist.com/image")
                                .into(avatarPhoto)
                            findViewById<TextView>(R.id.user_name_profile).text = user.username
                            findViewById<TextView>(R.id.first_name_profile).text = user.name.firstname.replaceFirstChar { it.uppercase() }
                            findViewById<TextView>(R.id.last_name_profile).text = user.name.lastname.replaceFirstChar { it.uppercase() }
                            findViewById<TextView>(R.id.email_profile).text = user.email
                            findViewById<TextView>(R.id.phone_profile).text = user.phone
                            findViewById<TextView>(R.id.city_profile).text = user.address.city.replaceFirstChar { it.uppercase() }
                            findViewById<TextView>(R.id.street_profile).text = user.address.street.replaceFirstChar { it.uppercase() }
                            findViewById<TextView>(R.id.number_profile).text = user.address.number.toString()
                            findViewById<TextView>(R.id.zip_code).text = user.address.zipcode
                        }
                    }
                }
            })
        }
    }
}
