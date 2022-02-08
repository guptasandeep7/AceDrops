package com.example.acedrops.view.dash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.acedrops.R
import com.example.acedrops.adapter.CartAdapter
import com.example.acedrops.adapter.SwipeGesture
import com.example.acedrops.databinding.FragmentCartBinding
import com.example.acedrops.model.cart.Cart
import com.example.acedrops.model.cart.CartData
import com.example.acedrops.model.home.Product
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.CartViewModel
import java.util.*

class CartFragment : Fragment() {

    lateinit var binding: FragmentCartBinding
    lateinit var cartViewModel: CartViewModel
    private var cartAdapter = CartAdapter()
    lateinit var swipeGesture: SwipeGesture

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[CartViewModel::class.java]

        binding.progressBar.visibility = View.GONE
        binding.cardView2.visibility = View.GONE

        binding.proceedBtn.setOnClickListener{
            val bundle = bundleOf("Cart" to "Cart")
            findNavController().navigate(R.id.action_cartFragment_to_addressFragment,bundle)
        }

        swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        cartViewModel.deleteProduct(
                            cartAdapter.cartList[viewHolder.adapterPosition].id.toString(),
                            requireContext()
                        )
                        observerDelete()
                    }
                }
            }
        }

        cartViewModel.totalAmount.observe(viewLifecycleOwner, {
            binding.viewmodel = cartViewModel
            if (it == 0L) {
                binding.emptyCart.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.cardView2.visibility = View.GONE
            }
        })

        cartViewModel.getCartData(requireContext())?.observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    if (it.data == null) {
                        binding.emptyCart.visibility = View.VISIBLE
                        binding.cardView2.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                    } else updateUI(it.data)
                }
                is ApiResponse.Loading -> {
                    binding.emptyCart.visibility = View.GONE
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
        })

        cartAdapter.setOnItemClickListener(object : CartAdapter.onItemClickListener {
            override fun decreaseQuantity(position: Int) {
                observerRemove(cartAdapter.cartList[position].id.toString())
            }

            override fun increaseQuantity(position: Int) {
                observerAdd(cartAdapter.cartList[position].id.toString())
            }

            override fun addWishlist(position: Int) {
                observerWishlistResult(cartAdapter.cartList[position].id.toString())
            }

            override fun onItemClick(position: Int) {
                val product:Product
                cartAdapter.cartList[position].also {
                    product = Product(
                        it.basePrice,
                        it.createdAt,
                        it.shortDescription,
                        it.discountedPrice,
                        it.id,
                        it.imgUrls,
                        it.offers,
                        null,
                        it.shopId,
                        it.stock,
                        it.title,
                        it.updatedAt,
                        it.wishlistStatus
                    )
                }
                val bundle = bundleOf("Product" to product)
                findNavController().navigate(R.id.action_cartFragment_to_productFragment, bundle)
            }
        })
    }

    private fun observerWishlistResult(id: String) {
        cartViewModel.addWishlist(id, requireContext()).observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    for (item in cartAdapter.cartList) {
                        if (item.id == it.data?.prodId) {
                            item.wishlistStatus = it.data.status.toInt()
                            cartAdapter.notifyDataSetChanged()
                            break
                        }
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

    private fun observerAdd(id: String) =
        cartViewModel.increaseQuantity(id, requireContext()).observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    for (item in cartAdapter.cartList) {
                        if (item.id == it.data?.prodId) {
                            item.cart_item.quantity = it.data.quantity!!
                            cartAdapter.notifyDataSetChanged()
                            break
                        }
                    }
                    cartViewModel.totalAmount.value =
                        calTotalAmount(cartAdapter.cartList as ArrayList<Cart>)
                    binding.progressBar.visibility = View.GONE
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

    private fun observerRemove(id: String) {
        cartViewModel.decreaseQuantity(id, requireContext()).observe(this, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    for (item in cartAdapter.cartList) {
                        if (item.id == it.data?.prodId) {
                            if (it.data.quantity!! > 0) {
                                item.cart_item.quantity = it.data.quantity
                            } else {
                                cartAdapter.cartList.remove(item)
                            }
                            cartAdapter.notifyDataSetChanged()
                            break
                        }
                    }
                    cartViewModel.totalAmount.value =
                        calTotalAmount(cartAdapter.cartList as ArrayList<Cart>)
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

    private fun observerDelete() {
        cartViewModel.deleteFromCartResult.observe(this, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    for (item in cartAdapter.cartList) {
                        if (item.id == it.data?.prodId) {
                            cartAdapter.cartList.remove(item)
                            cartAdapter.notifyDataSetChanged()
                            break
                        }
                    }
                    cartViewModel.totalAmount.value =
                        calTotalAmount(cartAdapter.cartList as ArrayList<Cart>)
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

    private fun updateUI(cartList: CartData) {
        with(binding) {
            progressBar.visibility = View.GONE
            emptyCart.visibility = View.GONE
            cartRecyclerview.adapter = cartAdapter
            binding.cardView2.visibility = View.VISIBLE
            cartViewModel.totalAmount.value = calTotalAmount(cartList.prodInCart)
        }
        cartAdapter.updateProductList(cartList.prodInCart, cartList.favProd)
        binding.cartRecyclerview.visibility = View.VISIBLE
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.cartRecyclerview)
    }

    private fun calTotalAmount(cartList: List<Cart>): Long {
        var total: Long = 0
        for (item in cartList) total += item.discountedPrice * item.cart_item.quantity
        return total
    }

}