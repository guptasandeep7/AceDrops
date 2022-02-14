package com.example.acedrops.view.dash.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.ShopProductsAdapter
import com.example.acedrops.databinding.FragmentShopBinding
import com.example.acedrops.model.home.ShopResult
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.home.ShopRepository
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewModelFactory.ShopViewModelFactory
import com.example.acedrops.viewmodel.ShopViewModel

class ShopFragment : Fragment(), View.OnClickListener {
    private var mLastClickTime:Long = 0
    private var _binding: FragmentShopBinding? = null
    private lateinit var shopViewModel: ShopViewModel
    private val binding get() = _binding!!
    var shopId: Int = 0
    lateinit var shopDetails: ShopResult
    private var shopProductAdapter = ShopProductsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.backBtn.setOnClickListener(this)
        binding.shopCallBtn.setOnClickListener(this)
        binding.shopEmailBtn.setOnClickListener(this)

        binding.productsRecyclerView.adapter = shopProductAdapter

        shopProductAdapter.setOnItemClickListener(object : ShopProductsAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    val bundle = bundleOf("Product" to shopProductAdapter.productsList[position])
                    findNavController().navigate(
                        R.id.action_shopFragment_to_productFragment,
                        bundle
                    )
                }
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shopViewModel.shopDetails.observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.shopCallBtn.isEnabled = true
                    binding.shopEmailBtn.isEnabled = true
                    shopDetails = it.data!!
                    updateUi(shopDetails)
                }
                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
                is ApiResponse.Error -> {
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    shopViewModel.getShopDetails(shopId)
                }
            }
        })
    }

    private fun updateUi(shop: ShopResult?) {
        binding.shop = shop
        shop?.products?.let { shopProductAdapter.updateProductList(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shopId = arguments?.getInt("ShopId") as Int
        shopViewModel = ViewModelProvider(
            this,
            ShopViewModelFactory(ShopRepository(ServiceBuilder.buildService()), shopId)
        )[ShopViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_btn -> findNavController().popBackStack()
            R.id.shop_call_btn -> callShop()
            R.id.shop_email_btn -> emailShop()
        }
    }

    private fun emailShop() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:${shopDetails.email}")
        intent.putExtra(Intent.EXTRA_EMAIL, shopDetails.email)
        startActivity(intent)
    }

    private fun callShop() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${shopDetails.phno}")
        startActivity(intent)
    }
}