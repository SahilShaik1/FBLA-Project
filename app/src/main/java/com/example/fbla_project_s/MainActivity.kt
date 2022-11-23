package com.example.fbla_project_s

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.fbla_project_s.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.snapshot.BooleanNode
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

//For all constants
    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }


    private lateinit var binding: ActivityMainBinding
    private var database = Firebase.database.reference
    private lateinit var gsc : GoogleSignInClient
    private lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1004010661295-165b7md6kuv0dge3p9iedtcua3br63cu.apps.googleusercontent.com")
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
        binding.GSignIn.setOnClickListener {
             Log.d(TAG,"onCreate: begin Google SignIn")
            val intent = gsc.signInIntent
            //Line means deprecated, but only deprecated in java
            startActivityForResult(intent, RC_SIGN_IN)
        }

        binding.saveBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val user = User(name, email)
            database.child("Users").child(name).setValue(user)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google SignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            } catch (e: Exception) {
                //Something Failed
                Log.d(TAG, "OnActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                //login worked
                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn")
                val firebaseUser = auth.currentUser
                val uid = firebaseUser!!.uid
                val email = firebaseUser!!.email
                Log.d(TAG, "firebaseAuthWithGoogleAccount: uid: $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")
                if (authResult.additionalUserInfo!!.isNewUser){
                    //User is new - account created
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account Created... \n $email")
                    Toast.makeText(this@MainActivity, "Account Created... \n$email", Toast.LENGTH_SHORT).show()
                    val AccName = account.givenName.toString()
                    database.child("Users").setValue(AccName)
                    database.child("Users").child(AccName).child("Name").setValue(AccName)
                    database.child("Users").child(AccName).child("Email").setValue(email)
                }
                else{
                    //existing user
                    var AccName = account.givenName.toString()
                    if (findData(AccName, false) == true){
                        //if found
                        Toast.makeText(this@MainActivity, "Found In Database", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        //if not found, add it
                        database.child("Users").setValue(AccName)
                        database.child("Users").child(AccName).child("Name").setValue(AccName)
                        database.child("Users").child(AccName).child("Email").setValue(email)
                    }
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing User...\n$email")
                    Toast.makeText(this@MainActivity, "Account Created... \n$email", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener{e ->
                //Login Failed
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Log-in failed, ${e.message}")
                Toast.makeText(this@MainActivity, "Log-in failed, ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun findData(name : String, getEmail : Boolean?): Any? {
        //getting Name would be useless since it is the title
        var database = FirebaseDatabase.getInstance().getReference("Users")
        //Can email can be anything
        var returnEmail: Any? = null
        var Found: Any? = null
        database.child(name).get().addOnSuccessListener {
            if (it.exists()){
                var email = it.child("Email").value
                returnEmail = email
            }
            else{
                //if not existing
                Toast.makeText(this, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
                Found = false
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
        if (getEmail == true){
            return returnEmail
        }
        if (Found == false){
            return false
        }
        return null
    }
}




