package com.example.map_final_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

data class UserInfo(
    val id: String,
    val passwd: String,
    val info: PersonalInfo,
    val reserved: MutableList<Reservation>
)

data class PersonalInfo(
    val name: String,
    val age: Int,
    val gender: String
)

data class Reservation(
    val reservation_id: Int,
    val restaurant_id: Int,

    @SerializedName("number_of_people")
    val people: Int,
    val date: String,
    val time: String
)

data class OpeningHours(
    val open: String,
    val close: String
)

data class MenuItem(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String
)

data class Restaurant(
    val id: Int,

    @SerializedName("restaurant")
    val name: String,
    val type: String,
    val location: String,
    val rating: String,
    val image: String,
    val description: String,
    val openingHours: OpeningHours,

    @SerializedName("Menu")
    val menu: List<MenuItem>
)


class MainActivity : AppCompatActivity() {

    private lateinit var userList: List<UserInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 处理系统边距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 解析 JSON 数据
        userList = loadUserInfo()
        //Log.w(localClassName, "stage 1")

        val editId = findViewById<EditText>(R.id.edit_user_id)
        val editPwd = findViewById<EditText>(R.id.edit_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        //Log.w(localClassName, "stage 2")

        btnLogin.setOnClickListener {
            val inputId = editId.text.toString()
            val inputPwd = editPwd.text.toString()

            val matchedUser = userList.find { it.id == inputId && it.passwd == inputPwd }
            //Log.w(localClassName, "stage 3")

            if (matchedUser != null) {
                // 成功登录，跳转到 HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("user_id", matchedUser.id)

                //Log.w(localClassName, "stage 4")
                startActivity(intent)

            } else {
                Toast.makeText(this, "ID or Password incorrect.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserInfo(): List<UserInfo> {
        val inputStream = assets.open("user_info.txt")
        val reader = InputStreamReader(inputStream)
        val gson = Gson()
        val type = object : TypeToken<List<UserInfo>>() {}.type
        return gson.fromJson(reader, type)
    }
}
