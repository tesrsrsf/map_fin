package com.example.map_final_project

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import java.util.Locale

class ReservationInfoActivity : AppCompatActivity() {

    private var selectedDate: String = ""
    //private lateinit var restaurant: Restaurant
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_info)


        val peopleEditText = findViewById<EditText>(R.id.editTextNumberDecimal)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val confirmBtn = findViewById<Button>(R.id.btn_confirm)


        val restaurantJson = intent.getStringExtra("restaurant")
        userId = intent.getStringExtra("user_id") ?: ""
        //restaurant = Gson().fromJson(restaurantJson, Restaurant::class.java)


        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance()
        selectedDate = sdf.format(today.time)


        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            selectedDate = sdf.format(cal.time)
        }


        confirmBtn.setOnClickListener {
            val peopleText = peopleEditText.text.toString()
            if (peopleText.isEmpty()) {
                Toast.makeText(this, "Please enter number of people", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val people = peopleText.toIntOrNull()
            if (people == null || people < 1 || people > 10) {
                Toast.makeText(this, "Number of people must be 1–10", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val selected = sdf.parse(selectedDate)
            if (selected.before(today.time)) {
                Toast.makeText(this, "Please select a future date (Current: ${selectedDate})", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Toast.makeText(this, "Not yet implemented ${selectedDate}", Toast.LENGTH_SHORT).show()



            val intent = Intent(this, ReservationConfirmActivity::class.java)
            intent.putExtra("user_id", userId)
            intent.putExtra("restaurant", restaurantJson)
            intent.putExtra("people", people)
            intent.putExtra("date", selectedDate)
            startActivity(intent)

        }
    }
}