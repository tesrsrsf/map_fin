package com.example.map_final_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.security.PrivateKey

class AdminMenuAdapter(
    private val menuList: MutableList<MenuItem>,
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<AdminMenuAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.menuName)
        val price: TextView = itemView.findViewById(R.id.menuPrice)
        val image: ImageView = itemView.findViewById(R.id.menuImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menuList[position]
        holder.name.text = menu.name
        holder.price.text = menu.price.toString() + " $"

        val context = holder.image.context
        val imageResId = context.resources.getIdentifier(menu.image, "drawable", context.packageName)
        holder.image.setImageResource(imageResId)

        holder.itemView.setOnClickListener { onItemClick(menu) }
    }
}
