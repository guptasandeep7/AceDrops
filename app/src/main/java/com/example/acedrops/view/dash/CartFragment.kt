package com.example.acedrops.view.dash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.CartAdapter
import com.example.acedrops.databinding.FragmentCartBinding
import com.example.acedrops.model.cart.Cart
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.dashboard.CartRepository
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.generateToken
import com.example.acedrops.view.dash.DashboardActivity.Companion.ACC_TOKEN
import com.example.acedrops.viewModelFactory.CartViewModelFactory
import com.example.acedrops.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import java.util.*

class CartFragment : Fragment() {

    lateinit var binding: FragmentCartBinding
    lateinit var cartViewModel: CartViewModel
    private var cartAdapter = CartAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        val view = binding.root

        binding.progressBar.visibility = View.GONE

        cartViewModel.cartData.observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    if (it.data == null) {
                        binding.emptyCart.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    } else updateUI(it.data)
                }
                is ApiResponse.Loading -> {
                    binding.emptyCart.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ApiResponse.TokenExpire -> {
                    Toast.makeText(requireContext(), "token expire", Toast.LENGTH_SHORT).show()
                    Log.w("access token ", "ACC_TOKEN is $ACC_TOKEN")
                    lifecycleScope.launch {
                        generateToken(requireContext())
                        if(ACC_TOKEN != null)
                        onCreate(savedInstanceState)
                        else {
                            view.findNavController().navigate(R.id.action_cartFragment_to_authActivity)
                            activity?.finish()
                        }
                    }
                }
                is ApiResponse.Error -> Toast.makeText(
                    requireContext(),
                    it.errorMessage ?: "Something went wrong!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return view
    }

    private fun updateUI(cartList: ArrayList<Cart>) {
        with(binding) {
            progressBar.visibility = View.GONE
            emptyCart.visibility = View.GONE
            totalAmount.text =
                "${resources.getString(R.string.Rs)}${calTotalAmount(cartList)}"
        }
        binding.cartRecyclerview.adapter = cartAdapter
        cartAdapter.updateProductList(cartList)
        binding.cartRecyclerview.visibility = View.VISIBLE
        binding.cardView2.visibility = View.VISIBLE
    }

    private fun calTotalAmount(cartList: ArrayList<Cart>): Long {
        var total: Long = 0
        for (item in cartList) total += item.discountedPrice
        return total
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.w("api call", "onCreate: $ACC_TOKEN")
        cartViewModel = ViewModelProvider(
            this,
            CartViewModelFactory(CartRepository(ServiceBuilder.buildService(token = ACC_TOKEN)))
        )[CartViewModel::class.java]
    }
}