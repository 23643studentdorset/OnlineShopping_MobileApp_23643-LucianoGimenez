package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class CartActivity: AppCompatActivity() {

    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var quantityList: ArrayList<Int>
    private lateinit var productList: ArrayList<Product>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar?.title = "Cart Page"
        val sharedPreferences = getSharedPreferences(MY_APP_PREFERENCES, Context.MODE_PRIVATE)!!
        val sharedToken = sharedPreferences.getString("Token", "No Login")
        val sharedId = sharedPreferences.getInt("id", -1)
        val jsonCurrentCart = sharedPreferences.getString(CURRENT_CART_KEY, null)


        recyclerViewCart = findViewById(R.id.recyclerView_cart)
        recyclerViewCart.layoutManager = LinearLayoutManager(this)

        fetchACartOfFrom(sharedId, jsonCurrentCart)

        findViewById<Button>(R.id.button_checkout).setOnClickListener {
            if (jsonCurrentCart != null){
                postACart(jsonCurrentCart,sharedToken!!, sharedPreferences)
            }

        }

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

    private fun fetchACartOfFrom(userId: Int, jsonCurrentCart : String? ) {
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
                //println("cartsList[0]:${cartsList[0]}")
                var urlFetchProductInfo: String
                //println(jsonCurrentCart)
                if (jsonCurrentCart != null){
                    val currentCart = gson.fromJson(jsonCurrentCart, Cart::class.java)
                    for (item in currentCart.products) {
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
                                println("Here")
                            }
                        })
                    }
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    val adapter = AdapterProductsCart(productList, quantityList)
                    recyclerViewCart.adapter = adapter
                    adapter.setOnItemClickListener(object : AdapterProductsCart.OnItemClickListener{

                        override fun onItemClick(position: Int, operation: Int ) {
                            println("position:$position operation:$operation")
                            if (operation == 1){
                                quantityList[position] -= 1
                                println("quantity:${quantityList[position]}")
                            }else{
                                quantityList[position] += 1
                                println("quantity:${quantityList[position]}")
                            }
                            adapter.notifyItemChanged(position)
                        }

                    })

                    findViewById<TextView>(R.id.loading).visibility = View.GONE
                    recyclerViewCart.visibility = View.VISIBLE}, 10000)
            }
        })
    }

    private fun postACart(cart: String, token: String, sharedPreferences : SharedPreferences){
        val urlPostCart = "https://fakestoreapi.com/carts"
        val client = OkHttpClient()
        val requestPostCart = Request
            .Builder()
            .url(urlPostCart)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Authorization","Bearer $token")
            .post(cart.toRequestBody())
            .build()
        client.newCall(requestPostCart).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.i("lucho", "$e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200){
                    sharedPreferences.edit().remove(CURRENT_CART_KEY).apply()
                    runOnUiThread{
                        Toast.makeText(this@CartActivity,"Order Sent", Toast.LENGTH_LONG).show()
                        recyclerViewCart.visibility = View.GONE
                        quantityList.clear()
                        productList.clear()
                    }


                }
            }
        })
    }
}








