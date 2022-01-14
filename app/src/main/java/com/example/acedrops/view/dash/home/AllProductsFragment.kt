package com.example.acedrops.view.dash.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.acedrops.adapter.ProductAdapter
import com.example.acedrops.databinding.FragmentAllProductsBinding
import com.example.acedrops.model.home.Product

class AllProductsFragment : Fragment() {
    private var _binding: FragmentAllProductsBinding? = null
    private val binding get() = _binding!!
    private var productAdapter = ProductAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        val view = binding.root

        val productList = arguments?.getSerializable("ProductList") as List<Product>
        binding.productsRecyclerView.adapter = productAdapter
        productAdapter.updateProductList(product = productList)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}