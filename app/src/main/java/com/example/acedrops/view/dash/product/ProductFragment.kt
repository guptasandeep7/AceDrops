package com.example.acedrops.view.dash.product

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.acedrops.R
import com.example.acedrops.adapter.PageAdapter
import com.example.acedrops.databinding.FragmentProductBinding
import com.example.acedrops.model.home.ImgUrl
import com.example.acedrops.model.home.Product
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.ProductViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private lateinit var productViewModel: ProductViewModel
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pageAdapter = PageAdapter(this)

        binding.viewpagerProduct.adapter = pageAdapter

        TabLayoutMediator(binding.tabProduct, binding.viewpagerProduct) { tab, position ->
            when (position) {
                0 -> tab.text = "Description"
                1 -> tab.text = "Reviews"
            }
        }.attach()
        super.onViewCreated(view, savedInstanceState)

        getProductDetails()

        binding.product = productViewModel.product.value

        binding.productBasePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        productViewModel.product.value?.let { showImages(it.imgUrls) }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addToCartBtn.setOnClickListener { addToCart() }

    }

    private fun getProductDetails() {
        productViewModel.getProductDetails(productViewModel.product.value!!.id, requireContext())
            .observe(viewLifecycleOwner, {
                when (it) {
                    is ApiResponse.Success -> {
                        binding.rating.text = (it.data?.rating ?: 0).toString()
                    }
                    is ApiResponse.Error -> Toast.makeText(
                        requireContext(),
                        it.errorMessage,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
    }

    private fun addToCart() {
        productViewModel.addToCart(productViewModel.product.value!!.id, requireContext())
            .observe(viewLifecycleOwner, {
                when (it) {
                    is ApiResponse.Success -> if (it.data == true) {
                        binding.progressBar.visibility = View.GONE
                        view?.let { it1 ->
                            Snackbar.make(
                                it1,
                                productViewModel.product.value?.title.toString(),
                                Snackbar.LENGTH_SHORT
                            ).setBackgroundTint(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.blue
                                )
                            )
                                .setAction("View Cart") {
                                    findNavController().navigate(R.id.action_productFragment_to_cartFragment)
                                }
                                .setActionTextColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.white
                                    )
                                )
                                .setAnchorView(R.id.bottomNavigationView)
                                .show()
                        }
                    }
                    is ApiResponse.Error -> Toast.makeText(
                        requireContext(),
                        it.errorMessage,
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
                }
            })
    }

    private fun showImages(imgUrls: List<ImgUrl>) {
        val imageList = mutableListOf<SlideModel>()
        imgUrls.forEach {
            imageList.add(SlideModel(it.imageUrl, ScaleTypes.CENTER_CROP))
        }
        binding.productImages.setImageList(imageList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val view = binding.root

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
        activity?.findViewById<Toolbar>(R.id.toolbar)?.visibility =
            View.GONE

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
        activity?.findViewById<Toolbar>(R.id.toolbar)?.visibility =
            View.GONE

        productViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[ProductViewModel::class.java]

        try {
            productViewModel.product.value = arguments?.getSerializable("Product") as Product?
        } catch (e: Exception) {

        }

    }

    override fun onResume() {
        super.onResume()

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
        activity?.findViewById<Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.VISIBLE
        activity?.findViewById<Toolbar>(R.id.toolbar)?.visibility =
            View.VISIBLE
    }

}