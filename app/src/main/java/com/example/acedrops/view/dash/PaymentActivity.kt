package com.example.acedrops.view.dash

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.acedrops.R
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.ProgressDialog
import com.example.acedrops.view.dash.profile.AddressFragment.Companion.addressId
import com.example.acedrops.view.dash.profile.AddressFragment.Companion.orderFrom
import com.example.acedrops.view.dash.profile.AddressFragment.Companion.product
import com.example.acedrops.view.dash.profile.AddressFragment.Companion.quantity
import com.example.acedrops.view.dash.profile.AddressFragment.Companion.totalAmount
import com.example.acedrops.viewmodel.OrderViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    private val orderViewModel: OrderViewModel by viewModels()
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Checkout.preload(applicationContext)

        progressDialog = ProgressDialog.progressDialog(this)

        makePayment()

    }

    private fun makePayment() {
        try {
            val co = Checkout()
            Checkout.preload(this)
            co.setKeyID("rzp_test_dwzZGLl7hFzDwt")
            co.setImage(R.drawable.acedrop_logo)
            val options = JSONObject()
            options.put("name", "Acedrop")
            options.put("description", "Payment")
            //You can omit the image option to fetch the image from dashboard
//            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#1983FF")
            options.put("currency", "INR")
            //    options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount", totalAmount * 100)//pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)
            options.put("send_sms_hash", true)


//            val prefill = JSONObject()
//            prefill.put("email", "guptasg300@gmail.com")
//            prefill.put("contact", "7897468764")
//
//            options.put("prefill", prefill)
            co.open(this, options)

        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment is successful", Toast.LENGTH_SHORT).show()
        when (orderFrom) {
            "Cart" -> orderCart()
            "Product" -> orderProduct()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.w("PAYMENT ACTIVITY", "onPaymentError: " + p1.toString())
        dialog(R.layout.order_failed)
    }

    private fun orderCart() {
        orderViewModel.orderCart(addressId, this)
            .observe(this) {
                when (it) {
                    is ApiResponse.Success -> {
                        progressDialog.cancel()
                        dialog(R.layout.order_successful)
                    }

                    is ApiResponse.Loading -> progressDialog.show()

                    is ApiResponse.Error -> {
                        progressDialog.cancel()
                        dialog(R.layout.order_failed)
                    }
                }
            }
    }

    private fun orderProduct() {
        orderViewModel.orderProduct(addressId, product!!.id, quantity, this).observe(this) {
            when (it) {
                is ApiResponse.Success -> {
                    progressDialog.cancel()
                    dialog(R.layout.order_successful)
                }

                is ApiResponse.Loading -> progressDialog.show()

                is ApiResponse.Error -> {
                    progressDialog.cancel()
                    dialog(R.layout.order_failed)
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun dialog(layout: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(layout)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.show()

        Handler().postDelayed(
            {
                startActivity(Intent(this, DashboardActivity::class.java))
            }, 4000
        )
    }
}
