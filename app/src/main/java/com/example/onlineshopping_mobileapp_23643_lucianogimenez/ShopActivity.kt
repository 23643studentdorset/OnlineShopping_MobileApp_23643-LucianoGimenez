package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import java.io.IOException

class ShopActivity: AppCompatActivity() {

    private lateinit var newRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        val testList = arrayOf<String>("cat", "dog", "pony")
        fetchJson("https://fakestoreapi.com/products/categories")

        newRecyclerView = findViewById(R.id.recyclerView_categories)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.adapter = AdapterCategories(testList, this)

    }
    fun fetchJson(url: String) {
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
                        println("users: $body")
                    }
                }
            })
        }
    }
}