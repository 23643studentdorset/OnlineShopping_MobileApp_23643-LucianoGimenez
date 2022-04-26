package com.example.onlineshopping_mobileapp_23643_lucianogimenez

data class Cart(val id: Int, val userId: Int, val date: String, val products: ArrayList<ProductsCart>)

class ProductsCart(val productId: Int, val quantity: Int)