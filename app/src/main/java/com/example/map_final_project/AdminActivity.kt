package com.example.map_final_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.InputStreamReader

class AdminActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminAdapter

    private var lastDefImg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recyclerView = findViewById(R.id.admin_rest_list)
        recyclerView.layoutManager = LinearLayoutManager(this)


        var restaurantList = loadResInfo()
        adapter = AdminAdapter(restaurantList) { selectedRestaurant ->
            val intent = Intent(this, AdminEditActivity::class.java)
            intent.putExtra("restaurant", Gson().toJson(selectedRestaurant))
            startActivity(intent)
        }
        recyclerView.adapter = adapter


        findViewById<Button>(R.id.admin_btn_add).setOnClickListener {
            val file = File(filesDir, "restaurant_info.txt")
            val gson = Gson()
            val listType = object : TypeToken<MutableList<Restaurant>>() {}.type
            val restaurantList: MutableList<Restaurant> = if (file.exists()) {
                gson.fromJson(file.readText(), listType)
            } else {
                gson.fromJson(assets.open("restaurant_info.txt").bufferedReader().use { it.readText() }, listType)
            }

            val newId = (restaurantList.maxOfOrNull { it.id } ?: 0) + 1
            val newRestaurant = Restaurant(
                id = newId,
                name = "",
                type = "",
                location = "",
                rating = "",
                image = generateRandLogo(),
                description = "",
                openingHours = OpeningHours("00:00", "00:00"),
                menu = mutableListOf()
            )

            val intent = Intent(this, AdminEditActivity::class.java)
            intent.putExtra("restaurant", Gson().toJson(newRestaurant))
            intent.putExtra("isNew", true)
            startActivity(intent)
        }

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

    private fun loadUserInfo(): MutableList<UserInfo> {
        val file = File(filesDir, "user_info.txt")
        val jsonStr = if (file.exists()) {
            file.bufferedReader().use { it.readText() }
        } else {
            assets.open("user_info.txt").bufferedReader().use { it.readText() }
        }

        val type = object : TypeToken<List<UserInfo>>() {}.type
        return Gson().fromJson(jsonStr, type)
    }

    override fun onResume() {
        super.onResume()
        val updatedList = loadResInfo()
        adapter = AdminAdapter(updatedList) { selectedRestaurant ->
            val intent = Intent(this, AdminEditActivity::class.java)
            intent.putExtra("restaurant", Gson().toJson(selectedRestaurant))
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }


    fun generateRandLogo(): String {
        val logos = listOf("rand_logo_1", "rand_logo_2", "rand_logo_3", "rand_logo_4", "rand_logo_5", "rand_logo_6", "rand_logo_7", "rand_logo_8", "rand_logo_9", "rand_logo_10", "rand_logo_11")
        var res = logos.random()
        while (res == lastDefImg) {
            res = logos.random()
        }

        return res
    }

}