package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class OrdersActivity: AppCompatActivity(){

    private lateinit var recyclerViewOrders : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        supportActionBar?.title = "Orders Page"

        recyclerViewOrders = findViewById(R.id.recyclerView_orders)
        recyclerViewOrders.layoutManager = LinearLayoutManager(this)

        fetchCarts()

        findViewById<Button>(R.id.shop_button_orders).setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.cart_button_orders).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.profile_button_orders).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
    private  fun fetchCarts(){
        val url = "https://fakestoreapi.com/carts/user/1"
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
                        val cartsList = gson.fromJson(body, Carts::class.java)
                        //println(cartsList)
                        runOnUiThread {
                            recyclerViewOrders.adapter = AdapterOrders(cartsList)
                        }
                    }
                }
            })
        }
    }

}