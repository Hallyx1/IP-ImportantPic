package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityChooseLoginOrRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseLoginOrRegisterBinding

    var started = true
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLoginOrRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        val currentUser = auth.currentUser

        if(currentUser !=null){
            val intent = Intent(this,ChooseLoginOrRegisterActivity::class.java)

            startActivity(intent)
            finish()
            //test
        }else{
            val intent = Intent(this,ChooseLoginOrRegisterActivity::class.java)

            startActivity(intent)
            finish()

        }
    }
}