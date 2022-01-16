package com.example.acedrops.view.dash.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

class AllProductsFragment : Fragment() {
    private var _binding: FragmentAllProductsBinding? = null
    private lateinit var productsViewModel: ProductsViewModel
    private val binding get() = _binding!!
    private var productAdapter = ProductAdapter()
    private var oneCategoryResult:OneCategoryResult? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        val view = binding.root

        oneCategoryResult = arguments?.getSerializable("OneCategory") as OneCategoryResult?

        if(oneCategoryResult==null){
            val categoryName = arguments?.getString("CategoryName")
            if (categoryName != null) productsViewModel.getProductList(categoryName)
        }
        else initRecyclerView(oneCategoryResult!!)

        return view
    }

    private fun initRecyclerView(oneCategoryResult: OneCategoryResult) {
        binding.productsRecyclerView.adapter = productAdapter
        productAdapter.updateProductList(oneCategoryResult.result?.products!!,oneCategoryResult.favProd)
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productsViewModel = ViewModelProvider(
            this,
            ProductsViewModelFactory(ProductRepository(ServiceBuilder.buildService(ACC_TOKEN)))
        )[ProductsViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}