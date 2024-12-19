package com.example.pickuplines.Adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pickuplines.DataClasses.PickupLine
import com.example.pickuplines.Notifications.showNotification
import com.example.pickuplines.R
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

class PickupLineAdapter(
    private val context: Context,
    private val pickupLines: List<PickupLine>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_PICKUP_LINE = 0
    private val VIEW_TYPE_NATIVE_AD = 1

    private val likedPickupLines: MutableList<String> = mutableListOf()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("LikedPickupLines", Context.MODE_PRIVATE)
    private val adUnitId = "ca-app-pub-3940256099942544/2247696110"

    private val backgroundImages = listOf(
        R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4,
        R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8,
        R.drawable.img9, R.drawable.img10, R.drawable.img11, R.drawable.img12,
        R.drawable.img13, R.drawable.img14, R.drawable.img15, R.drawable.img17,
        R.drawable.img18, R.drawable.img19, R.drawable.img20, R.drawable.img21,
        R.drawable.img22, R.drawable.img23, R.drawable.img24, R.drawable.img25,
        R.drawable.img26, R.drawable.img27, R.drawable.img28, R.drawable.img29,
        R.drawable.img30, R.drawable.img31, R.drawable.img32, R.drawable.img33,
        R.drawable.img34, R.drawable.img35, R.drawable.img36, R.drawable.img37,
        R.drawable.img38, R.drawable.img39
    )

    private val textColors = listOf(
        R.color.color1, R.color.color2, R.color.color3,
        R.color.color4, R.color.color5, R.color.color6,
        R.color.color7, R.color.color8, R.color.color9,
        R.color.color10, R.color.color11, R.color.color12,
        R.color.color13
    )

    override fun getItemViewType(position: Int): Int {
        return if ((position + 1) % 4 == 0) VIEW_TYPE_NATIVE_AD else VIEW_TYPE_PICKUP_LINE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_PICKUP_LINE) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_pickup_line, parent, false)
            PickupLineViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_native_ad, parent, false)
            NativeAdViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PickupLineViewHolder) {
            val pickupLine = pickupLines[position - position / 4]
            holder.pickupLineText.text = pickupLine.line

            // Initially set random background image
            val randomImageUrl = backgroundImages[Random.nextInt(backgroundImages.size)]
            Glide.with(context)
                .load(randomImageUrl)
                .centerCrop()
                .into(holder.cardViewBackground)

            var clickCounter: Int = 0

            holder.itemView.setOnClickListener {
                holder.itemView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

                clickCounter++

                if (clickCounter % 4 == 0) {
                    val randomImageUrl = backgroundImages[Random.nextInt(backgroundImages.size)]
                    Glide.with(context)
                        .load(randomImageUrl)
                        .centerCrop()
                        .into(holder.cardViewBackground)

                    holder.cardViewBackground.visibility = View.VISIBLE

                    val randomColor = textColors[Random.nextInt(textColors.size)]
                    holder.constraintLayout.setBackgroundColor(ContextCompat.getColor(context, randomColor))
                } else if (clickCounter % 2 == 0) {
                    val randomImageUrl = backgroundImages[Random.nextInt(backgroundImages.size)]
                    Glide.with(context)
                        .load(randomImageUrl)
                        .centerCrop()
                        .into(holder.cardViewBackground)

                    holder.cardViewBackground.visibility = View.VISIBLE
                    holder.constraintLayout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
                } else {
                    val randomColor = textColors[Random.nextInt(textColors.size)]
                    holder.constraintLayout.setBackgroundColor(ContextCompat.getColor(context, randomColor))

                    holder.cardViewBackground.visibility = View.GONE
                }
            }


            holder.saveButton.setOnClickListener {
                // Get references to views
                val txtAppName = holder.itemView.findViewById<TextView>(R.id.app_name)
                val cardView = holder.itemView.findViewById<androidx.cardview.widget.CardView>(R.id.cardView)

                if (txtAppName.visibility == View.GONE) {
                    // If app_name is already hidden for this item, download without watermark
                    savePickupLineAsImage(holder.itemView, cardView, txtAppNameHidden = true)
                    showNotification(
                        context,
                        "Image Saved",
                        "The image has been successfully saved without the app name."
                    )
                } else {
                    // Show the custom dialog to decide on watermark removal
                    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_watch_ad, null)
                    val dialog = AlertDialog.Builder(context)
                        .setView(dialogView)
                        .setCancelable(false)
                        .create()

                    val watchAdButton = dialogView.findViewById<Button>(R.id.watch_ad_button)
                    val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
                    val closeButton = dialogView.findViewById<ImageView>(R.id.closeImage)

                    closeButton.setOnClickListener {
                        dialog.dismiss()
                    }

                    watchAdButton.setOnClickListener {
                        // Handle the "Watch Ad" logic
                        showRewardAd {
                            // Callback when the ad finishes
                            txtAppName.visibility = View.GONE // Hide app_name for this item
                            savePickupLineAsImage(holder.itemView, cardView, txtAppNameHidden = true)
                            showNotification(
                                context,
                                "Image Saved",
                                "The image has been successfully saved without the app name."
                            )
                            dialog.dismiss()
                        }
                    }

                    cancelButton.setOnClickListener {
                        // Handle the "Cancel" logic
                        savePickupLineAsImage(holder.itemView, cardView, txtAppNameHidden = false)
                        showNotification(
                            context,
                            "Image Saved",
                            "The image has been successfully saved with the app name visible."
                        )
                        dialog.dismiss()
                    }

                    dialog.show()
                }
            }



            holder.shareButton.setOnClickListener {
                sharePickupLineAsText(pickupLine.line)
            }
            val likedPickupLines: MutableList<String> =
                sharedPreferences.getStringSet("LikedLines", setOf())?.toMutableList() ?: mutableListOf()

            holder.likeButton.setOnClickListener {
                if (likedPickupLines.contains(pickupLine.line)) {
                    likedPickupLines.remove(pickupLine.line)
                    sharedPreferences.edit().putStringSet("LikedLines", likedPickupLines.toSet()).apply()
                    holder.likeButton.setImageResource(R.drawable.favorite_border_24)
                } else {
                    likedPickupLines.add(pickupLine.line)
                    sharedPreferences.edit().putStringSet("LikedLines", likedPickupLines.toSet()).apply()
                    holder.likeButton.setImageResource(R.drawable.ic_like)
                }
            }
            if (likedPickupLines.contains(pickupLine.line)) {
                holder.likeButton.setImageResource(R.drawable.ic_like)
            } else {
                holder.likeButton.setImageResource(R.drawable.favorite_border_24)
            }


            holder.copyButton.setOnClickListener {
                copyPickupLine(pickupLine.line)
            }
        } else if (holder is NativeAdViewHolder) {
            holder.loadNativeAd(context, adUnitId)
        }
    }

    override fun getItemCount(): Int {
        return pickupLines.size + pickupLines.size / 3
    }


    private fun savePickupLineAsImage(view: View, cardView: androidx.cardview.widget.CardView, txtAppNameHidden: Boolean) {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val txtAppName = (view.context as Activity).findViewById<TextView>(R.id.app_name)

        // Adjust visibility of app_name based on the condition
        txtAppName.visibility = if (txtAppNameHidden) View.GONE else View.VISIBLE

        // Temporarily hide the cardView for clean bitmap capture
        cardView.visibility = View.GONE
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveImageToMediaStore(bitmap)
            } else {
                saveImageToExternalStorage(bitmap)
            }
            // Restore visibility after saving the image
            cardView.visibility = View.VISIBLE
        } catch (e: IOException) {
            Toast.makeText(view.context, "Failed to save image.", Toast.LENGTH_SHORT).show()
            // Ensure the visibility is restored in case of failure
            cardView.visibility = View.VISIBLE
        }
    }

    private fun saveImageToMediaStore(bitmap: Bitmap) {
        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "pickup_line_${System.currentTimeMillis()}.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Pickup Lines")
            }

            val imageUri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            imageUri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    Toast.makeText(context, "Image saved to gallery!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            throw e
        }
    }

    private fun saveImageToExternalStorage(bitmap: Bitmap) {
        try {
            val folder = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Pickup Lines")
            if (!folder.exists()) folder.mkdirs()

            val file = File(folder, "pickup_line_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                Toast.makeText(context, "Saved to: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            throw e
        }
    }

    private fun showRewardAd(onAdComplete: () -> Unit) {
        val context = context as Activity
        val txtAppName = context.findViewById<TextView>(R.id.app_name)
        txtAppName.visibility = View.GONE
        val rewardedAd = RewardedAd.load(
            context,
            "ca-app-pub-3940256099942544/5224354917",
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    ad.show(context) {
                        // Handle the reward
                        onAdComplete()
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Toast.makeText(context, "Failed to load ad", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun sharePickupLineAsText(line: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, line)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Pickup Line"))
    }

    private fun likePickupLine(line: String) {
        likedPickupLines.add(line)
        sharedPreferences.edit().putStringSet("LikedLines", likedPickupLines.toSet()).apply()
        Toast.makeText(context, "Liked:", Toast.LENGTH_SHORT).show()
    }

    private fun copyPickupLine(line: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Pickup Line", line)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun createBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    class PickupLineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pickupLineText: TextView = itemView.findViewById(R.id.pickup_line_text)
        val saveButton: ImageView = itemView.findViewById(R.id.download_button)
        val shareButton: ImageView = itemView.findViewById(R.id.share_button)
        val likeButton: ImageView = itemView.findViewById(R.id.like_button)
        val copyButton: ImageView = itemView.findViewById(R.id.copy_button)
        val cardViewBackground: ImageView = itemView.findViewById(R.id.cardViewBackground)
        val constraintLayout: androidx.constraintlayout.widget.ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
    }

    class NativeAdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //same ad will be display on all the views
        private val progressBar: ProgressBar = itemView.findViewById(R.id.ad_progress_bar)
        private val adHeadline: TextView = itemView.findViewById(R.id.ad_headline)
        private val adBody: TextView = itemView.findViewById(R.id.ad_body)
        private val callToAction: Button = itemView.findViewById(R.id.ad_call_to_action)
        private val adIcon: ImageView = itemView.findViewById(R.id.ad_app_icon)

        companion object {
            private var loadedNativeAd: NativeAd? = null
        }

        fun loadNativeAd(context: Context, adUnitId: String) {
            if (loadedNativeAd != null) {
                displayNativeAd()
            } else {
                AdLoader.Builder(context, adUnitId)
                    .forNativeAd { nativeAd ->
                        loadedNativeAd = nativeAd
                        displayNativeAd()
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            super.onAdFailedToLoad(adError)
                        }
                    })
                    .build()
                    .loadAd(AdRequest.Builder().build())
            }
        }

        private fun displayNativeAd() {
            loadedNativeAd?.let { nativeAd ->
                adHeadline.text = nativeAd.headline
                adBody.text = nativeAd.body
                callToAction.text = nativeAd.callToAction
                Glide.with(itemView.context).load(nativeAd.icon?.uri).into(adIcon)
                progressBar.visibility = View.GONE
                itemView.visibility = View.VISIBLE
            }
        }
    }
}
