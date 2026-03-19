package com.example.androidbasics

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class IntentActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intent2)

        //Intialize Buttons
        val btnSms: Button = findViewById(R.id.btnSms)
        val btnDial : Button = findViewById(R.id.btnDial)
        val btnWhatsApp: Button = findViewById(R.id.btnWhatsApp)
        val btnINstagram: Button = findViewById(R.id.btnInstagram)
        val btnEmail: Button = findViewById(R.id.btnEmail)


        //1. SMS Intent
        btnSms.setOnClickListener {
            val intent= Intent(Intent.ACTION_SENDTO).apply {
                data= Uri.parse("smsto:6230266308")
                putExtra("sms_body","Hi, check this out!")
            }

            startActivity(intent)
        }

        // 2. Telephone Dialer Intent
        btnDial.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:1234567890")
            }
            startActivity(intent)
        }


        // 3. WhatsApp Intent
        btnWhatsApp.setOnClickListener {
            val phoneNumber = "+1234567890" // Include country code
            val message = "Hello from my Android app!"
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        }

        // 4. Instagram Intent (with fallback to Browser)
        btnINstagram.setOnClickListener {
            val username= "google"
            val appUri = Uri.parse("http://instagram.com/_u/$username")
            val browserUri = Uri.parse("http://instagram.com/$username")

            val intent = Intent(Intent.ACTION_VIEW, appUri).apply{
                setPackage("com.instagram.android")
            }

            try {
                startActivity(intent)
            }catch (e: ActivityNotFoundException){
                //If the app isn't installed, open in the web broswer
                startActivity(Intent(Intent.ACTION_VIEW, browserUri))
            }
        }

        // 5. Email Intent Logic
        btnEmail.setOnClickListener {
            val emailRecipient = "tanishlalia03@gmail.com"
            val emailSubject = "Testing Android Intent"
            val emailBody = "Hello, buddy."

            // Use Intent.ACTION_SENDTO with mailto: to filter only email apps
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emailRecipient)) // Must be a String Array
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                putExtra(Intent.EXTRA_TEXT, emailBody)
            }

            try {
                // Use a chooser so the user can select their favorite email app
                startActivity(Intent.createChooser(intent, "Select Email App"))
            } catch (e: Exception) {
                Toast.makeText(this, "No Email app found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}