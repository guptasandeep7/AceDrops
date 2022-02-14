package com.example.acedrops.view.dash.product

import android.graphics.Paint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.acedrops.R
import com.example.acedrops.adapter.PageAdapter
import com.example.acedrops.adapter.status
import com.example.acedrops.databinding.FragmentProductBinding
import com.example.acedrops.model.home.ImgUrl
import com.example.acedrops.model.home.Product
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.OrderViewModel
import com.example.acedrops.viewmodel.ProductViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private lateinit var productViewModel: ProductViewModel
    private lateinit var orderViewModel: OrderViewModel
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

        if (productViewModel.product.value!!.stock == 0) {
            binding.outOfStock.visibility = View.VISIBLE
            binding.buyNowBtn.visibility = View.GONE
            binding.addToCartBtn.visibility = View.GONE
        }

        binding.product = productViewModel.product.value

        binding.productBasePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        productViewModel.product.value?.let { showImages(it.imgUrls) }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.searchBtn.setOnClickListener {
            findNavController().navigate(R.id.action_productFragment_to_searchFragment)
        }

        binding.addToCartBtn.setOnClickListener {
            addToCart(productViewModel.product.value!!.id)
        }

        binding.addToWishlistBtn.setOnClickListener {
            addToWishlist(productViewModel.product.value!!.id)
        }

        binding.buyNowBtn.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Buy Product")
        val input = EditText(requireContext())
        input.hint = "Enter quantity"
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            var quantity = 0L

            try {
                quantity = input.text.toString().toLong()
                when {
                    quantity < 1 -> Toast.makeText(
                        requireContext(),
                        "Quantity should be more than 0",
                        Toast.LENGTH_SHORT
                    ).show()

                    quantity > productViewModel.product.value!!.stock -> Toast.makeText(
                        requireContext(),
                        "Only ${productViewModel.product.value!!.stock} quantity is available",
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> {
                        orderViewModel.quantity = input.text.toString()
                        orderViewModel.product = productViewModel.product.value
                        orderViewModel.totalAmount =
                            productViewModel.product.value!!.discountedPrice * quantity
                        val bundle = bundleOf("LastFragment" to "Product")
                        findNavController().navigate(
                            R.id.action_productFragment_to_addressFragment,
                            bundle
                        )
                    }
                }
            } catch (e: Exception) {
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()

    }

    private fun addToWishlist(id: Int) {
        productViewModel.addWishlist(productId = id.toString(), requireContext())
            .observe(viewLifecycleOwner, {
                when (it) {
                    is ApiResponse.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.addToWishlistBtn.status(it.data?.status?.toInt()!!)

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

    private fun getProductDetails() {
        productViewModel.getProductDetails(productViewModel.product.value!!.id, requireContext())
            .observe(viewLifecycleOwner, {
                when (it) {
                    is ApiResponse.Success -> {
                        binding.rating.text = (it.data?.rating ?: 0).toString()
                        if (productViewModel.productDetails.value?.data?.isFav == true)
                            binding.addToWishlistBtn.status(1)
                        else
                            binding.addToWishlistBtn.status(0)
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

    private fun addToCart(id: Int) {
        productViewModel.addToCart(id, requireContext())
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
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[ProductViewModel::class.java]

        orderViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[OrderViewModel::class.java]

        try {
            productViewModel.product.value = arguments?.getSerializable("Product") as Product?
        } catch (e: Exception) {

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}