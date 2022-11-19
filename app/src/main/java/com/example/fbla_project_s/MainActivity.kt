package com.example.fbla_project_s

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import com.example.fbla_project_s.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private val database = Firebase.database.reference
    //getReference gets the node to put values
    //child is same as reference
    fun NewUserMade(userId: String, name: String, email: String){
        val user = User(name, email)
        database.child("Users").child("test").setValue(user)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NewUserMade("TestID", "Someone", "test@gmail.com")
    }

}