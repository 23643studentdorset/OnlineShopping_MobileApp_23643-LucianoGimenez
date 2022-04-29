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

class CartActivity: AppCompatActivity()  {

    private lateinit var recyclerViewCart : RecyclerView
    private lateinit var quantityList: ArrayList<Int>
    private lateinit var productList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar?.title = "Cart Page"
        fetchProducts()

        recyclerViewCart = findViewById(R.id.recyclerView_cart)
        recyclerViewCart.layoutManager = LinearLayoutManager(this)

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
        quantityList = arrayListOf()
        productList = arrayListOf()

        val url = "https://fakestoreapi.com/carts/6"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.run {
            newCall(request).enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("lucho", "$e")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        val body = response.body?.string()
                       // println("cart: $body")
                        var url2: String
                        val gson = GsonBuilder().create()
                        val cart = gson.fromJson(body, Cart::class.java)
                        for (item in cart.products){
                            //println(item.quantity)
                            //println(item.productId)
                            quantityList.add(item.quantity)
                            //println("quantityList: $quantityList")
                            url2 = "https://fakestoreapi.com/products/${item.productId}"
                            val requestProduct = Request.Builder().url(url2).build()
                            client.run{
                                newCall(requestProduct).enqueue(object :Callback{
                                    override fun onFailure(call: Call, e: IOException) {
                                        Log.i("lucho", "$e")
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        val body2 = response.body?.string()
                                        //println("body:$body2")
                                        val gson2 = GsonBuilder().create()
                                        val product = gson2.fromJson(body2, Product::class.java)
                                        //println("product: $product")
                                        productList.add(product)
                                        //println("productList: $productList")
                                    }
                                })
                            }
                        }

                    }
                    runOnUiThread {
                        //println("productList2: $productList")
                        //println("quantityList2: $quantityList")
                        while (productList.size == 0){
                            recyclerViewCart.adapter = AdapterProductsCart(productList, quantityList)
                        }
                    }
                }
            })
        }
    }
}








