package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class viewPic : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pic)

        var btnCam = findViewById<Button>(R.id.butcam)
        var btnCamRoll = findViewById<Button>(R.id.butcamRoll)
        btnCamRoll.isClickable = false

        btnCam.setOnClickListener(){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }
}