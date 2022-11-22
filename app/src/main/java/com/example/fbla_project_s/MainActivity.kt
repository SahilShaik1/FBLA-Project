package com.example.fbla_project_s

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.fbla_project_s.databinding.ActivityMainBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    /*private val database = Firebase.database.reference
    //getReference gets the node to put values
    //child is same as reference
    fun newUserMade(userId: String, name: String, email: String){
        val user = User(name, email)
        database.child("Users").child(userId).setValue(user)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newUserMade("User4", "Kaushal", "Kaushal@gmail.com")

    }*/
    private lateinit var binding: ActivityMainBinding
    private var database = Firebase.database.reference
    //getReference gets the node to put values
    //child is same as reference
    /*fun newUserMade(userId: String, name: String, email: String){
        val user = User(name, email)
        database.child("Users").child(userId).setValue(user)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveBtn.setOnClickListener {

            val name = binding.name.text.toString()
            val email = binding.email.text.toString()

            val user = User(name, email)
            database.child("Users").child(name).setValue(user)
        }
    }


}

