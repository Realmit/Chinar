package com.oookraton.chinar

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


class MenuAdapterOrder(
    private val context: Context,
    private var items: MutableList<MenuItem>,
    private val cart: MutableMap<MenuItem, Int>,
    private val onCartChanged: () -> Unit
) : RecyclerView.Adapter<MenuAdapterOrder.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemIcon: ImageView = view.findViewById(R.id.itemIcon)
        val itemTitle: TextView = view.findViewById(R.id.itemTitle)
        val itemDescription: TextView = view.findViewById(R.id.itemDescription)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
        val buttonMinus: Button = view.findViewById(R.id.buttonMinus)
        val buttonPlus: Button = view.findViewById(R.id.buttonPlus)
        val textQuantity: TextView = view.findViewById(R.id.textQuantity)
        val quantityGroup: LinearLayout = view.findViewById(R.id.quantityGroup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_menu_order, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val quantity = cart[item] ?: 0
        holder.itemTitle.text = item.title
        holder.itemDescription.text = item.description
        holder.itemPrice.text = item.price
        holder.itemIcon.setImageResource(item.iconRes)
        holder.textQuantity.setText(quantity.toString())
        holder.quantityGroup.visibility = View.GONE

        // Show minus button and group when + is pressed

        if (quantity > 0) {
            holder.quantityGroup.visibility = View.VISIBLE
            holder.textQuantity.setText(quantity.toString())
        } else {
            holder.quantityGroup.visibility = View.GONE
        }
        // + Button
        holder.buttonPlus.setOnClickListener {
            val newQty = (cart[item] ?: 0) + 1
            cart[item] = newQty
            holder.textQuantity.setText(newQty.toString())
            holder.quantityGroup.visibility = View.VISIBLE
            onCartChanged()
        }
        // – Button
        holder.buttonMinus.setOnClickListener {
            val current = cart[item] ?: 0
            if (current > 1) {
                val newQty = current - 1
                cart[item] = newQty
                holder.textQuantity.setText(newQty.toString())
            } else {
                cart.remove(item)
                holder.quantityGroup.visibility = View.GONE
            }
            onCartChanged()
        }
        // Allow user to tap and edit quantity directly
        holder.textQuantity.setOnClickListener {
            holder.textQuantity.isFocusableInTouchMode = true
        }
        holder.textQuantity.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val input = holder.textQuantity.text.toString()
                val newQty = if (input.isEmpty()) 1 else input.toIntOrNull() ?: 1
                val clampedQty = if (newQty < 1) 1 else newQty  // Ensure positive
                if (clampedQty == 1) {
                    cart[item] = 1
                } else {
                    cart[item] = clampedQty
                }
                holder.textQuantity.setText(clampedQty.toString())
                holder.quantityGroup.visibility = View.VISIBLE
                holder.textQuantity.isFocusableInTouchMode = false
            }
        }

        holder.itemView.setOnClickListener {
            showItemDetails(item)
        }
    }

    private fun showItemDetails(item: MenuItem) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_menu_details)
        dialog.findViewById<TextView>(R.id.dialogTitle).text = item.title
        dialog.findViewById<TextView>(R.id.dialogDescription).text = item.fullDescription
        dialog.findViewById<TextView>(R.id.dialogPrice).text = item.price
        dialog.findViewById<ImageView>(R.id.dialogImage).setImageResource(item.iconRes)

        dialog.findViewById<TextView>(R.id.dialogCalories).text = item.calories.toString()
        dialog.findViewById<TextView>(R.id.dialogProtein).text = item.protein.toString()
        dialog.findViewById<TextView>(R.id.dialogFat).text = item.fat.toString()
        dialog.findViewById<TextView>(R.id.dialogCarbs).text = item.carbs.toString()

        dialog.findViewById<Button>(R.id.buttonClose).setOnClickListener {
            dialog.dismiss()
        }
        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        dialog.show()
    }
    fun updateItems(newItems: List<MenuItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size
}