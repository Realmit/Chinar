package com.oookraton.chinarDebug
import okhttp3.*
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.content.Intent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable

class OrderFinal : AppCompatActivity() {
    private var phonePrefix = "8"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_final)
        val textBookingFinal = findViewById<TextView>(R.id.textBookingFinal)
        val textBookingFinalDebug = findViewById<TextView>(R.id.textBookingFinalDebug)
        val bookingData = intent.getStringExtra("order_details") ?: ""
        var bookingDataFinal = ""
        textBookingFinal.text = bookingData
        // Get inputs field
        val phoneInput = findViewById<TextInputEditText>(R.id.editPhoneNumberEvent)
        val surnameInput = findViewById<TextInputEditText>(R.id.editSurnameEvent)
        val nameInput = findViewById<TextInputEditText>(R.id.editNameEvent)
        val patronymicInput = findViewById<TextInputEditText>(R.id.editPatronymicEvent)
        // Set initial value with 8
        phoneInput.setText(phonePrefix)
        phoneInput.setSelection(phonePrefix.length)
        // Back button
        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }
        // Next button - with validation
        val nextButton = findViewById<Button>(R.id.buttonNext)
        nextButton.setOnClickListener {
            val phoneNumber = phoneInput.text.toString().filter { it.isDigit() }
            // Validate name,surname and phone number
            if(surnameInput.text.toString() == "")
            {
                findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputSurname)
                    .error = "Введите фамилию"
            }
            else findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputSurname).error = ""
            if(nameInput.text.toString() == "")
            {
                findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputName)
                    .error = "Введите имя"
            }
            else findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputName).error = ""
            if(phoneNumber.length == 11 && (phoneNumber.startsWith("8") || phoneNumber.startsWith("7")) ||
                phoneNumber.length == 12 && phoneNumber.startsWith("+7"))
                findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputPhoneNumber).error = ""
            else findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.inputPhoneNumber)
                .error = "Введите корректный номер телефона"
            if ((phoneNumber.length == 11 && (phoneNumber.startsWith("8") || phoneNumber.startsWith("7")) ||
                phoneNumber.length == 12 && phoneNumber.startsWith("+7"))
                && surnameInput.text.toString() != "" && nameInput.text.toString() != "") {
                bookingDataFinal =  "${surnameInput.text},${nameInput.text},${patronymicInput.text}|${phoneInput.text}|" + bookingData
                textBookingFinalDebug.text = bookingDataFinal
                sendBookingData(bookingDataFinal)
                //val intent = Intent(this, ConfirmationPage::class.java)
                //intent.putExtra("phone_number", phoneNumber)
                //intent.putExtra("order_details", bookingData)
                //startActivity(intent)
            }
        }

    }
    private fun sendBookingData(bookingDataFinal: String) {
        val client = OkHttpClient()

        val serverUrl = "https://shelby-unstretchable-promisingly.ngrok-free.dev/submit-booking"

        val requestBody = bookingDataFinal.toRequestBody("text/plain".toMediaType())

        val request = Request.Builder()
            .url(serverUrl)
            .addHeader("Content-Type", "text/plain")
            .addHeader("User-Agent", "ChinarApp/1.0")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@OrderFinal,
                        "Ошибка сети: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        showSuccessDialog()
                    } else {
                        Toast.makeText(
                            this@OrderFinal,
                            "Ошибка сервера: ${response.code}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }
    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_success)

        // Set dialog properties
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        // Set up confirm button
        val confirmButton = dialog.findViewById<Button>(R.id.buttonConfirmSuccess)
        confirmButton.setOnClickListener {
            dialog.dismiss()
            // Navigate to main menu
            val intent = Intent(this, Mainpage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        dialog.show()
    }
}