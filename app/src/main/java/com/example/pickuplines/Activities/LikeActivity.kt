package com.example.pickuplines.Activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pickuplines.Adapters.LikedPickupLineAdapter
import com.example.pickuplines.R

class LikeActivity : AppCompatActivity() {

    private lateinit var likedPickupLines: List<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_like)

        sharedPreferences = getSharedPreferences("LikedPickupLines", MODE_PRIVATE)

        val likedLinesSet = sharedPreferences.getStringSet("LikedLines", setOf())
        likedPickupLines = likedLinesSet?.toList() ?: emptyList()

        recyclerView = findViewById(R.id.recyclerViewLiked)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = LikedPickupLineAdapter(this, likedPickupLines.toMutableList())
        recyclerView.adapter = adapter
    }
}