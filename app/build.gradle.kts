plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.pickuplines"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pickuplines"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("com.google.android.gms:play-services-ads:22.3.0")
    implementation("com.google.android.gms:play-services-ads:22.4.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("androidx.core:core-ktx:1.15.0")

    //app open ad
    implementation("androidx.lifecycle:lifecycle-process:2.8.3")
    implementation("com.github.1902shubh:AdmobOpenAds:1.0.1")
}