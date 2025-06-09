package com.example.map_final_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReservationAdapter(
    private val items: MutableList<Reservation>,
    private val ress: List<Restaurant>,
    private val onItemClick: (Reservation, Restaurant) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.image_thumbnail)
        val name: TextView = view.findViewById(R.id.res_name)
        val time: TextView = view.findViewById(R.id.res_time)
        val people: TextView = view.findViewById(R.id.res_people)

        fun bind(reservation: Reservation, restaurant: Restaurant, onClick: (Reservation, Restaurant) -> Unit) {
            itemView.setOnClickListener {
                onClick(reservation, restaurant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.reservation_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservation = items[position]
        val res_id = reservation.restaurant_id
        var restaurant: Restaurant = ress[0]

        for (res in ress) {
            if (res.id == res_id) {
                restaurant = res
                break
            }
        }


        holder.name.text = restaurant.name
        holder.time.text = "${reservation.date} ${reservation.time}"
        holder.people.text = "People: ${reservation.people}"


        val resId = holder.img.context.resources.getIdentifier(
            restaurant.image, "drawable", holder.img.context.packageName
        )
        if (resId != 0) {
            holder.img.setImageResource(resId)
        }

        holder.bind(reservation, restaurant, onItemClick)
    }
}
