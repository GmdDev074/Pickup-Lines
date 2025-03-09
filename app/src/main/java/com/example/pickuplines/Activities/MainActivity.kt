package com.example.pickuplines.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickuplines.DataClasses.PickupLine
import com.example.pickuplines.DataClasses.PickupLinesData
import com.example.pickuplines.R
import com.example.pickuplines.Adapters.TypeAdapter
import com.example.pickuplines.Models.TypeModel
import com.example.pickuplines.Notifications.NotificationService
import com.example.pickuplines.Notifications.createNotificationChannel
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
    private lateinit var likeImageView: ImageView
    private lateinit var crownImageView: ImageView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private var backPressedTime: Long = 0
    private val exitInterval = 2000
    private val PERMISSIONS_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawer_layout)
        navigation = findViewById(R.id.nav_view)
        drawerImage = findViewById(R.id.drawer_image)
        likeImageView = findViewById(R.id.like)
        crownImageView = findViewById(R.id.crown)

        shimmerFrameLayout = findViewById(R.id.shimmer_ad_container)
        adView = findViewById(R.id.adView)

        createNotificationChannel(this)

        val serviceIntent = Intent(this, NotificationService::class.java)
        startService(serviceIntent)

        if (!hasPermissions()) {
            requestPermissions()
        }

        shimmerFrameLayout.startShimmer()
        setupAdView()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        likeImageView.setOnClickListener {
            val intent = Intent(this, LikeActivity::class.java)
            startActivity(intent)
        }

        crownImageView.setOnClickListener {
            val intent = Intent(this, PremiumActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupBackPressHandler()

        val typeList = listOf(
            TypeModel("Dreadful", R.drawable.bad, R.color.color1),
            TypeModel("Romance", R.drawable.love, R.color.color2),
            TypeModel("Endearing", R.drawable.cute, R.color.color3),
            TypeModel("Witty", R.drawable.clever, R.color.color4),
            TypeModel("Smutty", R.drawable.dirty, R.color.color5),
            TypeModel("Cuisine", R.drawable.foodie, R.color.color6),
            TypeModel("Corny", R.drawable.chessy, R.color.color7),
            TypeModel("Comical", R.drawable.funny, R.color.color8),
            TypeModel("Affectionate", R.drawable.romantic, R.color.color9),
            TypeModel("Gloomy", R.drawable.sad, R.color.color10),
            TypeModel("Seductive", R.drawable.flirt, R.color.color11),
            TypeModel("Classic", R.drawable.classic, R.color.color12),
            TypeModel("Compliment", R.drawable.compliment, R.color.color13),
            TypeModel("Attraction", R.drawable.attraction, R.color.color13)
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

    //request permissions
    private fun hasPermissions(): Boolean {
        val requiredPermissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                return false
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
        }
        ActivityCompat.requestPermissions(
            this,
            permissionsToRequest.toTypedArray(),
            PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            val deniedPermissions = permissions
                .filterIndexed { index, _ -> grantResults[index] != PackageManager.PERMISSION_GRANTED }

            if (deniedPermissions.isEmpty()) {
                Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*private fun proceedToNextScreen() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isOnboardingShown = sharedPreferences.getBoolean("onboarding_shown", false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (!isOnboardingShown) {
                //startActivity(Intent(this, Onboarding::class.java))
                sharedPreferences.edit().putBoolean("onboarding_shown", true).apply()
            } else {
                //startActivity(Intent(this, MainActivity::class.java))
            }
            //finish()
        }, 4000)
    } */ // end request permissions


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
                    finish()
                } else {
                    Toast.makeText(
                        this@MainActivity, "Press back again to exit", Toast.LENGTH_SHORT).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }

}