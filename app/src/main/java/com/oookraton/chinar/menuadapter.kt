package com.oookraton.chinar

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
data class MenuItem(
    val title: String,
    val description: String,
    val price: String,
    val iconRes: Int,
    val fullDescription: String,
    val category: String,
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val mass: Int,
    val availability: Boolean
)
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

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog.setContentView(R.layout.dialog_menu_details)
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false) // Prevent dismiss by tapping outside
        dialog.setCancelable(false)
        // Bind data
        dialog.findViewById<TextView>(R.id.dialogTitle).text = item.title
        dialog.findViewById<TextView>(R.id.dialogDescription).text = item.fullDescription
        dialog.findViewById<TextView>(R.id.dialogPrice).text = item.price
        dialog.findViewById<ImageView>(R.id.dialogImage).setImageResource(item.iconRes)
        dialog.findViewById<TextView>(R.id.dialogCalories).text = item.calories.toString()
        dialog.findViewById<TextView>(R.id.dialogCalories).text = item.calories.toString()
        dialog.findViewById<TextView>(R.id.dialogProtein).text = item.protein.toString()
        dialog.findViewById<TextView>(R.id.dialogFat).text = item.fat.toString()
        dialog.findViewById<TextView>(R.id.dialogCarbs).text = item.carbs.toString()
        dialog.findViewById<TextView>(R.id.dialogMass).text = item.mass.toString()
        // Close button
        dialog.findViewById<Button>(R.id.buttonClose).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}