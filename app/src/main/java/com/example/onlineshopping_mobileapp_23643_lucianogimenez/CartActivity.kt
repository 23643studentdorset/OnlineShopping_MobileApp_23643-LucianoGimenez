package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class CartActivity: AppCompatActivity() {

    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var quantityList: ArrayList<Int>
    private lateinit var productList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar?.title = "Cart Page"
        val sharedPreferences = getSharedPreferences(MY_APP_PREFERENCES, Context.MODE_PRIVATE)!!
        val sharedToken = sharedPreferences.getString("Token", "No Login")
        val sharedId = sharedPreferences.getInt("id", -1)
        //fetchProducts()

        recyclerViewCart = findViewById(R.id.recyclerView_cart)
        recyclerViewCart.layoutManager = LinearLayoutManager(this)

        fetchACartOfFrom(sharedId)

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

    private fun fetchACartOfFrom(userId: Int) {
        quantityList = arrayListOf()
        productList = arrayListOf()
        val urlFetchCarts = "https://fakestoreapi.com/carts/user/$userId"
        val client = OkHttpClient()
        val gson = GsonBuilder().create()
        val requestCarts = Request
            .Builder()
            .url(urlFetchCarts)
            .build()
        client.newCall(requestCarts).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("lucho", "$e")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val cartsList = gson.fromJson(body, Carts::class.java)
                println("cartsList[0]:${cartsList[0]}")
                var urlFetchProductInfo: String
                for (item in cartsList[0].products) {
                    quantityList.add(item.quantity)
                    urlFetchProductInfo = "https://fakestoreapi.com/products/${item.productId}"
                    val requestProduct = Request
                        .Builder()
                        .url(urlFetchProductInfo)
                        .build()
                    client.newCall(requestProduct).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.i("lucho", "$e")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val bodyFetchProduct = response.body?.string()
                            val product = gson.fromJson(bodyFetchProduct, Product::class.java)
                            productList.add(product)
                        }
                    })
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    recyclerViewCart.adapter = AdapterProductsCart(productList, quantityList)
                    findViewById<TextView>(R.id.loading).visibility = View.GONE
                    recyclerViewCart.visibility = View.VISIBLE}, 1000)
                /*
                runOnUiThread{
                    //println("productList2: $productList")
                    //println("quantityList2: $quantityList")
                    while (productList.size == 0) {
                        recyclerViewCart.adapter = AdapterProductsCart(productList, quantityList)
                    }
                }

                 */


            }
        })
    }
}


    /*
    private fun fetchProducts(){
        quantityList = arrayListOf()
        productList = arrayListOf()

        val url = "https://fakestoreapi.com/carts/5"
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
*/







