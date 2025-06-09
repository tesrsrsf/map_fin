package com.example.map_final_project

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class AdminEditActivity : AppCompatActivity() {
    private lateinit var restaurant: Restaurant
    private lateinit var menuList: MutableList<MenuItem>
    private lateinit var menuAdapter: AdminMenuAdapter

    private var lastDefImg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit)

        val restaurantJson = intent.getStringExtra("restaurant")
        restaurant = Gson().fromJson(restaurantJson, Restaurant::class.java)


        findViewById<EditText>(R.id.admin_edit_name).setText(restaurant.name)
        findViewById<TextView>(R.id.admin_edit_id).text = restaurant.id.toString()
        findViewById<EditText>(R.id.admin_edit_type).setText(restaurant.type)
        findViewById<EditText>(R.id.admin_edit_loc).setText(restaurant.location)
        findViewById<EditText>(R.id.admin_edit_rate).setText(restaurant.rating)
        findViewById<EditText>(R.id.admin_edit_desc).setText(restaurant.description)
        findViewById<EditText>(R.id.admin_edit_open).setText(restaurant.openingHours.open)
        findViewById<EditText>(R.id.admin_edit_close).setText(restaurant.openingHours.close)

        var rand_logo_btn = findViewById<Button>(R.id.admin_ref_logo)
        rand_logo_btn.isVisible = false
        rand_logo_btn.isEnabled = false



        var imageView = findViewById<ImageView>(R.id.imageView3)
        var imageResId = resources.getIdentifier(restaurant.image, "drawable", packageName)
        imageView.setImageResource(if (imageResId != 0) imageResId else R.drawable.ic_launcher_background)


        var recyclerView = findViewById<RecyclerView>(R.id.admin_edit_menus)
        recyclerView.layoutManager = LinearLayoutManager(this)

        menuList = loadMenuInfo(restaurant)

        recyclerView.adapter = AdminEditAdapter(menuList)


        val btn_confirm = findViewById<Button>(R.id.admin_btn_res_back)
        val btn_del = findViewById<Button>(R.id.admin_btn_res_del)
        val btn_new_menu = findViewById<Button>(R.id.admin_btn_add_menu)

        val isNew = intent.getBooleanExtra("isNew", false)
        if (isNew) {
            btn_del.isEnabled = false
            btn_new_menu.isEnabled = false

            rand_logo_btn.isEnabled = true
            rand_logo_btn.isVisible = true
        }

        btn_confirm.setOnClickListener {
            updateThisRest()
            finish()
        }

        btn_del.setOnClickListener {
            deleteThisRest()
            finish()
        }

        btn_new_menu.setOnClickListener {
            val intent = Intent(this, AdminMenuEditActivity::class.java)
            intent.putExtra("isNew", -1)
            startActivityForResult(intent, 1001)
        }

        rand_logo_btn.setOnClickListener {
            imageResId = resources.getIdentifier(generateRandLogo(), "drawable", packageName)
            imageView.setImageResource(imageResId)
        }

        recyclerView = findViewById(R.id.admin_edit_menus)
        recyclerView.layoutManager = LinearLayoutManager(this)

        menuAdapter = AdminMenuAdapter(menuList) { selectedMenu ->
            //Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AdminMenuEditActivity::class.java)
            intent.putExtra("isNew", selectedMenu.id)
            intent.putExtra("menuName", selectedMenu.name)
            intent.putExtra("menuPrice", selectedMenu.price)
            intent.putExtra("menuImage", selectedMenu.image)
            startActivityForResult(intent, 1001)
        }

        recyclerView.adapter = menuAdapter


    }

    private fun loadMenuInfo(restaurant: Restaurant): MutableList<MenuItem> {
        return restaurant.menu
    }


    private fun deleteThisRest() {
        val file = File(filesDir, "restaurant_info.txt")
        val gson = Gson()
        val listType = object : TypeToken<MutableList<Restaurant>>() {}.type
        val restaurantList: MutableList<Restaurant> = if (file.exists()) {
            gson.fromJson(file.readText(), listType)
        } else {
            gson.fromJson(assets.open("restaurant_info.txt").bufferedReader().use { it.readText() }, listType)
        }

        val removed = restaurantList.removeIf { it.id == restaurant.id }

        file.writeText(gson.toJson(restaurantList))

        if (removed) {
            Toast.makeText(this, "Restaurant deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateThisRest() {
        val id = findViewById<TextView>(R.id.admin_edit_id).text.toString().toIntOrNull() ?: restaurant.id
        val name = findViewById<EditText>(R.id.admin_edit_name).text.toString()
        val type = findViewById<EditText>(R.id.admin_edit_type).text.toString()
        val location = findViewById<EditText>(R.id.admin_edit_loc).text.toString()
        val rating = findViewById<EditText>(R.id.admin_edit_rate).text.toString()
        val desc = findViewById<EditText>(R.id.admin_edit_desc).text.toString()
        val open = findViewById<EditText>(R.id.admin_edit_open).text.toString()
        val close = findViewById<EditText>(R.id.admin_edit_close).text.toString()


        val newRestaurant = restaurant.copy(
            id = id,
            name = name,
            type = type,
            location = location,
            rating = rating,
            description = desc,
            openingHours = OpeningHours(open, close),
            menu = menuList.toMutableList()
        )

        val file = File(filesDir, "restaurant_info.txt")
        val gson = Gson()
        val listType = object : TypeToken<MutableList<Restaurant>>() {}.type
        val restaurantList: MutableList<Restaurant> = if (file.exists()) {
            gson.fromJson(file.readText(), listType)
        } else {
            gson.fromJson(assets.open("restaurant_info.txt").bufferedReader().use { it.readText() }, listType)
        }

        /*
        val idx = restaurantList.indexOfFirst { it.id == restaurant.id }
        if (idx != -1) {
            restaurantList[idx] = newRestaurant
            file.writeText(gson.toJson(restaurantList))
            Toast.makeText(this, "Restaurant Updated", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
        }*/

        val idx = restaurantList.indexOfFirst { it.id == restaurant.id }
        if (idx != -1) {
            restaurantList[idx] = newRestaurant
        } else {
            restaurantList.add(newRestaurant)
        }
        file.writeText(gson.toJson(restaurantList))
        Toast.makeText(this, if (idx != -1) "Restaurant Updated" else "Restaurant Added", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            val menuId = data.getIntExtra("isNew", -1)
            val delete = data.getBooleanExtra("delete", false)

            if (delete) {
                menuList.removeAll { it.id == menuId }
                menuAdapter.notifyDataSetChanged()
            } else {
                val menuName = data.getStringExtra("menuName") ?: ""
                val menuPrice = data.getIntExtra("menuPrice", 0)
                val menuImage = data.getStringExtra("menuImage") ?: "menu_default"

                if (menuId == -1) {
                    val newId = (menuList.maxOfOrNull { it.id } ?: 0) + 1
                    menuList.add(MenuItem(newId, menuName, menuPrice, menuImage))
                    menuAdapter.notifyItemInserted(menuList.size - 1)
                } else {
                    val idx = menuList.indexOfFirst { it.id == menuId }
                    if (idx != -1) {
                        menuList[idx] = MenuItem(menuId, menuName, menuPrice, menuImage)
                        menuAdapter.notifyItemChanged(idx)
                    }
                }
            }
        }
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