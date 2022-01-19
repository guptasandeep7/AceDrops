package com.example.acedrops.view.dash.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.view.isGone
import androidx.databinding.adapters.ViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.acedrops.R
import com.example.acedrops.adapter.ProductAdapter
import com.example.acedrops.databinding.FragmentAllProductsBinding
import com.example.acedrops.model.allproducts.OneCategoryResult
import com.example.acedrops.model.home.Product
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.dashboard.home.ProductRepository
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.view.auth.AuthActivity.Companion.ACC_TOKEN
import com.example.acedrops.viewModelFactory.ProductsViewModelFactory
import com.example.acedrops.viewmodel.ProductsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class AllProductsFragment : Fragment() {
    private var _binding: FragmentAllProductsBinding? = null
    private lateinit var productsViewModel: ProductsViewModel
    private val binding get() = _binding!!
    private var productAdapter = ProductAdapter()
    private var oneCategoryResult: OneCategoryResult? = null
    private lateinit var bottomNavigation:BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        val view = binding.root

        bottomNavigation =
            activity?.findViewById(R.id.bottomNavigationView)!!
        bottomNavigation?.visibility = View.GONE

        binding.backBtn.setOnClickListener{
            findNavController().popBackStack()
        }
        oneCategoryResult = arguments?.getSerializable("OneCategory") as OneCategoryResult?

        if (oneCategoryResult == null) {
            val categoryName = arguments?.getString("CategoryName")
            if (categoryName != null) {
                binding.toolbarTitle.text = categoryName
                productsViewModel.getProductList(categoryName)
            }
            else productsViewModel.getWishlist()
        } else {
            binding.toolbarTitle.text = oneCategoryResult!!.result?.category
            initRecyclerView(oneCategoryResult!!)
        }

        return view
    }

    private fun initRecyclerView(oneCategoryResult: OneCategoryResult) {
        binding.productsRecyclerView.adapter = productAdapter
        productAdapter.updateProductList(
            oneCategoryResult.result?.products!!,
            oneCategoryResult.favProd
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsViewModel.productList.observe(viewLifecycleOwner, {
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

        productsViewModel.wishlist.observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    it.data?.let { it1 -> updateList(it1) }
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

    private fun updateList(list: List<Product>) {
        for (item in list) item.wishlistStatus = 1
        binding.productsRecyclerView.adapter = productAdapter
        productAdapter.updateProductList(list, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility = View.GONE

        productsViewModel = ViewModelProvider(
            this,
            ProductsViewModelFactory(ProductRepository(ServiceBuilder.buildService(ACC_TOKEN)))
        )[ProductsViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        bottomNavigation.visibility = View.VISIBLE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility = View.VISIBLE
    }
}