package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class AdapterProductsShop (private val productList: ArrayList<Product>): RecyclerView.Adapter<CustomViewHolderProduct>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderProduct {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.product_item, parent, false)
        return CustomViewHolderProduct(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolderProduct, position: Int) {
        val item = productList[position]
        holder.title.text = item.title
        holder.description.text = item.description
        holder.price.text = item.price
        holder.rate.text = item.rating.rate.toString()
        Picasso.get()
            .load(item.image)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}

class CustomViewHolderProduct (private val view: View):RecyclerView.ViewHolder(view){
    val title: TextView = itemView.findViewById(R.id.title_product_item)
    val description: TextView = itemView.findViewById(R.id.description_product_item)
    val price: TextView = itemView.findViewById(R.id.price_product_item)
    val rate:TextView = itemView.findViewById(R.id.rate_product_item)
    val image: ImageView = itemView.findViewById(R.id.product_image_product_item)
}


