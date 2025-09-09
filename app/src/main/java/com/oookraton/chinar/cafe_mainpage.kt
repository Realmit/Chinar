package com.oookraton.chinar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Cafe_mainpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_mainpage)
        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            val intent = Intent(this, Mainpage::class.java)
            startActivity(intent)
            finish()
        }
        val button3d = findViewById<Button>(R.id.button3dView)
        button3d.setOnClickListener {
            val intent = Intent(this, PanoramaActivity::class.java)
            startActivity(intent)
        }
    }
}