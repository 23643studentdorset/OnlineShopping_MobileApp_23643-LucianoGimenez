package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.sax.TextElementListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class AdapterProductsCart (private val productList: ArrayList<Product>, private val quantityList: ArrayList<Int> ): RecyclerView.Adapter<CustomViewHolderCart>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, operation : Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderCart {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.cart_item, parent, false)
        return CustomViewHolderCart(cellForRow, mListener)
    }

    override fun onBindViewHolder(holder: CustomViewHolderCart, position: Int) {
        val item = productList[position]
        val quantity = quantityList[position]
        holder.title.text = item.title
        holder.description.text = item.description
        holder.price.text = item.price
        holder.rate.text = item.rating.rate.toString()
        Picasso.get()
            .load(item.image)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(holder.image)
        holder.quantity.text = quantity.toString()
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}
class CustomViewHolderCart(private val view: View, listener: AdapterProductsCart.OnItemClickListener):RecyclerView.ViewHolder(view){
    val title: TextView = itemView.findViewById(R.id.title_cart_item)
    val description: TextView = itemView.findViewById(R.id.description_cart_item)
    val image: ImageView = itemView.findViewById(R.id.product_image_cart_image)
    val rate: TextView = itemView.findViewById(R.id.rate_cart_item)
    val price: TextView = itemView.findViewById(R.id.price_cart_item)
    val quantity: TextView = itemView.findViewById(R.id.quantity)
    val minusButton: ImageButton = itemView.findViewById(R.id.button_minus)
    val plusButton: ImageButton = itemView.findViewById(R.id.button_plus)

    init{
        minusButton.setOnClickListener {
            listener.onItemClick(adapterPosition, 1)
        }
        plusButton.setOnClickListener {
            listener.onItemClick(adapterPosition, 2)
        }
    }
}