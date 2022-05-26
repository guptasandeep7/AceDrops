package com.acedrops.acedrops.view.dash.home

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.acedrops.acedrops.R
import com.acedrops.acedrops.adapter.ProductAdapter
import com.acedrops.acedrops.adapter.status
import com.acedrops.acedrops.databinding.FragmentAllProductsBinding
import com.acedrops.acedrops.model.allproducts.OneCategoryResult
import com.acedrops.acedrops.model.home.Product
import com.acedrops.acedrops.utill.ApiResponse
import com.acedrops.acedrops.viewmodel.CartViewModel
import com.acedrops.acedrops.viewmodel.ProductViewModel
import com.google.android.material.snackbar.Snackbar

class AllProductsFragment : Fragment() {
    private var mLastClickTime: Long = 0
    private var _binding: FragmentAllProductsBinding? = null
    private var productViewModel = ProductViewModel()
    private val binding get() = _binding!!
    private var productAdapter = ProductAdapter()
    private var cartViewModel = CartViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        if (productViewModel.oneCategoryData.value == null) {
            val categoryName = arguments?.getString("CategoryName")
            if (categoryName != null) {
                binding.toolbarTitle.text = categoryName
                getProductList(categoryName)
            }
        } else {
            binding.toolbarTitle.text = productViewModel.oneCategoryData.value!!.result?.category
            initRecyclerView(productViewModel.oneCategoryData.value!!)
        }

        productAdapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener {
            override fun onItemClick(product: Product) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return
                } else {
                    val bundle = bundleOf("Product" to product)
                    findNavController().navigate(
                        R.id.action_allProductsFragment_to_productFragment,
                        bundle
                    )
                }
            }

            override fun onAddToCartClick(product: Product, view: View) {
                addToCart(product, view)
            }

            override fun onAddToWishlistClick(product: Product, view: View, position: Int) {
                addToWishlist(product, view)
            }
        })

    }

    private fun addToCart(product: Product, view: View) {
        cartViewModel.increaseQuantity(productId = product.id.toString(), requireContext())
            .observe(viewLifecycleOwner, {
                when (it) {
                    is ApiResponse.Success -> {
                        view.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        snackbar(
                            "\u20B9${product.discountedPrice} plus taxes\n1 ITEM"
                        )
                    }

                    is ApiResponse.Error -> {
                        binding.progressBar.visibility = View.GONE
                        view.isEnabled = true
                        snackbar(
                            it.errorMessage ?: "Failed to add to cart : Try Again"
                        )
                    }

                    is ApiResponse.Loading -> {
                        view.isEnabled = false
//                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            })
    }

    private fun addToWishlist(product: Product, view: View) {
        cartViewModel.addWishlist(productId = product.id.toString(), requireContext())
            .observe(viewLifecycleOwner, {
                when (it) {
                    is ApiResponse.Success -> {
                        binding.progressBar.visibility = View.GONE
                        view as ImageView
                        view.status(it.data?.status?.toInt()!!)
                        if (it.data.status.toInt() == 1)
                            Toast.makeText(
                                requireContext(),
                                "Added to wishlist",
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            Toast.makeText(
                                requireContext(),
                                "Removed from wishlist",
                                Toast.LENGTH_SHORT
                            ).show()
                    }

                    is ApiResponse.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ApiResponse.Error -> Toast.makeText(
                        requireContext(),
                        it.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()

                }
            })
    }

    private fun getProductList(categoryName: String) {
        productViewModel.getProductList(categoryName, requireContext())
            .observe(viewLifecycleOwner, {
                when (it) {
                    is ApiResponse.Success -> {
                        binding.progressBar.visibility = View.GONE
                        it.data?.let { it1 -> initRecyclerView(it1) }
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

    private fun initRecyclerView(oneCategoryResult: OneCategoryResult) {
        binding.productsRecyclerView.adapter = productAdapter
        productAdapter.updateProductList(
            oneCategoryResult.result?.products!!,
            oneCategoryResult.favProd
        )
    }

    private fun snackbar(
        text: String
    ) {
        view?.let {
            Snackbar.make(
                it,
                text,
                Snackbar.LENGTH_SHORT
            ).setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
                .setAction("View Cart") {
                    findNavController()
                        .navigate(R.id.action_allProductsFragment_to_cartFragment)
                }.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                .setAnchorView(R.id.bottomNavigationView)
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[ProductViewModel::class.java]

        try {
            productViewModel.oneCategoryData.value =
                arguments?.getSerializable("OneCategory") as OneCategoryResult?
        } catch (e: Exception) {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}