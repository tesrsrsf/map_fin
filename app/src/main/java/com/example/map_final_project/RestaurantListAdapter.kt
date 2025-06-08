package com.example.map_final_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RestaurantListAdapter(
    private val restaurantList: List<Restaurant>,
    private val onItemClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.restaurantImage)
        val name: TextView = view.findViewById(R.id.restaurantName)
        val location: TextView = view.findViewById(R.id.restaurantLocation)
        val rating: TextView = view.findViewById(R.id.restaurantRating)
        val hours: TextView = view.findViewById(R.id.restaurantHours)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = restaurantList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.name.text = restaurant.name
        holder.location.text = "\t${restaurant.location}"
        holder.rating.text = "\t${restaurant.rating}"
        holder.hours.text = "\t${restaurant.openingHours.open} ~ ${restaurant.openingHours.close}"

        val context = holder.image.context
        val imageResId = context.resources.getIdentifier(restaurant.image, "drawable", context.packageName)
        holder.image.setImageResource(imageResId)

        holder.itemView.setOnClickListener { onItemClick(restaurant) }
    }
}
