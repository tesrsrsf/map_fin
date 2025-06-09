package com.example.map_final_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import androidx.recyclerview.widget.RecyclerView

class AdminAdapter(
    private val restaurantList: List<Restaurant>,
    private val onItemClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<AdminAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.restaurantImage)
        val name: TextView = itemView.findViewById(R.id.restaurantName)
        val location: TextView = itemView.findViewById(R.id.restaurantLocation)
        val rating: TextView = itemView.findViewById(R.id.restaurantRating)
        val hours: TextView = itemView.findViewById(R.id.restaurantHours)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = restaurantList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.name.text = restaurant.name
        holder.location.text = restaurant.location
        holder.rating.text = restaurant.rating
        holder.hours.text = "${restaurant.openingHours.open} ~ ${restaurant.openingHours.close}"


        val context = holder.image.context
        val imageResId = context.resources.getIdentifier(
            restaurant.image, "drawable", context.packageName
        )
        if (imageResId != 0) {
            holder.image.setImageResource(imageResId)
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.itemView.setOnClickListener {
            onItemClick(restaurant)
        }
    }
}