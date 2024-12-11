package com.example.pickuplines.Adapters

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

class TypeAdapter(
    private val context: Context,
    private val typeList: List<TypeModel>,
    private val itemClickListener: (String) -> Unit
) : RecyclerView.Adapter<TypeAdapter.TypeViewHolder>() {

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
            Log.d("TypeAdapter", "Item clicked: ${type.typeName}")

            val intent = Intent(context, PickupLineActivity::class.java)
            intent.putExtra("CATEGORY_NAME", type.typeName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = typeList.size

    class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtType: TextView = itemView.findViewById(R.id.txtType)
        val imgType: ImageView = itemView.findViewById(R.id.imgType)
    }
}
