package com.example.map_final_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class SignUpActivity : AppCompatActivity() {

    private lateinit var idInput: EditText
    private lateinit var pwInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var genderInput: EditText
    private lateinit var btnSignUp: Button
    private lateinit var btnExit: Button

    private val fileName = "user_info.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        idInput = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.sign_id).editText!!
        pwInput = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.sign_pw).editText!!
        nameInput = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.sign_name).editText!!
        ageInput = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.sign_age).editText!!
        genderInput = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.sign_gender).editText!!
        btnSignUp = findViewById<Button>(R.id.sign_btn_signup)
        btnExit = findViewById<Button>(R.id.sign_btn_exit)


        copyAssetIfNeeded(fileName)

        btnSignUp.setOnClickListener {
            val id = idInput.text.toString().trim()
            val pw = pwInput.text.toString().trim()
            val name = nameInput.text.toString().trim()
            val age = ageInput.text.toString().toIntOrNull()
            val gender = genderInput.text.toString().trim()

            if (id.isEmpty() || pw.isEmpty() || name.isEmpty() || age == null || gender.isEmpty()) {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (id == "admin") {
                Toast.makeText(this, "Illegal id.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userList = loadUsers()
            if (userList.any { it.id == id }) {
                Toast.makeText(this, "ID already exists.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUser = UserInfo(
                id = id,
                passwd = pw,
                info = PersonalInfo(name = name, age = age, gender = gender),
                reserved = mutableListOf()
            )

            val updatedList = userList.toMutableList().apply { add(newUser) }
            saveUsers(updatedList)

            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnExit.setOnClickListener {
            finish()
        }
    }

    private fun copyAssetIfNeeded(fileName: String) {
        val file = File(filesDir, fileName)
        if (!file.exists()) {
            assets.open(fileName).use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    private fun loadUsers(): List<UserInfo> {
        val file = File(filesDir, fileName)
        if (!file.exists()) return emptyList()
        val reader = file.bufferedReader()
        val type = object : TypeToken<List<UserInfo>>() {}.type
        return Gson().fromJson(reader, type)
    }

    private fun saveUsers(userList: List<UserInfo>) {
        val file = File(filesDir, fileName)
        val json = Gson().toJson(userList)
        file.writeText(json)
    }
}