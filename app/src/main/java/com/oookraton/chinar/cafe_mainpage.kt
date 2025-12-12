package com.oookraton.chinar

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class Cafe_mainpage : AppCompatActivity() {
    private var decorPopup: View? = null

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

        val buttonAboutDecoration = findViewById<Button>(R.id.buttonAboutDecoration)
        buttonAboutDecoration.setOnClickListener {
            showDecorationPopup()
        }
    }

    private fun showDecorationPopup() {
        // Create popup if not already created
        if (decorPopup == null) {
            val popupView = layoutInflater.inflate(R.layout.popup_decoration, null)

            // Set up close button
            popupView.findViewById<Button>(R.id.buttonClosePopup).setOnClickListener {
                hideDecorationPopup()
            }

            // Add to root layout
            val rootLayout = findViewById<ViewGroup>(android.R.id.content)
            rootLayout.addView(popupView)

            // Set initial state (invisible)
            popupView.alpha = 0f
            popupView.isVisible = true

            // Position in center
            val params = popupView.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = (resources.displayMetrics.heightPixels * 0.1).toInt()
            params.bottomMargin = (resources.displayMetrics.heightPixels * 0.1).toInt()
            popupView.layoutParams = params

            decorPopup = popupView
        }

        // Show with fade-in animation
        decorPopup?.apply {
            alpha = 0f
            isVisible = true

            // Fade in animation (2 seconds)
            ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).apply {
                duration = 2000
                interpolator = DecelerateInterpolator()
                start()
            }
        }
    }

    private fun hideDecorationPopup() {
        decorPopup?.apply {
            // Fade out animation (1 second)
            ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply {
                duration = 1000
                interpolator = DecelerateInterpolator()
                addListener(object: android.animation.AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: android.animation.Animator) {}
                    override fun onAnimationEnd(animation: android.animation.Animator) {
                        isVisible = false
                    }
                    override fun onAnimationCancel(animation: android.animation.Animator) {
                        isVisible = false
                    }
                    override fun onAnimationRepeat(animation: android.animation.Animator) {}
                })
                start()
            }
        }
    }
}