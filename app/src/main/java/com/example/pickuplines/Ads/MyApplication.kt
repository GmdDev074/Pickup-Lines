package com.example.pickuplines.Ads

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import papaya.`in`.admobopenads.AppOpenManager

class MyApplication : Application() {

    lateinit var appOpenManager: AppOpenManager

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        Log.d("MyApp", "Admob initialized")
        appOpenManager = AppOpenManager(this, "ca-app-pub-3940256099942544/9257395921")
        Log.d("MyApp", "AppOpenManager initialized")
    }
}
