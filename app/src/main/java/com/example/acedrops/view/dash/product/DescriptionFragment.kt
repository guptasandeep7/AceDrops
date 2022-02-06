package com.example.acedrops.view.dash.product

import android.os.Bundle
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
import com.example.acedrops.R
import com.example.acedrops.adapter.ProductAdapter
import com.example.acedrops.adapter.status
import com.example.acedrops.databinding.FragmentDescriptionBinding
import com.example.acedrops.model.allproducts.OneCategoryResult
import com.example.acedrops.model.home.Product
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.ProductViewModel
import com.google.android.material.snackbar.Snackbar

class DescriptionFragment : Fragment() {
    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private var productAdapter = ProductAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[ProductViewModel::class.java]

        binding.cardView3.setOnClickListener {

            val bundle = bundleOf("ShopId" to productViewModel.product.value?.shopId)
            findNavController().navigate(R.id.action_productFragment_to_shopFragment, bundle)
        }

        productViewModel.productDetails.observe(viewLifecycleOwner, {
            if (it is ApiResponse.Success) {
                binding.viewmodel = productViewModel
                getSimilarList()
            }
        })

        productAdapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener {
            override fun onItemClick(product: Product) {
                val bundle = bundleOf("Product" to product)
                findNavController().navigate(R.id.action_productFragment_self, bundle)
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
        productViewModel.addToCart(productId = product.id, requireContext())
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
        productViewModel.addWishlist(productId = product.id.toString(), requireContext())
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
                        .navigate(R.id.action_productFragment_to_cartFragment)
                    it.visibility = View.GONE
                }.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                .setAnchorView(R.id.bottomNavigationView)
                .show()
        }
    }

    private fun getSimilarList() {
        productViewModel.productDetails.value?.data?.category?.category?.let {
            productViewModel.getProductList(
                it,
                requireContext()
            ).observe(viewLifecycleOwner, {

                if (it is ApiResponse.Success) it.data?.let { it1 -> initRecyclerView(it1) }
            })
        }
    }

    private fun initRecyclerView(it: OneCategoryResult) {
        productAdapter.updateProductList(
            it.result?.products!!,
            it.favProd
        )
        binding.similarProdRv.adapter = productAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

}
