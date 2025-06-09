package com.example.map_final_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/*
class AdminMenuEditActivity : AppCompatActivity() {
    private var nameWatcherJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_menu_edit)

        val menuId = intent.getIntExtra("isNew", -1)
        val nameEdit = findViewById<EditText>(R.id.admin_edit_menu_name)
        val priceEdit = findViewById<EditText>(R.id.admin_edit_menu_price)
        val imgView = findViewById<ImageView>(R.id.admin_menu_image)


        if (menuId != -1) {
            nameEdit.setText(intent.getStringExtra("menuName"))
            priceEdit.setText(intent.getIntExtra("menuPrice", 0).toString())
        }



        nameWatcherJob = CoroutineScope(Dispatchers.Main).launch {
            var lastResult: String? = null
            while (isActive) {
                val input = nameEdit.text.toString().trim()
                val imgRes = getDefaultImageForMenu(input)

                if (imgRes != lastResult){
                    val imgId = resources.getIdentifier(imgRes, "drawable", packageName)
                    imgView.setImageResource(if (imgId != 0) imgId else R.drawable.menu_default)
                    lastResult = imgRes

                }

                delay(200)


            }
        }


        findViewById<Button>(R.id.admin_menu_confirm).setOnClickListener {
            val menuName = nameEdit.text.toString().trim()
            val menuPrice = priceEdit.text.toString().toIntOrNull() ?: 0
            val menuImage = getDefaultImageForMenu(menuName)

            val result = Intent().apply {
                putExtra("isNew", menuId)
                putExtra("menuName", menuName)
                putExtra("menuPrice", menuPrice)
                putExtra("menuImage", menuImage)
            }
            setResult(RESULT_OK, result)
            finish()
        }

        findViewById<Button>(R.id.admin_menu_del).setOnClickListener {
            val result = Intent().apply {
                putExtra("isNew", menuId)
                putExtra("delete", true)
            }
            setResult(RESULT_OK, result)
            finish()
        }
    }

    override fun onDestroy() {

        nameWatcherJob?.cancel()
        super.onDestroy()
    }


    private fun getDefaultImageForMenu(name: String): String {
        val lowered = name.lowercase()
        return when {
            "burger" in lowered -> "menu_burger"
            "fries" in lowered -> "menu_fries"
            "pizza" in lowered -> "menu_pizza"
            "chicken" in lowered -> "menu_chicken"
            "coke" in lowered -> "menu_coke"
            "beer" in lowered -> "menu_beer"
            else -> "menu_default"
        }
    }
}

*/


class AdminMenuEditActivity : AppCompatActivity() {
    private var nameWatcherJob: Job? = null

    private lateinit var nameEdit: EditText
    private lateinit var priceEdit: EditText
    private lateinit var imgView: ImageView

    private var isNew: Boolean = true
    private var menuId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_menu_edit)

        nameEdit = findViewById(R.id.admin_edit_menu_name)
        priceEdit = findViewById(R.id.admin_edit_menu_price)
        imgView = findViewById(R.id.admin_menu_image)

        // 读取传入参数
        menuId = intent.getIntExtra("isNew", -1)
        val menuName = intent.getStringExtra("menuName")
        val menuPrice = intent.getIntExtra("menuPrice", -1)
        val menuImage = intent.getStringExtra("menuImage") ?: "menu_default"

        isNew = (menuId == -1)


        if (!isNew) {
            nameEdit.setText(menuName ?: "")
            priceEdit.setText(if (menuPrice >= 0) menuPrice.toString() else "")
            val imgId = resources.getIdentifier(menuImage, "drawable", packageName)
            imgView.setImageResource(imgId)
        }


        nameWatcherJob = CoroutineScope(Dispatchers.Main).launch {
            var lastImageKey = ""
            while (isActive) {
                val inputName = nameEdit.text.toString().trim()
                val imageKey = getDefaultImageForMenu(inputName)

                if (imageKey != lastImageKey) {
                    val imgId = resources.getIdentifier(imageKey, "drawable", packageName)
                    imgView.setImageResource(if (imgId != 0) imgId else R.drawable.menu_default)
                    lastImageKey = imageKey
                }

                delay(200)
            }
        }


        findViewById<Button>(R.id.admin_menu_confirm).setOnClickListener {
            val resultName = nameEdit.text.toString().trim()
            val resultPrice = priceEdit.text.toString().toIntOrNull() ?: 0
            val resultImage = getDefaultImageForMenu(resultName)

            val result = Intent().apply {
                putExtra("isNew", menuId)
                putExtra("menuName", resultName)
                putExtra("menuPrice", resultPrice)
                putExtra("menuImage", resultImage)
            }
            setResult(RESULT_OK, result)
            finish()
        }


        findViewById<Button>(R.id.admin_menu_del).apply {
            isEnabled = !isNew
            setOnClickListener {
                val result = Intent().apply {
                    putExtra("isNew", menuId)
                    putExtra("delete", true)
                }
                setResult(RESULT_OK, result)
                finish()
            }
        }
    }

    override fun onDestroy() {
        nameWatcherJob?.cancel()
        super.onDestroy()
    }


    private fun getDefaultImageForMenu(name: String): String {
        val lowered = name.lowercase()

        if (lowered == "big mac") {
            return "menu_bigmac"
        } else if (lowered == "french fries mc") {
            return "menu_frenchfries"
        } else if (lowered == "coca cola mc") {
            return "menu_mccoke"
        } else if (lowered == "kfc chicken") {
            return "menu_chicken"
        } else if (lowered == "kfc wings") {
            return "menu_wings"
        } else if (lowered == "chicken burger") {
            return "menu_chicken_burger"
        } else if (lowered == "classic whopper") {
            return "menu_whopper"
        } else if (lowered == "burger king french fries") {
            return "menu_bkfries"
        } else if (lowered == "coca cola bk") {
            return "menu_bkcoke"
        } else if (lowered == "super supreme pizza") {
            return "menu_pizza"
        } else if (lowered == "pizza hut spaghetti") {
            return "menu_spaghetti"
        } else if (lowered == "pizza hut beer") {
            return "menu_beer"
        }


        if ("burger" in lowered) {
            return "menu_burger"
        } else if ("whopper" in lowered) {
            return "menu_def_whopper"
        } else if ("fries" in lowered) {
            return "menu_def_fries"
        } else if ("wings" in lowered) {
            return "menu_def_wings"
        } else if ("pizza" in lowered) {
            return "menu_def_pizza"
        } else if ("chicken" in lowered) {
            return "menu_def_chicken"
        } else if ("coke" in lowered) {
            return "menu_def_coke"
        } else if ("beer" in lowered) {
            return "menu_def_beer"
        } else if ("spaghetti" in lowered) {
            return "menu_def_spaghetti"
        } else if ("hawai" in lowered && "pizza" in lowered) {
            return "menu_def_hawaii"
        } else if ("noodle" in lowered) {
            return "menu_def_noodles"
        } else if ("curry" in lowered) {
            return "menu_def_curry"
        } else if ("soup" in lowered) {
            return "menu_def_soup"
        } else {
            return "menu_default"
        }
    }
}


