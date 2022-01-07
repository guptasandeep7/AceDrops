package com.example.acedrops.view.dash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.acedrops.R
import com.example.acedrops.adapter.CartAdapter
import com.example.acedrops.databinding.FragmentCartBinding
import com.example.acedrops.model.cart.Cart
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import com.example.acedrops.repository.dashboard.CartRepository
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.generateToken
import com.example.acedrops.viewModelFactory.CartViewModelFactory
import com.example.acedrops.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import java.util.*

class CartFragment : Fragment() {

    lateinit var binding: FragmentCartBinding
    lateinit var cartViewModel: CartViewModel
    private var cartAdapter = CartAdapter()
    lateinit var token: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        val view = binding.root
        binding.progressBar.visibility = View.VISIBLE
        val dataStore = Datastore(requireContext())
        lifecycleScope.launch {
            token = dataStore.getUserDetails(Datastore.ACCESS_TOKEN_KEY).toString()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel.getCartData()

        cartViewModel.cartData.observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> updateUI(it.data!!)
                is ApiResponse.Loading -> binding.progressBar.visibility = View.GONE
                is ApiResponse.TokenExpire -> lifecycleScope.launch {
                    generateToken(requireContext())
                    cartViewModel.getCartData()
                }
                is ApiResponse.Error -> Toast.makeText(
                    requireContext(),
                    it.errorMessage ?: "Something went wrong!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateUI(cartList: ArrayList<Cart>) {
        with(binding) {
            emptyCart.visibility = View.GONE
            cartRecyclerview.visibility = View.VISIBLE
            cardView2.visibility = View.VISIBLE
        }
        binding.cartRecyclerview.adapter = cartAdapter
        cartAdapter.updateProductList(cartList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cartViewModel = ViewModelProvider(
            this,
            CartViewModelFactory(CartRepository(ServiceBuilder.buildService(token = token)))
        )[CartViewModel::class.java]
    }
}