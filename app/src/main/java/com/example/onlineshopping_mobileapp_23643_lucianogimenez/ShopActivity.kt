package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.time.LocalDateTime




class ShopActivity: AppCompatActivity() {


    private lateinit var categoriesRecyclerView : RecyclerView
    private lateinit var productsRecyclerView : RecyclerView
    private lateinit var currentCart: Cart
    private lateinit var currentCartJson : String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        supportActionBar?.title = "Shop Page"

        val sharedPreferences = getSharedPreferences(MY_APP_PREFERENCES, Context.MODE_PRIVATE)!!
        val sharedId = sharedPreferences.getInt("id", -1)
        val currentCartShared = sharedPreferences.getString(CURRENT_CART_KEY, null)

        if (currentCartShared != null){
            currentCartJson = currentCartShared
        }

        categoriesRecyclerView = findViewById(R.id.recyclerView_categories)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchCategories(sharedId, currentCartShared)

        findViewById<Button>(R.id.cart_button_shop).setOnClickListener {
            with(sharedPreferences.edit()) {
                putString(CURRENT_CART_KEY, currentCartJson)
                apply()
            }
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.orders_button_shop).setOnClickListener {
            with(sharedPreferences.edit()) {
                putString(CURRENT_CART_KEY, currentCartJson)
                apply()
            }
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.profile_button_shop).setOnClickListener {
            with(sharedPreferences.edit()) {
                putString(CURRENT_CART_KEY, currentCartJson)
                apply()
            }
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

    }
    private fun fetchCategories(sharedId : Int, currentCartShared : String?) {
        //val url = "https://fakestoreapi.com/products/categories"
        val url = "https://raw.githubusercontent.com/23643studentdorset/TuesdayLesson2/master/sample2.json"
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
                            val categoriesAdapter = AdapterCategories(categoriesList)
                            categoriesRecyclerView.adapter = categoriesAdapter
                            categoriesAdapter.setOnItemClickListener(object : AdapterCategories.onItemClicklistener {
                                @RequiresApi(Build.VERSION_CODES.O)
                                override fun onItemClick(position: Int) {
                                    //println(categoriesList[position])
                                    fetchProducts(categoriesList[position], sharedId, currentCartShared)
                                }
                            })
                        }
                    }
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchProducts (category: String, sharedId : Int, currentCartShared: String?){
        currentCart  = Cart(-1, sharedId, LocalDateTime.now().toString(), mutableSetOf())
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
                        //println("products: $body")
                        val gson = GsonBuilder().create()
                        val productsList = gson.fromJson(body, Products::class.java)
                        //println(productsList)
                        runOnUiThread {
                            productsRecyclerView = findViewById(R.id.recyclerView_products)
                            productsRecyclerView.layoutManager = LinearLayoutManager(this@ShopActivity)
                            val productAdapter = AdapterProductsShop(productsList)
                            productsRecyclerView.adapter = productAdapter
                            productAdapter.setOnItemClickListener(object : AdapterProductsShop.onItemClicklistener{
                                override fun onItemClick(position: Int) {
                                    println(productsList[position].title)
                                    println("currentCartShared: $currentCartShared")
                                    if (currentCartShared == null) {
                                        currentCart.addProduct(productsList[position])
                                        currentCartJson = gson.toJson(currentCart)
                                    }else{
                                        currentCart = gson.fromJson(currentCartShared, Cart::class.java)
                                        currentCart.addProduct(productsList[position])
                                        println("here")
                                        currentCartJson = gson.toJson(currentCart)

                                    }
                                }
                            })
                        }
                    }
                }
            })
        }
    }
}