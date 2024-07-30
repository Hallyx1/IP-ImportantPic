package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.view.View
import com.example.myapplication.databinding.ActivityChooseLoginOrRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChooseLoginOrRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseLoginOrRegisterBinding

    var started = true
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityChooseLoginOrRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth


    }

    fun loginPage(view: View){
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

    fun registerPage(view: View){
        val intent = Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }
}