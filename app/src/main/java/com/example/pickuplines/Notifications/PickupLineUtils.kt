package com.example.pickuplines.Utils

import com.example.pickuplines.DataClasses.PickupLine
import com.example.pickuplines.DataClasses.PickupLinesData
import com.example.pickuplines.Models.TypeModel
import com.example.pickuplines.R

// Define categories and associated drawable/color
private val typeList = listOf(
    TypeModel("Bad", R.drawable.bad, R.color.color1),
    TypeModel("Love", R.drawable.love, R.color.color2),
    TypeModel("Cute", R.drawable.cute, R.color.color3),
    TypeModel("Clever", R.drawable.clever, R.color.color4),
    TypeModel("Dirty", R.drawable.dirty, R.color.color5),
    TypeModel("Food", R.drawable.foodie, R.color.color6),
    TypeModel("Cheesy", R.drawable.chessy, R.color.color7),
    TypeModel("Funny", R.drawable.funny, R.color.color8),
    TypeModel("Romantic", R.drawable.romantic, R.color.color9),
    TypeModel("Sad", R.drawable.sad, R.color.color10),
    TypeModel("Flirty", R.drawable.flirt, R.color.color11),
    TypeModel("Classic", R.drawable.classic, R.color.color12),
    TypeModel("Compliment", R.drawable.compliment, R.color.color13)
)

// Function to get pickup lines for a specific category
private fun getPickupLinesForCategory(category: String): List<PickupLine> {
    return when (category) {
        "Bad" -> PickupLinesData.badLines
        "Love" -> PickupLinesData.loveLines
        "Cute" -> PickupLinesData.cuteLines
        "Clever" -> PickupLinesData.cleverLines
        "Dirty" -> PickupLinesData.dirtyLines
        "Food" -> PickupLinesData.foodLines
        "Cheesy" -> PickupLinesData.cheesyLines
        "Funny" -> PickupLinesData.funnyLines
        "Romantic" -> PickupLinesData.romanticLines
        "Sad" -> PickupLinesData.sadLines
        "Flirty" -> PickupLinesData.flirtyLines
        "Classic" -> PickupLinesData.classicLines
        "Compliment" -> PickupLinesData.complimentLines
        else -> emptyList()
    }
}

// Function to randomly pick a line from a random categoryf
fun getRandomPickupLine(): String {
    val randomType = typeList.random()
    val pickupLines = getPickupLinesForCategory(randomType.typeName)

    return if (pickupLines.isNotEmpty()) {
        pickupLines.random().line
    } else {
        "Here's a line for you: Try again later!"
    }
}
