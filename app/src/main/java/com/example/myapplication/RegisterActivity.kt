package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()


        binding.btnLog.setOnClickListener(){

            var mEmail = binding.txtEmail.text.toString()
            var mPassword = binding.txtPass.text.toString()
            var mCPassword = binding.txtPassConfirm.text.toString()

            if (mEmail.isNotEmpty() && mPassword.isNotEmpty() && mCPassword.isNotEmpty()){
                if(mPassword == mCPassword){
                auth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }}
            }
        }
    }
}