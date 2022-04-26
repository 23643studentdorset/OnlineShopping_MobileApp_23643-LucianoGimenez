package com.example.onlineshopping_mobileapp_23643_lucianogimenez


data class User( val email:String, val username: String, val password: String,
                 val name: Name, val address: Address, val phone: String )

data class Address( val city: String, val street: String, val number: Int,
                   val zipcode: String, val geolocation: Geolocation )

data class Geolocation ( val lat: String, val long: String )

data class Name( val firstname: String, val lastname: String )




