package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterOrders (private val orderList: ArrayList<Cart>): RecyclerView.Adapter<CustomViewHolderOrders>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderOrders {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.orders_item, parent, false)
        return CustomViewHolderOrders(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolderOrders, position: Int) {
        val item = orderList[position]
        holder.orderNumber.text = item.id.toString()
        holder.priceOrders.text = 56.toString()
        holder.status.text = "Delivered"
        holder.date.text = item.date.slice(0..9)

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}

class CustomViewHolderOrders(private val view: View):RecyclerView.ViewHolder(view){
    val orderNumber: TextView = itemView.findViewById(R.id.order_number_orders_item)
    val priceOrders: TextView = itemView.findViewById(R.id.price_orders_item)
    val status: TextView = itemView.findViewById(R.id.status_order_item)
    val date: TextView = itemView.findViewById(R.id.date_order_item)
}
