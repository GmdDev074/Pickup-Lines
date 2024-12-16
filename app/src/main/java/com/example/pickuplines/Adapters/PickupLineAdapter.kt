package com.example.pickuplines.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pickuplines.DataClasses.PickupLine
import com.example.pickuplines.R
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PickupLineAdapter(
    private val context: Context,
    private val pickupLines: List<PickupLine>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_PICKUP_LINE = 0
    private val VIEW_TYPE_NATIVE_AD = 1

    private val likedPickupLines: MutableList<String> = mutableListOf()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("LikedPickupLines", Context.MODE_PRIVATE)
    private val adUnitId = "ca-app-pub-3940256099942544/2247696110"

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

            holder.saveButton.setOnClickListener {
                savePickupLineAsImage(holder.itemView)
                Log.d("PickupLineAdapter", "Save button clicked for pickup line: ${pickupLine.line}")
            }
            holder.shareButton.setOnClickListener {
                sharePickupLineAsText(pickupLine.line)
            }
            holder.likeButton.setOnClickListener {
                likePickupLine(pickupLine.line)
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

    @SuppressLint("CutPasteId")
    private fun savePickupLineAsImage(view: View) {
        val likeButtonVisibility = view.findViewById<ImageView>(R.id.like_button).visibility
        val shareButtonVisibility = view.findViewById<ImageView>(R.id.share_button).visibility
        val saveButtonVisibility = view.findViewById<ImageView>(R.id.download_button).visibility
        val copyButtonVisibility = view.findViewById<ImageView>(R.id.copy_button).visibility

        val cardViewVisibility = view.findViewById<androidx.cardview.widget.CardView>(R.id.cardView).visibility
        val txtDownloadVisibility = view.findViewById<TextView>(R.id.download_text).visibility
        val txtShareVisibility = view.findViewById<TextView>(R.id.share_text).visibility
        val txtLikeVisibility = view.findViewById<TextView>(R.id.like_text).visibility
        val txtCopyVisibility = view.findViewById<TextView>(R.id.copy_text).visibility

        view.findViewById<ImageView>(R.id.like_button).visibility = View.INVISIBLE
        view.findViewById<ImageView>(R.id.share_button).visibility = View.INVISIBLE
        view.findViewById<ImageView>(R.id.download_button).visibility = View.INVISIBLE
        view.findViewById<ImageView>(R.id.copy_button).visibility = View.INVISIBLE
        view.findViewById<androidx.cardview.widget.CardView>(R.id.cardView).visibility = View.INVISIBLE
        view.findViewById<TextView>(R.id.download_text).visibility = View.INVISIBLE
        view.findViewById<TextView>(R.id.share_text).visibility = View.INVISIBLE
        view.findViewById<TextView>(R.id.like_text).visibility = View.INVISIBLE
        view.findViewById<TextView>(R.id.copy_text).visibility = View.INVISIBLE

        val bitmap = createBitmapFromView(view)

        view.findViewById<ImageView>(R.id.like_button).visibility = likeButtonVisibility
        view.findViewById<ImageView>(R.id.share_button).visibility = shareButtonVisibility
        view.findViewById<ImageView>(R.id.download_button).visibility = saveButtonVisibility
        view.findViewById<ImageView>(R.id.copy_button).visibility = copyButtonVisibility
        view.findViewById<androidx.cardview.widget.CardView>(R.id.cardView).visibility = cardViewVisibility
        view.findViewById<TextView>(R.id.download_text).visibility = txtDownloadVisibility
        view.findViewById<TextView>(R.id.share_text).visibility = txtShareVisibility
        view.findViewById<TextView>(R.id.like_text).visibility = txtLikeVisibility
        view.findViewById<TextView>(R.id.copy_text).visibility = txtCopyVisibility

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageToMediaStore(bitmap)
        } else {
            saveImageToExternalStorage(bitmap)
        }
    }


    private fun saveImageToMediaStore(bitmap: Bitmap) {
        try {
            val contentValues = android.content.ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "pickup_line_${System.currentTimeMillis()}.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Pickup Lines")
            }

            val imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            imageUri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    Toast.makeText(context, "Saved to Photos!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            Log.e("PickupLineAdapter", "Error saving image", e)
            Toast.makeText(context, "Failed to save image.", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, "Saved to Photos: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("PickupLineAdapter", "Error saving image", e)
            Toast.makeText(context, "Failed to save image.", Toast.LENGTH_SHORT).show()
        }
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
        Toast.makeText(context, "Liked: $line", Toast.LENGTH_SHORT).show()
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
    }

    /*class NativeAdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {  //every time new ad will be loads
        private val progressBar: ProgressBar = itemView.findViewById(R.id.ad_progress_bar)
        private val adHeadline: TextView = itemView.findViewById(R.id.ad_headline)
        private val adBody: TextView = itemView.findViewById(R.id.ad_body)
        private val callToAction: Button = itemView.findViewById(R.id.ad_call_to_action)
        private val adIcon: ImageView = itemView.findViewById(R.id.ad_app_icon)

        fun loadNativeAd(context: Context, adUnitId: String) {
            progressBar.visibility = View.VISIBLE
            adHeadline.text = ""
            adBody.text = ""
            callToAction.text = ""
            adIcon.setImageDrawable(null)

            val adLoader = AdLoader.Builder(context, adUnitId)
                .forNativeAd { nativeAd ->
                    progressBar.visibility = View.GONE

                    adHeadline.text = nativeAd.headline
                    adBody.text = nativeAd.body
                    callToAction.text = nativeAd.callToAction
                    adIcon.setImageDrawable(nativeAd.icon?.drawable)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        super.onAdFailedToLoad(adError)
                        progressBar.visibility = View.GONE
                    }
                })
                .build()

            adLoader.loadAd(AdRequest.Builder().build())
        }
    }*/

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
                displayAd(loadedNativeAd!!)
            } else {
                progressBar.visibility = View.VISIBLE
                adHeadline.text = ""
                adBody.text = ""
                callToAction.text = ""
                adIcon.setImageDrawable(null)

                val adLoader = AdLoader.Builder(context, adUnitId)
                    .forNativeAd { nativeAd ->
                        progressBar.visibility = View.GONE
                        loadedNativeAd = nativeAd
                        displayAd(nativeAd)
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            super.onAdFailedToLoad(adError)
                            progressBar.visibility = View.GONE
                        }
                    })
                    .build()

                adLoader.loadAd(AdRequest.Builder().build())
            }
        }

        private fun displayAd(nativeAd: NativeAd) {
            adHeadline.text = nativeAd.headline
            adBody.text = nativeAd.body
            callToAction.text = nativeAd.callToAction
            adIcon.setImageDrawable(nativeAd.icon?.drawable)
        }
    }


}
