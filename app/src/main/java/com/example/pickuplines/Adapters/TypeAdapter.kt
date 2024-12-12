package com.example.pickuplines.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pickuplines.Activities.PickupLineActivity
import com.example.pickuplines.R
import com.example.pickuplines.Models.TypeModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class TypeAdapter(
    private val context: Context,
    private val typeList: List<TypeModel>,
    private val itemClickListener: (String) -> Unit
) : RecyclerView.Adapter<TypeAdapter.TypeViewHolder>() {

    private var interstitialAd: InterstitialAd? = null
    private var isAdLoading = false
    private var clickCounts = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return TypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = typeList[position]
        holder.txtType.text = type.typeName
        holder.imgType.setImageResource(type.imageResId)

        type.colorResId?.let {
            val color = ContextCompat.getColor(context, it)
            holder.itemView.setBackgroundColor(color)
        }

        holder.itemView.setOnClickListener {
            clickCounts++

            when (clickCounts) {
                1, 2 -> {
                    openPickupLineActivity(type.typeName)
                }
                3 -> {
                    loadInterstitialAd(type.typeName)
                }
            }
        }
    }

    override fun getItemCount(): Int = typeList.size

    private fun loadInterstitialAd(categoryName: String) {
        if (isAdLoading) return

        isAdLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    Log.d("AdStatus", "Interstitial Ad loaded successfully.")
                    isAdLoading = false

                    interstitialAd?.let { loadedAd ->
                        loadedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                clickCounts = 0
                                openPickupLineActivity(categoryName)
                                Log.d("AdStatus", "Interstitial Ad dismissed.")
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                clickCounts = 0
                                openPickupLineActivity(categoryName)
                                Log.e("AdStatus", "Interstitial Ad failed to show: ${adError.message}")
                            }
                        }
                        loadedAd.show(context as Activity)
                    }
                }

                override fun onAdFailedToLoad(adError: com.google.android.gms.ads.LoadAdError) {
                    interstitialAd = null
                    isAdLoading = false
                    clickCounts = 0
                    openPickupLineActivity(categoryName)
                    Log.e("AdStatus", "Interstitial Ad failed to load: ${adError.message}")
                }
            }
        )
    }

    private fun openPickupLineActivity(categoryName: String) {
        val intent = Intent(context, PickupLineActivity::class.java)
        intent.putExtra("CATEGORY_NAME", categoryName)
        context.startActivity(intent)
    }

    class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtType: TextView = itemView.findViewById(R.id.txtType)
        val imgType: ImageView = itemView.findViewById(R.id.imgType)
    }
}


