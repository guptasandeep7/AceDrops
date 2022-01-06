package com.example.acedrops.view.dash.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.acedrops.R
import com.example.acedrops.adapter.CategoryHomeAdapter
import com.example.acedrops.adapter.ShopAdapter
import com.example.acedrops.databinding.FragmentHomeBinding
import com.example.acedrops.model.home.Category
import com.example.acedrops.model.home.NewArrival
import com.example.acedrops.model.home.Shop
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.home.HomeRepository
import com.example.acedrops.viewModelFactory.HomeViewModelFactory
import com.example.acedrops.viewmodel.HomeViewModel

class HomeFragment : Fragment(){
    private lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    lateinit var newArrivals: List<NewArrival>
    lateinit var shops: List<Shop>
    lateinit var category: List<Category>
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
        homeViewModel.homeData
        binding.shopRecyclerView.adapter = shopAdapter
        binding.categoryRecyclerView.adapter = categoryAdapter

        homeViewModel.homeData?.observe(viewLifecycleOwner, { it ->
            if (it?.data != null) {
                it.data.also {
                    binding.progressBar.visibility = View.GONE
                    newArrivals = it.newArrival
                    shops = it.Shop
                    category = it.category
                    showNewArrivals(newArrivals)
                    shopAdapter.setShopList(shops)
                    categoryAdapter.updateCategoryList(category)
                }
            }
            if(it.errorMessage!=null){
                Toast.makeText(requireContext(), it?.errorMessage, Toast.LENGTH_SHORT)
                    .show()
                homeViewModel.homeData
            }
        })

        binding.allShopBtn.setOnClickListener {
            val bundle = bundleOf("ShopList" to shops)
            findNavController().navigate(R.id.action_homeFragment_to_allShopsFragment,bundle)
        }


        shopAdapter.setOnItemClickListener(object : ShopAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(requireContext(), "shop id ${shopAdapter.shopsList[position].id}", Toast.LENGTH_SHORT).show()
            }
        })

        categoryAdapter.setOnItemClickListener(object : CategoryHomeAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(requireContext(), categoryAdapter.categoryList[position].category, Toast.LENGTH_SHORT).show()
            }
        })
        
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeRepository = HomeRepository(ServiceBuilder.buildService())
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