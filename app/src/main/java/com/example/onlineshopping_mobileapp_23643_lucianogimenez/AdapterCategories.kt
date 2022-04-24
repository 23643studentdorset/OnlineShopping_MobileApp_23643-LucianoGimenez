package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterCategories (private val categoriesList: ArrayList<String>):RecyclerView.Adapter<CustomViewHolder>() {

    private lateinit var mListener : onItemClicklistener

    interface onItemClicklistener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:onItemClicklistener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.category_item, parent, false)
        return CustomViewHolder(cellForRow,mListener)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.category.text = categoriesList[position]
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

}

class CustomViewHolder(private val view: View, listener: AdapterCategories.onItemClicklistener):RecyclerView.ViewHolder(view){
    val category: Button = itemView.findViewById(R.id.category_filter)

    init{
        category.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

}