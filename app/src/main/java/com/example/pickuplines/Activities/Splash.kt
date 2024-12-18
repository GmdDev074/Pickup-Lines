package com.example.pickuplines.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.pickuplines.Notifications.createNotificationChannel
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

        createNotificationChannel(this)

        proceedToNextScreen()
    }

    private fun proceedToNextScreen() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isOnboardingShown = sharedPreferences.getBoolean("onboarding_shown", false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (!isOnboardingShown) {
                startActivity(Intent(this, Onboarding::class.java))
                sharedPreferences.edit().putBoolean("onboarding_shown", true).apply()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, 4000)
    }
}
