package com.example.pickuplines.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.pickuplines.R

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val animationView: LottieAnimationView = findViewById(R.id.lottieAnimation)

        animationView.setAnimation(R.raw.animation)
        animationView.playAnimation()

        animationView.loop(true)

        Handler().postDelayed({
            val intent = Intent(this, Onboarding::class.java)
            startActivity(intent)
            finish()
        }, 4000)

    }
}