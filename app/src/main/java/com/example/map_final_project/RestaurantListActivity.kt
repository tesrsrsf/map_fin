package com.example.map_final_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.InputStreamReader

class RestaurantListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RestaurantListAdapter
    private lateinit var restaurantList: List<Restaurant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        val userId = intent.getStringExtra("user_id") ?: ""

        recyclerView = findViewById(R.id.restaurantRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        restaurantList = loadResInfo()
        adapter = RestaurantListAdapter(restaurantList) { selectedRestaurant ->
            //Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RestaurantDetailActivity::class.java)
            intent.putExtra("restaurant", Gson().toJson(selectedRestaurant))
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }

    private fun loadResInfo(): MutableList<Restaurant> {
        val file = File(filesDir, "restaurant_info.txt")
        val jsonStr = if (file.exists()) {
            file.bufferedReader().use { it.readText() }
        } else {
            assets.open("restaurant_info.txt").bufferedReader().use { it.readText() }
        }

        val type = object : TypeToken<List<Restaurant>>() {}.type
        return Gson().fromJson(jsonStr, type)
    }
}
