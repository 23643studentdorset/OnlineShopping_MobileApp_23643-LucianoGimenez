package com.example.onlineshopping_mobileapp_23643_lucianogimenez

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView


class AdapterCategories (private val categoriesList: ArrayList<String>):RecyclerView.Adapter<CustomViewHolderCategory>() {

    private lateinit var mListener : onItemClicklistener

    interface onItemClicklistener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:onItemClicklistener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderCategory {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.category_item, parent, false)
        return CustomViewHolderCategory(cellForRow,mListener)
    }

    override fun onBindViewHolder(holder: CustomViewHolderCategory, position: Int) {
        holder.category.text = categoriesList[position]
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

}

class CustomViewHolderCategory(private val view: View, listener: AdapterCategories.onItemClicklistener):RecyclerView.ViewHolder(view){
    val category: Button = itemView.findViewById(R.id.category_filter)

    init{
        category.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

}