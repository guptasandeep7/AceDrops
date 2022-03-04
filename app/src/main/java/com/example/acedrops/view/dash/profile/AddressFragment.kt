package com.example.acedrops.view.dash.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.AddressAdapter
import com.example.acedrops.databinding.FragmentAddressBinding
import com.example.acedrops.model.home.Product
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.AddressViewModel
import com.example.acedrops.viewmodel.OrderViewModel
import com.google.android.material.button.MaterialButton

class AddressFragment : Fragment() {
    private var mLastClickTime: Long = 0
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var addressViewModel: AddressViewModel
    private val addressAdapter = AddressAdapter()
    lateinit var orderViewModel: OrderViewModel
    private var lastFragment: String? = null

    companion object {
        var totalAmount = 0L
        lateinit var orderFrom: String
        var product: Product? = null
        lateinit var quantity: String
        var addressId: Int = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (orderViewModel.lastFragment != null) {
            binding.toolbarTitle.text = getString(R.string.choose_delivery_address)
        }

        getAddress()

        binding.backBtn.setOnClickListener { findNavController().popBackStack() }

        binding.addAddressBtn.setOnClickListener { findNavController().navigate(R.id.action_addressFragment_to_addAddressFragment) }

        addressAdapter.setOnItemClickListener(object : AddressAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    if (orderViewModel.lastFragment != null) {
                        orderViewModel.addressId = addressAdapter.addressList[position].id!!
                        orderSummary()
                    }
                }
            }
        })
    }

    private fun orderSummary() {
        val dialog = Dialog(requireContext())
        dialog.setTitle("Order Summary")
        val layout = layoutInflater.inflate(R.layout.order_summary, null)
        dialog.setContentView(layout)
        val productName = layout.findViewById<TextView>(R.id.product_name)
        val productQuantity = layout.findViewById<TextView>(R.id.product_quantity)
        val productPrice = layout.findViewById<TextView>(R.id.product_price)

        if (orderViewModel.lastFragment == "Product") {
            productName.text = orderViewModel.product?.title
            productQuantity.append(orderViewModel.quantity)
            productPrice.append(orderViewModel.product!!.discountedPrice.toString())
        } else {
            productName.visibility = View.GONE
            productQuantity.visibility = View.GONE
            productPrice.visibility = View.GONE
        }
        layout.findViewById<TextView>(R.id.total_amount)
            .append(orderViewModel.totalAmount.toString())
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.show()

        layout.findViewById<MaterialButton>(R.id.proceed_to_pay_btn).setOnClickListener {
            totalAmount = orderViewModel.totalAmount
            orderFrom = orderViewModel.lastFragment.toString()
            product = orderViewModel.product
            quantity = orderViewModel.quantity
            addressId = orderViewModel.addressId
            findNavController().navigate(R.id.action_addressFragment_to_paymentActivity)
        }
    }


    private fun getAddress() {
        addressViewModel.getAddress(requireContext()).observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data.isNullOrEmpty()) {
                        binding.empty.visibility = View.VISIBLE
                        binding.productsRecyclerView.visibility = View.GONE
                    } else {
                        binding.productsRecyclerView.adapter = addressAdapter
                        addressAdapter.updateAddressList(it.data)
                    }
                }
                is ApiResponse.Loading -> {
                    binding.empty.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        it.errorMessage ?: "Something went wrong!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addressViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[AddressViewModel::class.java]

        orderViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[OrderViewModel::class.java]

        arguments?.let {
            lastFragment = it.getString("LastFragment")
            orderViewModel.lastFragment = lastFragment.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}