package com.example.map_final_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RestaurantDetailAdapter(
    private val menuList: List<MenuItem>
) : RecyclerView.Adapter<RestaurantDetailAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuImage: ImageView = view.findViewById(R.id.menuImage)
        val menuName: TextView = view.findViewById(R.id.menuName)
        val menuPrice: TextView = view.findViewById(R.id.menuPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = menuList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.menuName.text = menuItem.name
        holder.menuPrice.text = "${menuItem.price} $"

        val context = holder.menuImage.context
        val imageResId = context.resources.getIdentifier(menuItem.image, "drawable", context.packageName)
        holder.menuImage.setImageResource(imageResId)
    }
}
