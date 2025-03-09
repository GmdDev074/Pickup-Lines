package com.example.pickuplines.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickuplines.Adapters.PickupLineAdapter
import com.example.pickuplines.DataClasses.PickupLine
import com.example.pickuplines.DataClasses.PickupLinesData
import com.example.pickuplines.Notifications.showNotification
import com.example.pickuplines.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.*

class PickupLineActivity : AppCompatActivity() {

    private lateinit var pickupLinesRecyclerView: RecyclerView
    private lateinit var pickupLineAdapter: PickupLineAdapter
    private lateinit var category: String
    private lateinit var adView: AdView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var txtType: TextView
    private lateinit var imageBack: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickup_line)

        MobileAds.initialize(this)

        shimmerFrameLayout = findViewById(R.id.shimmer_ad_container)
        adView = findViewById(R.id.adView)
        txtType = findViewById(R.id.typeText)
        pickupLinesRecyclerView = findViewById(R.id.pickupLinesRecyclerView)
        imageBack = findViewById(R.id.imageBack)

        imageBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        pickupLinesRecyclerView.layoutManager = LinearLayoutManager(this)

        category = intent.getStringExtra("CATEGORY_NAME") ?: "General"
        txtType.text = category

        shimmerFrameLayout.startShimmer()
        setupAdView()
        loadPickupLines()
    }

    private fun setupAdView() {
        val adRequest = AdRequest.Builder().build()

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                adView.visibility = View.VISIBLE
                Log.d("AdStatus", "Ad loaded successfully.")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                Log.e("AdStatus", "Ad failed to load: ${adError.message}")
            }
        }
        adView.loadAd(adRequest)
    }

    private fun loadPickupLines() {
        val pickupLines = getPickupLinesForCategory(category)

        if (pickupLines.isNotEmpty()) {
            pickupLineAdapter = PickupLineAdapter(this, pickupLines)
            pickupLinesRecyclerView.adapter = pickupLineAdapter
        } else {
            Toast.makeText(this, "No pickup lines available for $category", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getPickupLinesForCategory(category: String): List<PickupLine> {
        return when (category) {
            "Dreadful" -> PickupLinesData.badLines
            "Romance" -> PickupLinesData.loveLines
            "Endearing" -> PickupLinesData.cuteLines
            "Witty" -> PickupLinesData.cleverLines
            "Smutty" -> PickupLinesData.dirtyLines
            "Cuisine" -> PickupLinesData.foodLines
            "Corny" -> PickupLinesData.cheesyLines
            "Comical" -> PickupLinesData.funnyLines
            "Affectionate" -> PickupLinesData.romanticLines
            "Gloomy" -> PickupLinesData.sadLines
            "Seductive" -> PickupLinesData.flirtyLines
            "Classic" -> PickupLinesData.classicLines
            "Compliment" -> PickupLinesData.complimentLines
            "Attraction" -> PickupLinesData.attraction
            else -> emptyList()
        }
    }


    override fun onPause() {
        super.onPause()
        shimmerFrameLayout.stopShimmer()
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmer()
    }
}