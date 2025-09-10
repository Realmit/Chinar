package com.oookraton.chinar

import AssetServer
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class PanoramaActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var server: AssetServer
    private lateinit var selectButton: Button
    private var currentImage = "room1.jpg"
    private val roomMap = mapOf(
        "Комната 1" to "room1.jpg",
        "Комната 2" to "room2.jpg",
        "Ванная" to "bathroom.jpg",
        "Кухня" to "kitchen.jpg"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3dview)

        selectButton = findViewById<Button>(R.id.button_select_room)
        val backButton = findViewById<Button>(R.id.buttonBack)

        // Set initial text
        selectButton.text = "Комната 1"

        // Popup menu on click
        selectButton.setOnClickListener {
            showRoomSelectionPopup(it)
        }

        // Back button
        backButton.setOnClickListener {
            val intent = Intent(this, Cafe_mainpage::class.java)
            startActivity(intent)
            finish()
        }

        // Start server
        server = AssetServer(this, 8080)
        try {
            server.start()
        } catch (e: IOException) {
            e.printStackTrace()
            finish()
            return
        }

        webView = findViewById(R.id.webview)
        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            useWideViewPort = true
            loadWithOverviewMode = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.loadUrl("http://localhost:8080/360.html")
    }

    private fun showRoomSelectionPopup(anchor: View) {
        val popup = PopupMenu(this, anchor)
        val menu = popup.menu

        // Add rooms to menu
        roomMap.keys.forEach { roomName ->
            menu.add(roomName)
        }

        popup.setOnMenuItemClickListener { item ->
            val selectedRoom = item.title.toString()
            val imagePath = roomMap[selectedRoom] ?: return@setOnMenuItemClickListener false

            if (imagePath == currentImage) return@setOnMenuItemClickListener true // No action

            currentImage = imagePath
            selectButton.text = selectedRoom // Update button text
            webView.evaluateJavascript("javascript:changePanorama('$imagePath')", null)
            true
        }
        popup.show()
    }

    override fun onDestroy() {
        server.stop()
        webView.destroy()
        super.onDestroy()
    }
}