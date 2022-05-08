package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class OrdersActivity: AppCompatActivity(){

    private lateinit var recyclerViewOrders : RecyclerView
    private lateinit var productsList : ArrayList<Product>
    private lateinit var cartPriceList : ArrayList<Double>
    private lateinit var recyclerViewOrdersProduct : RecyclerView
    private lateinit var currentCart: Cart
    private lateinit var currentCartJson : String


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        supportActionBar?.title = "Orders Page"

        val sharedPreferences = getSharedPreferences(MY_APP_PREFERENCES, Context.MODE_PRIVATE)!!
        val sharedId = sharedPreferences.getInt("id", -1)


        recyclerViewOrders = findViewById(R.id.recyclerView_orders)
        recyclerViewOrders.layoutManager = LinearLayoutManager(this)

        recyclerViewOrdersProduct = findViewById(R.id.recyclerView_items_in_orders)
        recyclerViewOrdersProduct.layoutManager = LinearLayoutManager(this)

        fetchCarts(sharedId, sharedPreferences)

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
    @RequiresApi(Build.VERSION_CODES.O)
    private  fun fetchCarts(sharedId: Int, sharedPreferences: SharedPreferences) {
        cartPriceList = arrayListOf()
        currentCart  = Cart(-1, sharedId, LocalDateTime.now().toString(), mutableSetOf())
        val url = "https://fakestoreapi.com/carts/user/$sharedId"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        val gson = GsonBuilder().create()
        client.run {
            newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("lucho", "$e")
                }
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        val body = response.body?.string()
                        //println(body)
                        val cartsList = gson.fromJson(body, Carts::class.java)
                        //println(cartsList)
                        if (cartsList.size != 0){
                            for (item in cartsList) {
                                var singleCartPrice = 0.00
                                for (product in item.products){
                                    fetchProducts(product.productId)
                                }
                                Handler(Looper.getMainLooper()).postDelayed({
                                    for (product in productsList){
                                        println("product.price:${product.price}")
                                        singleCartPrice += product.price.toDouble()
                                    }
                                }, 2000)

                                Handler(Looper.getMainLooper()).postDelayed({
                                    println("singleCartPrice:$singleCartPrice")
                                    cartPriceList.add(singleCartPrice)
                                }, 3000)

                            }
                        }
                        Handler(Looper.getMainLooper()).postDelayed({
                            var adapter = AdapterOrders(cartsList, cartPriceList)
                            recyclerViewOrders.adapter = adapter
                            adapter.setOnItemClickListener(object: AdapterOrders.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val productAdapter = AdapterProductsShop(productsList)
                                    recyclerViewOrdersProduct.adapter = productAdapter
                                    productAdapter.setOnItemClickListener(object : AdapterProductsShop.onItemClicklistener{
                                        override fun onItemClick(position: Int) {
                                            currentCart.addProduct(productsList[position])
                                            currentCartJson = gson.toJson(currentCart)
                                            with(sharedPreferences.edit()) {
                                                putString(CURRENT_CART_KEY, currentCartJson)
                                                apply()
                                            }
                                        }
                                    })
                                }
                            })
                        }, 4000)
                    }
                }
            })
        }
    }
    private fun fetchProducts(productId:Int){
        productsList = arrayListOf()
        val url = "https://fakestoreapi.com/products/$productId"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        val gson = GsonBuilder().create()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("lucho", "$e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val product = gson.fromJson(body, Product::class.java)
                    productsList.add(product)
                }
            }
        })

    }

}