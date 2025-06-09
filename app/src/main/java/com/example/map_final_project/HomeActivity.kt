package com.example.map_final_project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.InputStreamReader

class HomeActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var user: UserInfo
    private lateinit var reservationAdapter: ReservationAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userId = intent.getStringExtra("user_id") ?: ""


        val userList = loadUserInfo()
        val allRestaurants = loadResInfo()


        user = userList.find { it.id == userId } ?: run {
            Toast.makeText(this, "Cannot find user info", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        val userInfoText = findViewById<TextView>(R.id.text_user_info)
        userInfoText.text = "${user.id}: ${user.info.name} (${user.info.age}/${user.info.gender})"


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_reservation_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        reservationAdapter = ReservationAdapter(user.reserved, allRestaurants) { reservation, restaurant ->
            val intent = Intent(this, ReservationFinActivity::class.java)
            intent.putExtra("user_id", user.id)
            intent.putExtra("reservation_id", reservation.reservation_id)
            intent.putExtra("restaurant", Gson().toJson(restaurant))
            intent.putExtra("people", reservation.people.toString())
            intent.putExtra("date", reservation.date)
            intent.putExtra("time", reservation.time)
            startActivity(intent)
        }
        recyclerView.adapter = reservationAdapter


        val reserveBtn = findViewById<Button>(R.id.button_reserve)
        reserveBtn.setOnClickListener {
            //Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RestaurantListActivity::class.java)
            intent.putExtra("user_id", user.id)
            startActivity(intent)
        }


        val luckyBtn = findViewById<Button>(R.id.btn_lucky)
        luckyBtn.setOnClickListener {
            val recommended = getRecommendedRestaurant(user, allRestaurants, userList)
            val intent = Intent(this, RestaurantDetailActivity::class.java)
            intent.putExtra("restaurant", Gson().toJson(recommended))
            intent.putExtra("user_id", user.id)
            startActivity(intent)
            Toast.makeText(this, "You think you are lucky?", Toast.LENGTH_SHORT).show()

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
        /*
        val inputStream = assets.open("user_info.txt")
        val reader = InputStreamReader(inputStream)
        val gson = Gson()
        val type = object : TypeToken<List<UserInfo>>() {}.type
        return gson.fromJson(reader, type)
        */


        val file = File(filesDir, "user_info.txt")
        val jsonStr = if (file.exists()) {
            file.bufferedReader().use { it.readText() }
        } else {
            assets.open("user_info.txt").bufferedReader().use { it.readText() }
        }

        val type = object : TypeToken<List<UserInfo>>() {}.type
        return Gson().fromJson(jsonStr, type)
    }

    private fun getRecommendedRestaurant(
        user: UserInfo,
        allRestaurants: List<Restaurant>,
        allUsers: List<UserInfo>
    ): Restaurant? {
        val reservationCount = mutableMapOf<Int, Int>()
        for (restaurant in allRestaurants) {
            reservationCount[restaurant.id] = 0
        }
        for (u in allUsers) {
            for (r in u.reserved) {
                reservationCount[r.restaurant_id] = (reservationCount[r.restaurant_id] ?: 0) + 1
            }
        }

        val minCount = reservationCount.values.minOrNull() ?: return null
        val leastReserved = reservationCount.filter { it.value == minCount }.keys
        val candidatesLeast = allRestaurants.filter { it.id in leastReserved }
        val candidatesOther = allRestaurants.filter { it.id !in leastReserved }


        val pickLeast = Math.random() < 0.5
        return if (pickLeast && candidatesLeast.isNotEmpty()) {
            candidatesLeast.random()
        } else {
            if (candidatesOther.isNotEmpty()) {
                candidatesOther.random()
            } else {
                candidatesLeast.randomOrNull()
            }
        }
    }




}
