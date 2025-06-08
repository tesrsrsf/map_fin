package com.example.map_final_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RestaurantDetailAdapter
    private lateinit var menuList: List<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restaurant_detail)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        val userId = intent.getStringExtra("user_id") ?: ""
        val json_restaurant = intent.getStringExtra("restaurant")
        val restaurant = Gson().fromJson(json_restaurant, Restaurant::class.java)


        var name = findViewById<TextView>(R.id.name)
        var type = findViewById<TextView>(R.id.type)
        var location = findViewById<TextView>(R.id.location)
        var rates = findViewById<TextView>(R.id.rates)
        var descriptions = findViewById<TextView>(R.id.descriptions)
        var opening = findViewById<TextView>(R.id.opening)
        var logo = findViewById<ImageView>(R.id.thumbnail)

        name.text = restaurant.name
        type.text = "\t" + restaurant.type
        location.text = "\t" + restaurant.location
        rates.text = "\t" + restaurant.rating
        descriptions.text = "\t" + restaurant.description
        opening.text = "\t${restaurant.openingHours.open} ~ ${restaurant.openingHours.close}"


        val imageResId = resources.getIdentifier(restaurant.image, "drawable", packageName)
        logo.setImageResource(imageResId)

        recyclerView = findViewById(R.id.Menus)
        recyclerView.layoutManager = LinearLayoutManager(this)

        menuList = loadMenuInfo(restaurant)

        val reserveBtn = findViewById<Button>(R.id.detail_reserve_btn)
        reserveBtn.setOnClickListener {
            //Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ReservationInfoActivity::class.java)
            intent.putExtra("user_id", userId)
            intent.putExtra("restaurant", Gson().toJson(restaurant))
            startActivity(intent)
        }

        adapter = RestaurantDetailAdapter(menuList)
        recyclerView.adapter = adapter
    }


    private fun loadMenuInfo(restaurant: Restaurant): List<MenuItem> {
        return restaurant.menu
    }


}