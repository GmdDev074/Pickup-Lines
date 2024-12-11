package com.example.pickuplines

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        drawerImage = findViewById(R.id.drawer_image)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val typeList = listOf(
            TypeModel("Bad", R.drawable.bad, R.color.color1),
            TypeModel("Love", R.drawable.love, R.color.color2),
            TypeModel("Cute", R.drawable.cute, R.color.color3),
            TypeModel("Clever", R.drawable.clever, R.color.color4),
            TypeModel("Dirty", R.drawable.dirty, R.color.color5),
            TypeModel("Food", R.drawable.foodie, R.color.color6),
            TypeModel("Chessy", R.drawable.chessy, R.color.color7),
            TypeModel("Funny", R.drawable.funny, R.color.color8),
            TypeModel("Romantic", R.drawable.romantic, R.color.color9),
            TypeModel("Sad", R.drawable.sad, R.color.color10),
            TypeModel("Flirty", R.drawable.flirt, R.color.color11),
        )

        val adapter = TypeAdapter(this, typeList)
        recyclerView.adapter = adapter


        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_like -> {
                    Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_share -> {
                    Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_premium -> {
                    Toast.makeText(this, "Premium", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_review -> {
                    Toast.makeText(this, "Review", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_support -> {
                    Toast.makeText(this, "Customer Support", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_policy -> {
                    Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_terms -> {
                    Toast.makeText(this, "Terms and Conditions", Toast.LENGTH_SHORT).show()
                    true
                }


                else -> false
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        drawerImage.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}