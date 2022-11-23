package com.example.fbla_project_s

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fbla_project_s.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var auth : FirebaseAuth
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
        auth = FirebaseAuth.getInstance()

        binding.saveBtn.setOnClickListener {

            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                if ((password == confirmPassword) && (password.length >= 8)) {

                    val user = User(name, email, password)
                    auth.createUserWithEmailAndPassword(email, password)
                    database.child("Users").child(name).setValue(user).addOnSuccessListener {

                        binding.name.text.clear()
                        binding.email.text.clear()
                        binding.password.text.clear()
                        binding.confirmPassword.text.clear()

                        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener {

                        Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                else {

                    if (password != confirmPassword) {

                        Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
                    }
                    else {

                        Toast.makeText(this, "Password has to be above 8 characters", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {

                Toast.makeText(this, "Enter a real email", Toast.LENGTH_SHORT).show()

            }
        }
    }


}

