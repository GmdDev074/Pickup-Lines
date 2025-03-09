package com.example.pickuplines.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pickuplines.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdManager(private val context: Context) {
    private var rewardedAd: RewardedAd? = null
    private var nativeAd: NativeAd? = null

    private val rewardedAdUnitId: String = context.getString(R.string.rewarded_ad_unit_id)
    private val nativeAdUnitId: String = context.getString(R.string.native_ad_unit_id)

    init {
        MobileAds.initialize(context)
    }

    fun loadRewardedAd(onAdLoaded: () -> Unit, onAdFailed: () -> Unit) {
        RewardedAd.load(
            context,
            rewardedAdUnitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    onAdLoaded()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    Toast.makeText(context, "Failed to load rewarded ad", Toast.LENGTH_SHORT).show()
                    onAdFailed()
                }
            }
        )
    }

    fun showRewardedAd(activity: Activity, onAdComplete: () -> Unit) {
        rewardedAd?.show(activity) {
            // User watched the ad; grant reward
            onAdComplete()
        } ?: Toast.makeText(context, "No rewarded ad available", Toast.LENGTH_SHORT).show()
    }

    fun loadNativeAd(onAdLoaded: (NativeAd) -> Unit) {
        val adLoader = AdLoader.Builder(context, nativeAdUnitId)
            .forNativeAd { ad ->
                nativeAd = ad
                onAdLoaded(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    nativeAd = null
                    Toast.makeText(context, "Failed to load native ad", Toast.LENGTH_SHORT).show()
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun getNativeAd(): NativeAd? {
        return nativeAd
    }
}
