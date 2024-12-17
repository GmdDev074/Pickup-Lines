package com.example.pickuplines.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickuplines.DataClasses.PickupLine
import com.example.pickuplines.DataClasses.PickupLinesData
import com.example.pickuplines.R
import com.example.pickuplines.Adapters.TypeAdapter
import com.example.pickuplines.Models.TypeModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigation: NavigationView
    private lateinit var drawerImage: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TypeAdapter
    private lateinit var adView: AdView
    private lateinit var starImageView: ImageView
    private lateinit var crownImageView: ImageView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private var backPressedTime: Long = 0
    private val exitInterval = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawer_layout)
        navigation = findViewById(R.id.nav_view)
        drawerImage = findViewById(R.id.drawer_image)
        starImageView = findViewById(R.id.star)
        crownImageView = findViewById(R.id.crown)

        shimmerFrameLayout = findViewById(R.id.shimmer_ad_container)
        adView = findViewById(R.id.adView)

        shimmerFrameLayout.startShimmer()
        setupAdView()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        starImageView.setOnClickListener {
            val intent = Intent(this, LikeActivity::class.java)
            startActivity(intent)
            finish()
        }

        crownImageView.setOnClickListener {
            val intent = Intent(this, PremiumActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupBackPressHandler()

        val typeList = listOf(
            TypeModel("Bad", R.drawable.bad, R.color.color1),
            TypeModel("Love", R.drawable.love, R.color.color2),
            TypeModel("Cute", R.drawable.cute, R.color.color3),
            TypeModel("Clever", R.drawable.clever, R.color.color4),
            TypeModel("Dirty", R.drawable.dirty, R.color.color5),
            TypeModel("Food", R.drawable.foodie, R.color.color6),
            TypeModel("Cheesy", R.drawable.chessy, R.color.color7),
            TypeModel("Funny", R.drawable.funny, R.color.color8),
            TypeModel("Romantic", R.drawable.romantic, R.color.color9),
            TypeModel("Sad", R.drawable.sad, R.color.color10),
            TypeModel("Flirty", R.drawable.flirt, R.color.color11),
            TypeModel("Classic", R.drawable.classic, R.color.color12),
            TypeModel("Compliment", R.drawable.compliment, R.color.color13)
        )

        val progressLayout: FrameLayout = findViewById(R.id.progress_layout)

        adapter = TypeAdapter(this, typeList, { category ->
            val pickupLines = getPickupLinesForCategory(category)
            if (pickupLines.isNotEmpty()) {
                Toast.makeText(this, "Loaded ${pickupLines.size} lines for $category", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No lines available for $category", Toast.LENGTH_SHORT).show()
            }
        }, progressLayout)

        recyclerView.adapter = adapter



        Log.d("MainActivity", "onCreate: Initialized drawerLayout, navigationView, and drawerImage")

        if (navigation == null) {
            Log.e("MainActivity", "NavigationView is null, cannot set listener.")
        } else {
            Log.d("MainActivity", "NavigationView initialized successfully.")
        }

        navigation.setNavigationItemSelectedListener { item ->
            Log.d("NavigationItemSelected", "Navigation item selected: ${item.title}")

            when (item.itemId) {
                R.id.nav_like -> {
                    Log.d("MainActivity", "Navigating to LikeActivity")
                    val intent = Intent(this, LikeActivity::class.java)
                    startActivity(intent)

                    drawerLayout.closeDrawer(GravityCompat.START)

                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_share -> {
                    Log.d("MainActivity", "Navigating to Share option")
                    Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show()

                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_premium -> {
                    Log.d("MainActivity", "Navigating to Premium option")
                    val intent = Intent(this, PremiumActivity::class.java)
                    startActivity(intent)

                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_review -> {
                    Log.d("MainActivity", "Navigating to Review option")
                    Toast.makeText(this, "Review", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_support -> {
                    Log.d("MainActivity", "Navigating to Customer Support")
                    Toast.makeText(this, "Customer Support", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_policy -> {
                    Log.d("MainActivity", "Navigating to Privacy Policy")
                    Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                else -> {
                    Log.d("MainActivity", "Unknown item selected")
                    return@setNavigationItemSelectedListener false
                }
            }
        }

        drawerImage.setOnClickListener {
            Log.d("MainActivity", "Opening drawer")
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun getPickupLinesForCategory(category: String): List<PickupLine> {
        return when (category) {
            "Bad" -> PickupLinesData.badLines
            "Love" -> PickupLinesData.loveLines
            "Cute" -> PickupLinesData.cuteLines
            "Clever" -> PickupLinesData.cleverLines
            "Dirty" -> PickupLinesData.dirtyLines
            "Food" -> PickupLinesData.foodLines
            "Cheesy" -> PickupLinesData.cheesyLines
            "Funny" -> PickupLinesData.funnyLines
            "Romantic" -> PickupLinesData.romanticLines
            "Sad" -> PickupLinesData.sadLines
            "Flirty" -> PickupLinesData.flirtyLines
            else -> emptyList()
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

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + exitInterval > System.currentTimeMillis()) {
                    finish() // Close the app
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Press back again to exit",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }

}