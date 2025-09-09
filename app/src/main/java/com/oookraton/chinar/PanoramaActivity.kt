package com.oookraton.chinar

import AssetServer
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class PanoramaActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var server: AssetServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3dview)
        // Back button
        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            val intent = Intent(this, Cafe_mainpage::class.java)
            startActivity(intent)
            finish()
        }
        // Start local server
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }

        webView.loadUrl("http://localhost:8080/360.html")
    }

    override fun onDestroy() {
        server.stop()
        webView.destroy()
        super.onDestroy()
    }
}