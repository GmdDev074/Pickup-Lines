package com.example.pickuplines.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pickuplines.R

class OnboardingAdapter(private val context: Context) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    private val images = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4
    )

    private val headings = listOf(
        "Are you French? Because Eiffel for you!",
        "Do you have a map? I keep getting lost in your eyes.",
        "Is your name Google? Because you have everything I've been searching for.",
        "Are you a magician? Because whenever I look at you, everyone else disappears."
    )

    private val descriptions = listOf(
        "This is my way of saying 'hello'! 😄",
        "I hope you can find your way to my heart as well. 😍",
        "You're my answer to every question. 😏",
        "I must be under a spell. You're enchanting! ✨"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_item, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val image = images[position]
        val heading = headings[position]
        val description = descriptions[position]

        holder.imageView.setImageResource(image)
        holder.headingText.text = heading
        holder.descriptionText.text = description
    }

    override fun getItemCount(): Int = images.size

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val headingText: TextView = itemView.findViewById(R.id.headingText)
        val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
    }
}

