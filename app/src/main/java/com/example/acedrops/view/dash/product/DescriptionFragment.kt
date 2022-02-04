package com.example.acedrops.view.dash.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.ProductAdapter
import com.example.acedrops.databinding.FragmentDescriptionBinding
import com.example.acedrops.model.allproducts.OneCategoryResult
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.ProductViewModel

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
