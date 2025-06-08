package com.example.map_final_project

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import kotlin.random.Random

class ReservationConfirmActivity : AppCompatActivity() {

    private lateinit var restaurant: Restaurant
    private lateinit var userId: String
    private var people: Int = 1
    private lateinit var date: String
    private lateinit var openingTime: String
    private lateinit var closingTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_confirm)

        // 绑定视图
        val dateText = findViewById<TextView>(R.id.date)
        val peopleText = findViewById<TextView>(R.id.people)
        val openingText = findViewById<TextView>(R.id.openinghrs)
        val timeInput = findViewById<EditText>(R.id.editTextTime)
        val confirmButton = findViewById<Button>(R.id.btn_confirm)
        val cancelButton = findViewById<Button>(R.id.btn_cancel)

        // 获取传入数据
        userId = intent.getStringExtra("user_id") ?: ""
        date = intent.getStringExtra("date") ?: ""
        people = intent.getIntExtra("people", 1)
        val restaurantJson = intent.getStringExtra("restaurant")
        restaurant = Gson().fromJson(restaurantJson, Restaurant::class.java)

        // 显示基础信息
        dateText.text = date
        peopleText.text = people.toString()
        openingTime = restaurant.openingHours.open
        closingTime = restaurant.openingHours.close
        openingText.text = "$openingTime ~ $closingTime"

        // 取消按钮
        cancelButton.setOnClickListener {
            finish() // 返回上一页
        }

        // 确认按钮
        confirmButton.setOnClickListener {
            val inputTime = timeInput.text.toString().trim()

            if (!isValidTime(inputTime)) {
                Toast.makeText(this, "Invalid time. Must be between $openingTime and $closingTime", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 创建新预约对象
            val newReservation = Reservation(
                reservation_id = -1,
                restaurant_id = restaurant.id,
                people = people,
                date = date,
                time = inputTime
            )

            // 写入到用户的预约信息中
            updateUserReservation(userId, newReservation)

            // 返回 Activity 1

            Toast.makeText(this, "Reservation completed", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("user_id", userId)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            Toast.makeText(this, "Reservation completed", Toast.LENGTH_SHORT).show()


        }
    }

    // 验证时间格式是否合法且在营业时间范围内
    private fun isValidTime(time: String): Boolean {
        try {
            val fmt = SimpleDateFormat("HH:mm", Locale.getDefault())
            val input = fmt.parse(time)
            val open = fmt.parse(openingTime)
            val close = fmt.parse(closingTime)
            return input != null && input.after(open) && input.before(close)
        } catch (e: Exception) {
            return false
        }
    }

    // 修改用户预约信息（仅内存方式，可拓展为真实文件写入）
    private fun updateUserReservation(userId: String, newReservation: Reservation) {
        /*
        val jsonStr = assets.open("user_info.txt").bufferedReader().use { it.readText() }
        val userListType = object : TypeToken<MutableList<UserInfo>>() {}.type
        val users: MutableList<UserInfo> = Gson().fromJson(jsonStr, userListType)

        val maxId = users.flatMap { it.reserved }.maxOfOrNull { it.reservation_id } ?: 0
        var newId = maxId + 1

        val finalReservation = newReservation.copy(reservation_id = newId)


        val user = users.find { it.id == userId }
        user?.reserved?.add(finalReservation)*/

        val file = File(filesDir, "user_info.txt")

        // 第一次运行时从 assets 拷贝
        if (!file.exists()) {
            assets.open("user_info.txt").use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }

        // 读取当前文件数据
        val jsonStr = file.bufferedReader().use { it.readText() }
        val userListType = object : TypeToken<MutableList<UserInfo>>() {}.type
        val users: MutableList<UserInfo> = Gson().fromJson(jsonStr, userListType)

        // 找最大 reservation_id
        val maxId = users.flatMap { it.reserved }.maxOfOrNull { it.reservation_id } ?: 0
        val newId = maxId + 1
        val finalReservation = newReservation.copy(reservation_id = newId)

        // 添加新预约到当前用户
        val user = users.find { it.id == userId }
        user?.reserved?.add(finalReservation)

        // 写回文件
        val newJson = Gson().toJson(users)
        //Toast.makeText(this, "${user?.reserved?.get(2)?.toString()}", Toast.LENGTH_LONG).show()
        file.writeText(newJson)

    }


}