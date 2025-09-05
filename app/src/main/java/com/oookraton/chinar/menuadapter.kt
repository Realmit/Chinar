package com.oookraton.chinar

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(private val context: Context, private var items: MutableList<MenuItem>) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    fun updateItems(newItems: List<MenuItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemIcon: ImageView = view.findViewById(R.id.itemIcon)
        val itemTitle: TextView = view.findViewById(R.id.itemTitle)
        val itemDescription: TextView = view.findViewById(R.id.itemDescription)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemTitle.text = item.title
        holder.itemDescription.text = item.description
        holder.itemPrice.text = item.price
        holder.itemIcon.setImageResource(item.iconRes)
        holder.itemView.setOnClickListener {
            showItemDetails(item)
        }
    }
    override fun getItemCount() = items.size
    // Function to show custom dialog
    private fun showItemDetails(item: MenuItem) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_menu_details)
        dialog.setCanceledOnTouchOutside(false) // Prevent dismiss by tapping outside
        dialog.setCancelable(false)

        // Bind data
        dialog.findViewById<TextView>(R.id.dialogTitle).text = item.title
        dialog.findViewById<TextView>(R.id.dialogDescription).text = item.fullDescription
        dialog.findViewById<TextView>(R.id.dialogPrice).text = item.price
        dialog.findViewById<ImageView>(R.id.dialogImage).setImageResource(item.iconRes)

        // Close button
        dialog.findViewById<Button>(R.id.buttonClose).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}