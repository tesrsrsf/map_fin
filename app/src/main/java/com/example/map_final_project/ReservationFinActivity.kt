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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class ReservationFinActivity : AppCompatActivity() {

    private lateinit var userId: String
    private var reservationId: Int = -1
    private lateinit var restaurant: Restaurant
    private lateinit var people: String
    private lateinit var date: String
    private lateinit var time: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_fin)

        userId = intent.getStringExtra("user_id") ?: ""
        reservationId = intent.getIntExtra("reservation_id", -1)
        restaurant = Gson().fromJson(intent.getStringExtra("restaurant"), Restaurant::class.java)
        people = intent.getStringExtra("people") ?: "-"
        date = intent.getStringExtra("date") ?: "-"
        time = intent.getStringExtra("time") ?: "-"

        findViewById<TextView>(R.id.rest_name).text = restaurant.name
        findViewById<TextView>(R.id.final_people).text = people
        findViewById<TextView>(R.id.final_date).text = date
        findViewById<TextView>(R.id.final_time).text = time

        val resId = resources.getIdentifier(restaurant.image, "drawable", packageName)
        findViewById<ImageView>(R.id.imageView).setImageResource(resId)

        findViewById<Button>(R.id.final_btn_cancel).setOnClickListener {
            cancelReservation(userId, reservationId)
            Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("user_id", userId)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun cancelReservation(userId: String, reservationId: Int) {
        val file = File(filesDir, "user_info.txt")
        val jsonStr = if (file.exists()) {
            file.readText()
        } else {
            assets.open("user_info.txt").bufferedReader().use { it.readText() }
        }

        val type = object : TypeToken<MutableList<UserInfo>>() {}.type
        val userList: MutableList<UserInfo> = Gson().fromJson(jsonStr, type)

        val user = userList.find { it.id == userId }
        user?.reserved?.removeIf { it.reservation_id == reservationId }

        val updatedJson = Gson().toJson(userList)
        file.writeText(updatedJson)
    }
}