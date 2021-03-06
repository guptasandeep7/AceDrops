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
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.acedrops.acedrops.R
import com.acedrops.acedrops.adapter.CategoryHomeAdapter
import com.acedrops.acedrops.adapter.ShopAdapter
import com.acedrops.acedrops.adapter.status
import com.acedrops.acedrops.databinding.FragmentHomeBinding
import com.acedrops.acedrops.model.allproducts.OneCategoryResult
import com.acedrops.acedrops.model.home.NewArrival
import com.acedrops.acedrops.model.home.Product
import com.acedrops.acedrops.utill.ApiResponse
import com.acedrops.acedrops.viewmodel.CartViewModel
import com.acedrops.acedrops.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private var mLastClickTime: Long = 0
    private lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    private val shopAdapter = ShopAdapter()
    private val categoryAdapter = CategoryHomeAdapter()
    private val cartViewModel = CartViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[HomeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getHomeData()

        binding.searchBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.allShopBtn.setOnClickListener {
            val bundle = bundleOf("ShopList" to homeViewModel.homeData.value?.data?.Shop)
            findNavController().navigate(R.id.action_homeFragment_to_allShopsFragment, bundle)
        }


        shopAdapter.setOnItemClickListener(object : ShopAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                    val bundle = bundleOf("ShopId" to shopAdapter.shopsList[position].id)
                    findNavController().navigate(
                        R.id.action_homeFragment_to_shopFragment,
                        bundle
                    )
                }
            }
        })

        categoryAdapter.setOnItemClickListener(
            object : CategoryHomeAdapter.onItemClickListener {
                override fun showAll(position: Int) {

                    val oneCategory = homeViewModel.homeData.value?.data?.favProd?.let {
                        OneCategoryResult(
                            it, categoryAdapter.categoryList[position]
                        )
                    }
                    val bundle = bundleOf("OneCategory" to oneCategory)
                    findNavController()
                        .navigate(R.id.action_homeFragment_to_allProductsFragment, bundle)
                }

                override fun addToCartClick(product: Product, view: View) {
                    addToCart(product, view)
                }

                override fun addToWishlistClick(product: Product, view: View) {
                    addToWishlist(product, view)
                }

                override fun onItemClick(
                    product: Product
                ) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return
                    } else {
                        mLastClickTime = SystemClock.elapsedRealtime()
                        val bundle = bundleOf("Product" to product)
                        findNavController().navigate(
                            R.id.action_homeFragment_to_productFragment,
                            bundle
                        )
                    }
                }
                })

            }

                    private fun addToWishlist(product: Product, view: View) {
                cartViewModel.addWishlist(productId = product.id.toString(), requireContext())
                    .observe(viewLifecycleOwner) {
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
                    }
                    }

                    private fun addToCart(product: Product, view: View) {
                cartViewModel.increaseQuantity(productId = product.id.toString(), requireContext())
                    .observe(viewLifecycleOwner) {
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
                    }
                    }

                    private fun getHomeData() {
                homeViewModel.getHomeData(requireContext()).observe(viewLifecycleOwner) {
                    when (it) {
                        is ApiResponse.Success -> {
                            binding.allShopBtn.visibility = View.VISIBLE
                            binding.textView.visibility = View.VISIBLE
                            it.data?.newArrival?.let { it1 -> showNewArrivals(it1) }
                            it.data?.Shop?.let { it1 -> shopAdapter.setShopList(it1) }
                            it.data?.category?.let { it1 ->
                                categoryAdapter.updateCategoryList(
                                    it1,
                                    it.data.favProd
                                )
                            }
                            binding.shopRecyclerView.adapter = shopAdapter
                            binding.categoryRecyclerView.adapter = categoryAdapter
                            binding.progressBar.visibility = View.GONE
                        }

                        is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is ApiResponse.Error -> {
                            Toast.makeText(
                                requireContext(),
                                it.errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                            homeViewModel.getHomeData(requireContext())
                        }

                    }
                }
                    }


                    private fun showNewArrivals(newArrival: List<NewArrival>) {
                val imageList = ArrayList<SlideModel>()
                newArrival.forEach{
                    try {
                        imageList.add(SlideModel(it.imgUrls[0].imageUrl, ScaleTypes.CENTER_CROP))
                    }catch (e:Exception){
                        imageList.add(SlideModel(getString(R.string.default_image), ScaleTypes.CENTER_CROP))
                    }
                }
                binding.imageSlider.setImageList(imageList)
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
                                .navigate(R.id.action_homeFragment_to_cartFragment)
                            it.visibility = View.GONE
                        }
                        .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        .setAnchorView(R.id.bottomNavigationView)
                        .show()
                }
            }


    }