package com.example.acedrops.view.dash.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.acedrops.R
import com.example.acedrops.adapter.CategoryHomeAdapter
import com.example.acedrops.adapter.ShopAdapter
import com.example.acedrops.databinding.FragmentHomeBinding
import com.example.acedrops.model.allproducts.OneCategoryResult
import com.example.acedrops.model.home.NewArrival
import com.example.acedrops.model.home.Shop
import com.example.acedrops.model.home.productId
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import com.example.acedrops.repository.dashboard.home.HomeRepository
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.generateToken
import com.example.acedrops.view.auth.AuthActivity.Companion.ACC_TOKEN
import com.example.acedrops.viewModelFactory.HomeViewModelFactory
import com.example.acedrops.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    private var newArrivals = mutableListOf<NewArrival>()
    private var shops = mutableListOf<Shop>()
    private var favList = mutableListOf<productId>()
    private val shopAdapter = ShopAdapter()
    private val categoryAdapter = CategoryHomeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view = binding.root

        binding.progressBar.visibility = View.VISIBLE
        binding.shopRecyclerView.adapter = shopAdapter
        binding.categoryRecyclerView.adapter = categoryAdapter

        binding.allShopBtn.setOnClickListener {
            val bundle = bundleOf("ShopList" to shops)
            findNavController().navigate(R.id.action_homeFragment_to_allShopsFragment, bundle)
        }

        shopAdapter.setOnItemClickListener(object : ShopAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(
                    requireContext(),
                    "shop id ${shopAdapter.shopsList[position].id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        categoryAdapter.setOnItemClickListener(object : CategoryHomeAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val oneCategory = OneCategoryResult(favList,categoryAdapter.categoryList[position])
                val bundle = bundleOf("OneCategory" to oneCategory )
                view.findNavController()
                    .navigate(R.id.action_homeFragment_to_allProductsFragment, bundle)
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataStore = Datastore(requireContext())

        generateToken.observe(viewLifecycleOwner, {
            if (it == null) {
                lifecycleScope.launch {
                    dataStore.changeLoginState(false)
                    view.findNavController().navigate(R.id.action_homeFragment_to_authActivity)
                    activity?.finish()
                }
            } else {
                homeViewModel.getHomeData()
            }
        })

        homeViewModel.homeData.observe(viewLifecycleOwner, { it ->
            when (it) {
                is ApiResponse.Success -> {
                    if (it.data != null) {
                        it.data.also {
                            binding.progressBar.visibility = View.GONE
                            newArrivals = it.newArrival as MutableList<NewArrival>
                            shops = it.Shop as MutableList<Shop>
                            favList = it.favProd as MutableList<productId>
                            showNewArrivals(newArrivals)
                            shopAdapter.setShopList(shops)
                            categoryAdapter.updateCategoryList(it.category,it.favProd)
                        }
                    }
                }
                is ApiResponse.Error -> Toast.makeText(
                    requireContext(),
                    it.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                is ApiResponse.TokenExpire -> {
                    Toast.makeText(requireContext(), "generateToken expire", Toast.LENGTH_SHORT)
                        .show()
                    Log.w("access generateToken ", "ACC_TOKEN is $ACC_TOKEN")
                    lifecycleScope.launch {
                        generateToken(requireContext())
                    }
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.w("HOME FRAGMENT", "onCreate: ACCESS TOKEN $ACC_TOKEN")
        val homeRepository = HomeRepository(ServiceBuilder.buildService(token = ACC_TOKEN))
        val homeViewModelFactory = HomeViewModelFactory(homeRepository)
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]

    }

    private fun showNewArrivals(newArrival: List<NewArrival>) {
        val imageList = ArrayList<SlideModel>()
        for (item in newArrival) {
            imageList.add(SlideModel(item.imgUrls[0].imageUrl, ScaleTypes.CENTER_CROP))
        }
        binding.imageSlider.setImageList(imageList)
    }

}