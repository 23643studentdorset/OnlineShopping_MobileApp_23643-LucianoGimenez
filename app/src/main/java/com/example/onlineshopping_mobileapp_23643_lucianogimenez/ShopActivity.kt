package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class ShopActivity: AppCompatActivity() {

    private lateinit var testList: ArrayList<String>
    private lateinit var categoriesRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        //testList = arrayListOf<String>("cat", "dog", "pony")


        categoriesRecyclerView = findViewById(R.id.recyclerView_categories)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(this)


        fetchCategories()

    }
    private fun fetchCategories() {
        val url = "https://fakestoreapi.com/products/categories"
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
                        //println("categories: $body")
                        val gson = GsonBuilder().create()
                        val categoriesList = gson.fromJson(body, Categories::class.java)
                        //println(testList2[0])
                        runOnUiThread {
                            var categoriesAdapter = AdapterCategories(categoriesList)
                            categoriesRecyclerView.adapter = categoriesAdapter
                            categoriesAdapter.setOnItemClickListener(object : AdapterCategories.onItemClicklistener {
                                override fun onItemClick(position: Int) {
                                    println(categoriesList[position])
                                    fetchProducts(categoriesList[position])
                                }
                            })
                        }
                    }
                }
            })
        }
    }

    private fun fetchProducts (category: String){
        val url = "https://fakestoreapi.com/products/category/$category"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.run{
            newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("lucho", "$e")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        val body = response.body?.string()
                        println("products: $body")
                    }
                }
            })
        }
    }



}