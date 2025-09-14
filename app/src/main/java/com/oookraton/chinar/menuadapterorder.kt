package com.oookraton.chinar

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
private var isSelfUpdate = false

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
        val textQuantity: EditText = view.findViewById(R.id.textQuantity)
        val quantityGroup: LinearLayout = view.findViewById(R.id.quantityGroup)
        val textSubtotal: TextView = view.findViewById(R.id.textSubtotal)
        var currentTextWatcher: TextWatcher? = null
        var currentItem: MenuItem? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_menu_order, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fun updateSubtotal(item: MenuItem, quantity: Int) {
            val priceValue = parsePrice(item.price)
            val total = priceValue * quantity
            holder.textSubtotal.text = "${formatPrice(total)}₽"
            holder.textSubtotal.visibility = View.VISIBLE
        }

        val item = items[position]
        clearViewHolderState(holder)
        holder.currentItem = item
        holder.itemTitle.text = item.title
        holder.itemDescription.text = item.description
        holder.itemPrice.text = item.price
        holder.itemIcon.setImageResource(item.iconRes)
        val quantity = cart[item] ?: 0
        val quantityInCart = cart[item] ?: 0
        if (quantityInCart > 0) {
            holder.textQuantity.setText(quantityInCart.toString())
            holder.quantityGroup.visibility = View.VISIBLE
            holder.textSubtotal.visibility = View.VISIBLE
            updateSubtotal(item, quantityInCart)
        } else {
            holder.textQuantity.setText("0")
            holder.quantityGroup.visibility = View.GONE
            holder.textSubtotal.visibility = View.GONE
        }

        // Set up listeners for this specific item
        setupListeners(holder, item)

        // Show minus button and group when + is pressed
        if (quantity > 0) {
            holder.quantityGroup.visibility = View.VISIBLE
            holder.textQuantity.setText(quantity.toString())
        } else {
            holder.quantityGroup.visibility = View.GONE
        }

    }

    private fun showItemDetails(item: MenuItem) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_menu_details)
        dialog.findViewById<TextView>(R.id.dialogTitle).text = item.title
        dialog.findViewById<TextView>(R.id.dialogDescription).text = item.fullDescription
        dialog.findViewById<TextView>(R.id.dialogPrice).text = item.price
        dialog.findViewById<ImageView>(R.id.dialogImage).setImageResource(item.iconRes)
        dialog.findViewById<TextView>(R.id.dialogMass).text = item.mass.toString()
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
    private fun parsePrice(price: String): Double {
        return price.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 0.0
    }
    private fun formatPrice(value: Double): String {
        return if (value % 1 == 0.0) value.toInt().toString() else String.format("%.2f", value)
    }
    override fun getItemCount() = items.size
    private fun clearViewHolderState(holder: ViewHolder) {
        // Remove previous TextWatcher if exists
        holder.currentTextWatcher?.let {
            holder.textQuantity.removeTextChangedListener(it)
        }
        holder.currentTextWatcher = null

        // Clear previous click listeners
        holder.buttonPlus.setOnClickListener(null)
        holder.buttonMinus.setOnClickListener(null)
        holder.textQuantity.setOnClickListener(null)
        holder.textQuantity.onFocusChangeListener = null
        holder.itemView.setOnClickListener(null)

        // Reset focus state
        holder.textQuantity.isFocusableInTouchMode = false
        holder.textQuantity.clearFocus()

        // Reset current item reference
        holder.currentItem = null
    }
    private fun updateSubtotal(item: MenuItem, quantity: Int, holder: ViewHolder) {
        val priceValue = parsePrice(item.price)
        val total = priceValue * quantity
        holder.textSubtotal.text = "${formatPrice(total)}₽"
        holder.textSubtotal.visibility = if (quantity > 0) View.VISIBLE else View.GONE
    }
    private fun setupListeners(holder: ViewHolder, item: MenuItem) {

        // + Button
        holder.buttonPlus.setOnClickListener {
            val newQty = (cart[item] ?: 0) + 1
            cart[item] = newQty
            holder.textQuantity.setText(newQty.toString())
            holder.quantityGroup.visibility = View.VISIBLE
            updateSubtotal(item, newQty, holder)
            onCartChanged()
        }

        // - Button
        holder.buttonMinus.setOnClickListener {
            val current = cart[item] ?: 0
            if (current > 1) {
                val newQty = current - 1
                cart[item] = newQty
                holder.textQuantity.setText(newQty.toString())
                updateSubtotal(item, newQty, holder)
            } else {
                cart.remove(item)
                holder.quantityGroup.visibility = View.GONE
                holder.textSubtotal.visibility = View.GONE
            }
            onCartChanged()
        }

        // Create new TextWatcher for this item
        val textWatcher = object : TextWatcher {
            private var lastEmittedValue = cart[item] ?: 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isSelfUpdate) return

                // Safety check: ensure we're still bound to the correct item
                if (holder.currentItem != item) return

                val input = s.toString().trim()
                val newQty = if (input.isEmpty()) 0 else input.toIntOrNull() ?: 0

                if (newQty != lastEmittedValue && newQty > 0) {
                    cart[item] = newQty
                    lastEmittedValue = newQty
                    updateSubtotal(item, newQty, holder)
                    onCartChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Safety check
                if (holder.currentItem != item) return

                val input = s.toString().trim()
                val current = cart[item] ?: 0

                if (input.isEmpty() && current > 0) {
                    isSelfUpdate = true
                    s?.clear()
                    s?.append(current.toString())
                    isSelfUpdate = false
                }
            }
        }

        // Store and add the TextWatcher
        holder.currentTextWatcher = textWatcher
        holder.textQuantity.addTextChangedListener(textWatcher)

        // Other listeners...
        holder.textQuantity.setOnClickListener {
            holder.textQuantity.isFocusableInTouchMode = true
        }

        holder.textQuantity.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (holder.currentItem != item) return@OnFocusChangeListener

            if (!hasFocus) {
                val input = holder.textQuantity.text.toString().trim()
                val current = cart[item] ?: 0
                val newQty = input.toIntOrNull()

                when {
                    newQty == null || newQty < 1 -> {
                        isSelfUpdate = true
                        holder.textQuantity.setText(current.toString())
                        isSelfUpdate = false
                    }
                    newQty != current -> {
                        cart[item] = newQty
                        updateSubtotal(item, newQty, holder)
                        onCartChanged()
                    }
                }
                holder.textQuantity.isFocusableInTouchMode = false
            } else {
                holder.textQuantity.selectAll()
            }
        }

        holder.itemView.setOnClickListener {
            showItemDetails(item)
        }
    }
}