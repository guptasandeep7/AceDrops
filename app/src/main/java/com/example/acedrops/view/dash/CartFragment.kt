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
import com.example.acedrops.model.cart.CartData
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.dashboard.CartRepository
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.generateToken
import com.example.acedrops.view.auth.AuthActivity.Companion.ACC_TOKEN
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

        cartViewModel.totalAmount.observe(viewLifecycleOwner, {
            binding.viewmodel = cartViewModel
        })

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
                    Toast.makeText(requireContext(), "generateToken expire", Toast.LENGTH_SHORT)
                        .show()
                    Log.w("access generateToken ", "ACC_TOKEN is $ACC_TOKEN")
                    lifecycleScope.launch {
                        generateToken(requireContext())
                        cartViewModel.getCartData()
                    }
                }
                is ApiResponse.Error -> Toast.makeText(
                    requireContext(),
                    it.errorMessage ?: "Something went wrong!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


        generateToken.observe(viewLifecycleOwner, {
            if (it == null) {
                view.findNavController().navigate(R.id.action_cartFragment_to_authActivity)
                activity?.finish()
            } else {
                onCreate(savedInstanceState)
            }
        })

        cartAdapter.setOnItemClickListener(object : CartAdapter.onItemClickListener {
            override fun decreaseQuantity(position: Int) {
                cartViewModel.decreaseQuantity(cartAdapter.cartList[position].id.toString())
                observerRemove(position)
            }

            override fun increaseQuantity(position: Int) {
                cartViewModel.increaseQuantity(cartAdapter.cartList[position].id.toString())
                observerAdd(position)
            }

            override fun addWishlist(position: Int) {
                cartViewModel.addWishlist(cartAdapter.cartList[position].id.toString())
                cartViewModel.wishlistResult.observe(viewLifecycleOwner, {
                    when (it) {
                        is ApiResponse.Success -> if (it.data == true) {
                            Toast.makeText(requireContext(), "add to wishlist", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is ApiResponse.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                it.errorMessage ?: "Try Again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        })

        return view
    }

    private fun observerAdd(position: Int) = cartViewModel.atcResult.observe(viewLifecycleOwner, {
        when (it) {
            is ApiResponse.Success -> {
                if (it.data == true) {
                    cartAdapter.cartList[position].cart_item.quantity++
                    cartViewModel.totalAmount.value =
                        calTotalAmount(cartAdapter.cartList as ArrayList<Cart>)
                    binding.progressBar.visibility = View.GONE
                }
            }
            is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
            is ApiResponse.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    it.errorMessage ?: "Try Again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    })

    private fun observerRemove(position: Int) {
        cartViewModel.removeFromCartResult.observe(this, {
            when (it) {
                is ApiResponse.Success -> {
                    if (it.data == true) {
                        binding.progressBar.visibility = View.GONE
                        updateAdapter(position)
                        cartViewModel.totalAmount.value =
                            calTotalAmount(cartAdapter.cartList as ArrayList<Cart>)
                    }
                }
                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        it.errorMessage ?: "Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun updateAdapter(position: Int) {
        if (cartAdapter.cartList[position].cart_item.quantity > 1) {
            cartAdapter.cartList[position].cart_item.quantity--
        } else {
            cartAdapter.cartList.removeAt(position)
        }
    }

    private fun updateUI(cartList: CartData) {
        with(binding) {
            progressBar.visibility = View.GONE
            emptyCart.visibility = View.GONE
            cartRecyclerview.adapter = cartAdapter
            cartViewModel.totalAmount.value = calTotalAmount(cartList.prodInCart)
        }
        cartAdapter.updateProductList(cartList.prodInCart,cartList.favProd)
        binding.cartRecyclerview.visibility = View.VISIBLE
        binding.cardView2.visibility = View.VISIBLE
    }

    private fun calTotalAmount(cartList: List<Cart>): Long {
        var total: Long = 0
        for (item in cartList) total += item.discountedPrice * item.cart_item.quantity
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