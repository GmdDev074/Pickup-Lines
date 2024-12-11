package com.example.pickuplines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TypeAdapter(
    private val context: Context,
    private val typeList: List<TypeModel>
) : RecyclerView.Adapter<TypeAdapter.TypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return TypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = typeList[position]
        holder.txtType.text = type.typeName
        holder.imgType.setImageResource(type.imageResId)
    }

    override fun getItemCount(): Int = typeList.size

    class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtType: TextView = itemView.findViewById(R.id.txtType)
        val imgType: ImageView = itemView.findViewById(R.id.imgType)
    }
}
