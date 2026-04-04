package com.example.androidbasics.androidSDK

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidbasics.R
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startPayment()
    }

    override fun onPaymentSuccess(paymentID: String?) {

        Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
    }


    fun startPayment() {
        val checkout = Checkout()
        checkout.setKeyID("rzp_live_ILgsfZCZoFIKMb")

        try {

            val option = JSONObject()
            option.put("name","Merchant name")
            option.put("description","This is our testing gateway")
            option.put("currency","INR")
            option.put("amount",1*100)


            var prefill = JSONObject()
            prefill.put("email","tanish@gmail.com")

            option.put("prefill",prefill)

            checkout.open(this,option)
        } catch (e: Exception) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }
}