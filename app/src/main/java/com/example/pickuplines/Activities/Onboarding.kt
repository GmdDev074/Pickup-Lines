package com.example.pickuplines.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.pickuplines.Adapters.OnboardingAdapter
import com.example.pickuplines.R
import com.google.android.material.button.MaterialButton

class Onboarding : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingAdapter
    private lateinit var btnStart: MaterialButton
    private lateinit var btnSkip: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        btnStart = findViewById(R.id.btnStart)
        btnSkip = findViewById(R.id.btnSkip)

        adapter = OnboardingAdapter(this)
        viewPager.adapter = adapter

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                if (viewPager.currentItem < adapter.itemCount - 1) {
                    viewPager.currentItem = viewPager.currentItem + 1
                } else {
                    viewPager.currentItem = 0
                }

                handler.postDelayed(this, 3000)
            }
        }

        handler.postDelayed(runnable, 3000)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == adapter.itemCount - 1) {
                    btnStart.visibility = View.VISIBLE
                    btnSkip.visibility = View.GONE
                } else {
                    btnStart.visibility = View.GONE
                    btnSkip.visibility = View.VISIBLE
                }
            }
        })

        btnStart.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}
