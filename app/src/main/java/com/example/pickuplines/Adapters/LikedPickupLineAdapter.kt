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
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pickuplines.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

class LikedPickupLineAdapter(
    private val context: Context,
    private val likedLines: MutableList<String>
) : RecyclerView.Adapter<LikedPickupLineAdapter.LikedPickupLineViewHolder>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("LikedPickupLines", Context.MODE_PRIVATE)

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedPickupLineViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pickup_line, parent, false)
        return LikedPickupLineViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikedPickupLineViewHolder, position: Int) {
        val pickupLine = likedLines[position]
        holder.pickupLineText.text = pickupLine
        holder.likeButton.setImageResource(R.drawable.ic_like)

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
            savePickupLineAsImage(holder.itemView)
        }

        holder.shareButton.setOnClickListener {
            sharePickupLineAsText(pickupLine)
        }

        holder.likeButton.setOnClickListener {
            unlikePickupLine(pickupLine, position)
        }

        holder.copyButton.setOnClickListener {
            copyPickupLine(pickupLine)
        }
    }

    override fun getItemCount(): Int = likedLines.size

    @SuppressLint("CutPasteId")
    private fun savePickupLineAsImage(view: View) {
        val likeButtonVisibility = view.findViewById<ImageView>(R.id.like_button).visibility
        val shareButtonVisibility = view.findViewById<ImageView>(R.id.share_button).visibility
        val saveButtonVisibility = view.findViewById<ImageView>(R.id.download_button).visibility
        val copyButtonVisibility = view.findViewById<ImageView>(R.id.copy_button).visibility
        val save = view.findViewById<TextView>(R.id.download_text).visibility
        val share = view.findViewById<TextView>(R.id.share_text).visibility
        val like = view.findViewById<TextView>(R.id.like_text).visibility
        val copy = view.findViewById<TextView>(R.id.copy_text).visibility
        val card= view.findViewById<androidx.cardview.widget.CardView>(R.id.cardView).visibility

        view.findViewById<ImageView>(R.id.like_button).visibility = View.INVISIBLE
        view.findViewById<ImageView>(R.id.share_button).visibility = View.INVISIBLE
        view.findViewById<ImageView>(R.id.download_button).visibility = View.INVISIBLE
        view.findViewById<ImageView>(R.id.copy_button).visibility = View.INVISIBLE

        view.findViewById<TextView>(R.id.download_text).visibility = View.INVISIBLE
        view.findViewById<TextView>(R.id.share_text).visibility = View.INVISIBLE
        view.findViewById<TextView>(R.id.like_text).visibility = View.INVISIBLE
        view.findViewById<TextView>(R.id.copy_text).visibility = View.INVISIBLE
        view.findViewById<androidx.cardview.widget.CardView>(R.id.cardView).visibility = View.INVISIBLE

        val bitmap = createBitmapFromView(view)

        view.findViewById<ImageView>(R.id.like_button).visibility = likeButtonVisibility
        view.findViewById<ImageView>(R.id.share_button).visibility = shareButtonVisibility
        view.findViewById<ImageView>(R.id.download_button).visibility = saveButtonVisibility
        view.findViewById<ImageView>(R.id.copy_button).visibility = copyButtonVisibility

        view.findViewById<TextView>(R.id.download_text).visibility = save
        view.findViewById<TextView>(R.id.share_text).visibility = share
        view.findViewById<TextView>(R.id.like_text).visibility = like
        view.findViewById<TextView>(R.id.copy_text).visibility = copy
        view.findViewById<androidx.cardview.widget.CardView>(R.id.cardView).visibility = card

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageToMediaStore(bitmap)
        } else {
            saveImageToExternalStorage(bitmap)
        }
    }

    private fun saveImageToMediaStore(bitmap: Bitmap) {
        val contentValues = android.content.ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "pickup_line_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Pickup Lines")
        }

        val imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            imageUri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    Toast.makeText(context, "Saved to Photos!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Failed to save image.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToExternalStorage(bitmap: Bitmap) {
        val folder = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Pickup Lines")
        if (!folder.exists()) folder.mkdirs()
        val file = File(folder, "pickup_line_${System.currentTimeMillis()}.png")

        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            //Toast.makeText(context, "Saved to Photos: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to save image.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sharePickupLineAsText(line: String) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, line)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Pickup Line"))
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to share pickup line.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun unlikePickupLine(line: String, position: Int) {
        likedLines.removeAt(position)
        val editor = sharedPreferences.edit()

        val updatedLikedLines = likedLines.toSet()
        editor.putStringSet("LikedLines", updatedLikedLines)
        editor.apply()
        notifyItemRemoved(position)
        Toast.makeText(context, "Unliked:", Toast.LENGTH_SHORT).show()
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

    class LikedPickupLineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pickupLineText: TextView = itemView.findViewById(R.id.pickup_line_text)
        val saveButton: ImageView = itemView.findViewById(R.id.download_button)
        val shareButton: ImageView = itemView.findViewById(R.id.share_button)
        val likeButton: ImageView = itemView.findViewById(R.id.like_button)
        val copyButton: ImageView = itemView.findViewById(R.id.copy_button)
        val cardViewBackground: ImageView = itemView.findViewById(R.id.cardViewBackground)
        val constraintLayout: androidx.constraintlayout.widget.ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
    }
}