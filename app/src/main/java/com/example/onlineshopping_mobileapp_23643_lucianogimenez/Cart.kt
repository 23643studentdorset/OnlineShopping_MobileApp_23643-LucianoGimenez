package com.example.onlineshopping_mobileapp_23643_lucianogimenez

class Cart(val id: Int, val userId: Int, val date: String, val products: ArrayList<ProductsCart>){

    fun addProduct (newProduct: Product){
        this.products.add(ProductsCart(newProduct.id, 1))
    }
}

class ProductsCart(val productId: Int, val quantity: Int)