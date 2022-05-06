package com.example.onlineshopping_mobileapp_23643_lucianogimenez

class Cart(val id: Int, val userId: Int, val date: String, val products: MutableSet<ProductsCart>){

    fun addProduct (newProduct: Product){
        val cartProduct = ProductsCart(newProduct.id, 1)
        this.products.add(cartProduct)
    }
}

class ProductsCart(val productId: Int, val quantity: Int)