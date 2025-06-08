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

        // 加载所有用户
        val userList = loadUserInfo()
        val allRestaurants = loadResInfo()

        // 根据 ID 找到当前用户
        user = userList.find { it.id == userId } ?: run {
            Toast.makeText(this, "Can\' find user info", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 设置用户信息文本
        val userInfoText = findViewById<TextView>(R.id.text_user_info)
        userInfoText.text = "${user.id}: ${user.info.name} (${user.info.age}/${user.info.gender})"

        // 设置预约列表
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

        // 设置“预约”按钮 → 跳转到 Activity 2
        val reserveBtn = findViewById<Button>(R.id.button_reserve)
        reserveBtn.setOnClickListener {
            //Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RestaurantListActivity::class.java)
            intent.putExtra("user_id", user.id)
            startActivity(intent)
        }
    }

    private fun loadResInfo(): List<Restaurant> {
        val inputStream = assets.open("restaurant_info.txt")
        val reader = InputStreamReader(inputStream)
        val gson = Gson()
        val type = object : TypeToken<List<Restaurant>>() {}.type
        return gson.fromJson(reader, type)
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
}
