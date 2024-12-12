package com.example.pickuplines.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickuplines.Adapters.LikedPickupLineAdapter
import com.example.pickuplines.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class LikeActivity : AppCompatActivity() {

    private lateinit var likedPickupLines: List<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var imageBack: ImageView
    private lateinit var adView: AdView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_like)

        imageBack = findViewById(R.id.imageBack1)

        shimmerFrameLayout = findViewById(R.id.shimmer_ad_container)
        adView = findViewById(R.id.adView)

        shimmerFrameLayout.startShimmer()
        setupAdView()

        sharedPreferences = getSharedPreferences("LikedPickupLines", MODE_PRIVATE)

        val likedLinesSet = sharedPreferences.getStringSet("LikedLines", setOf())
        likedPickupLines = likedLinesSet?.toList() ?: emptyList()

        recyclerView = findViewById(R.id.recyclerViewLiked)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = LikedPickupLineAdapter(this, likedPickupLines.toMutableList())
        recyclerView.adapter = adapter

        imageBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

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
                adView.loadAd(adRequest)
            }
        }
        adView.loadAd(adRequest)
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