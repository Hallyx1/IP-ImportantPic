package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityChooseLoginOrRegisterBinding
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.newFixedThreadPoolContext

class LoginActivity : AppCompatActivity() {
        private lateinit var binding: ActivityLoginBinding
        private lateinit var auth: FirebaseAuth


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            auth = FirebaseAuth.getInstance()


            binding.btnLog.setOnClickListener(){

            var mEmail = binding.txtViewEmail.text.toString()
            var mPassword = binding.txtPass.text.toString()

            if (mEmail.isNotEmpty() && mPassword.isNotEmpty()){

                auth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
        }
}