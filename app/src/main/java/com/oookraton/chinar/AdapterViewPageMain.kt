// File: AdapterViewPageMain.kt
package com.oookraton.chinar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AdapterViewPageMain(private val context: Context, private val images: IntArray) :
    RecyclerView.Adapter<AdapterViewPageMain.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
        // Set click listener
        holder.imageView.setOnClickListener {
            Toast.makeText(context, "Image ${position + 1}", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int = images.size
}