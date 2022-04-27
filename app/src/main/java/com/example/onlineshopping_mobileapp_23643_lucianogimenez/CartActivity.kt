package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.annotation.SuppressLint
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

class CartActivity: AppCompatActivity()  {

    private lateinit var productsRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar?.title = "Cart Page"
        fetchProducts()


        findViewById<Button>(R.id.shop_button_cart).setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.orders_button_cart).setOnClickListener {
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.profile_button_cart).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
    private fun fetchProducts(){
        val url = "https://fakestoreapi.com/carts/5"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.run {
            newCall(request).enqueue(object :Callback{

                override fun onFailure(call: Call, e: IOException) {
                    Log.i("lucho", "$e")
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        //println("productsCart: $body")
                        val gson = GsonBuilder().create()
                        val cartList = gson.fromJson(body, Cart::class.java)
                        runOnUiThread {
                            val productListCart = ArrayList<Product>()
                            for (i in cartList.products.indices) {
                                val productId = cartList.products[i].productId
                                //println(productId)
                                val urlProduct ="https://fakestoreapi.com/products/$productId"
                                val requestProduct = Request.Builder().url(urlProduct).build()
                                client.run {
                                    newCall(requestProduct).enqueue(object : Callback{
                                        override fun onFailure(call: Call, e: IOException) {
                                            Log.i("lucho", "$e")
                                        }

                                        override fun onResponse(call: Call, response: Response) {
                                            if (response.isSuccessful) {
                                                val body2 = response.body?.string()
                                                //println("products: $body2")
                                                val productCart = gson.fromJson(body2, Product::class.java)
                                                runOnUiThread {
                                                    productListCart.add(productCart)
                                                }
                                            }
                                        }
                                    })
                                }
                                //println(cartList.products[i].quantity)
                            }
                            productsRecyclerView = findViewById(R.id.recyclerView_cart)
                            productsRecyclerView.layoutManager = LinearLayoutManager(this@CartActivity)
                            productsRecyclerView.adapter = AdapterProductsCart(productListCart, cartList.products)
                            println("Here")
                            println("productListCart: $productListCart")
                            println("cartProducts: $cartList.products")
                            //productsRecyclerView.adapter!!.notifyDataSetChanged()
                        }
                    }
                }
            })
        }
    }
}