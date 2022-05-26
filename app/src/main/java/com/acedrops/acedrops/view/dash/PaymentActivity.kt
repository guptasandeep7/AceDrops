package com.acedrops.acedrops.view.dash

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.acedrops.acedrops.R
import com.acedrops.acedrops.repository.Datastore
import com.acedrops.acedrops.utill.ApiResponse
import com.acedrops.acedrops.utill.HashGenerationUtils
import com.acedrops.acedrops.utill.ProgressDialog
import com.acedrops.acedrops.view.dash.profile.AddressFragment.Companion.addressId
import com.acedrops.acedrops.view.dash.profile.AddressFragment.Companion.orderFrom
import com.acedrops.acedrops.view.dash.profile.AddressFragment.Companion.product
import com.acedrops.acedrops.view.dash.profile.AddressFragment.Companion.quantity
import com.acedrops.acedrops.view.dash.profile.AddressFragment.Companion.totalAmount
import com.acedrops.acedrops.viewmodel.OrderViewModel
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.utils.PayUCheckoutProConstants
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_NAME
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_STRING
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener
import com.razorpay.Checkout
import kotlinx.coroutines.runBlocking

class PaymentActivity : AppCompatActivity() {

    private val orderViewModel: OrderViewModel by viewModels()
    private lateinit var progressDialog: Dialog
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var name: String
    private val merchantName = "Acedrops"
    private val sUrl = "https://www.acedrops.in"
    private val fUrl = "https://www.acedrops.in"
    private lateinit var datastore: Datastore

    //Test Key and Salt
    private val testKey = "gtKFFx"
    private val testSalt = "wia56q6O"

    //Prod Key and Salt
    private val prodKey = "3TnMpV"
    private val prodSalt = "g0nGFe03"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Checkout.preload(applicationContext)
        datastore = Datastore(this)

        progressDialog = ProgressDialog.progressDialog(this)

        runBlocking {
            email = datastore.getUserDetails(Datastore.EMAIL_KEY).toString()
            phone = datastore.getUserDetails(Datastore.PHN_NUMBER).toString()
            name = datastore.getUserDetails(Datastore.NAME_KEY).toString()

        }
        makePayment()
    }


    private fun makePayment() {
        try {
            val payUPaymentParams = PayUPaymentParams.Builder()
                .setAmount(totalAmount.toString())
                .setIsProduction(true)
                .setKey(testKey)
                .setProductInfo("Test")
                .setPhone("9876543216")
                .setTransactionId(System.currentTimeMillis().toString())
                .setFirstName("John")
                .setEmail("john@gmail.com")
                .setSurl(sUrl)
                .setFurl(fUrl)
                .build()


            PayUCheckoutPro.open(
                this, payUPaymentParams,
                object : PayUCheckoutProListener {

                    override fun onPaymentSuccess(response: Any) {
                        response as HashMap<*, *>
                        val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                        val merchantResponse =
                            response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
                        Toast.makeText(
                            this@PaymentActivity,
                            "Payment is successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        when (orderFrom) {
                            "Cart" -> orderCart()
                            "Product" -> orderProduct()
                        }
                    }

                    override fun onPaymentFailure(response: Any) {
                        response as HashMap<*, *>
                        val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                        val merchantResponse =
                            response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
                        Toast.makeText(this@PaymentActivity, "onPaymentFailure: $payUResponse", Toast.LENGTH_SHORT).show()
                        dialog(R.layout.order_failed)
                    }

                    override fun onPaymentCancel(isTxnInitiated: Boolean) {
                        Toast.makeText(this@PaymentActivity, "onPaymentCancel: $isTxnInitiated", Toast.LENGTH_SHORT).show()
                        dialog(R.layout.order_failed)
                    }

                    override fun onError(errorResponse: ErrorResponse) {
                        val errorMessage: String =
                            if (errorResponse.errorMessage != null && errorResponse.errorMessage!!.isNotEmpty())
                                errorResponse.errorMessage!!
                            else
                                "Payment failed !!!"

                        Toast.makeText(this@PaymentActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        Log.w("PAYMENT ACTIVITY ", "onError: $errorMessage")
                        dialog(R.layout.order_failed)
                    }

                    override fun setWebViewProperties(webView: WebView?, bank: Any?) {
                        //For setting webview properties, if any. Check Customized Integration section for more details on this
                    }

                    override fun generateHash(
                        valueMap: HashMap<String, String?>,
                        hashGenerationListener: PayUHashGenerationListener
                    ) {
                        if (valueMap.containsKey(CP_HASH_STRING)
                            && valueMap.containsKey(CP_HASH_STRING) != null
                            && valueMap.containsKey(CP_HASH_NAME)
                            && valueMap.containsKey(CP_HASH_NAME) != null
                        ) {

                            val hashData = valueMap[CP_HASH_STRING]
                            val hashName = valueMap[CP_HASH_NAME]

                            //Do not generate hash from local, it needs to be calculated from server side only. Here, hashString contains hash created from your server side.
                            val hash: String? = HashGenerationUtils.generateHashFromSDK(hashData!!)
                            if (!TextUtils.isEmpty(hash)) {
                                val dataMap: HashMap<String, String?> = HashMap()
                                dataMap[hashName!!] = hash!!
                                hashGenerationListener.onHashGenerated(dataMap)
                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    //Razorpay methods

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
