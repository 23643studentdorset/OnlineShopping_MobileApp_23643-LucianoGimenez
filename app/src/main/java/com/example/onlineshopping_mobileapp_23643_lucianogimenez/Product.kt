package com.example.onlineshopping_mobileapp_23643_lucianogimenez

data class Product(val id: Int, val title: String, val price: String, val description: String, val image: String, val rating: Rate)

class Rate (val rate: Double)
