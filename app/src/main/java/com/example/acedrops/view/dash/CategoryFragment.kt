package com.example.acedrops.view.dash

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.CategoryAdapter
import com.example.acedrops.databinding.FragmentCategoryBinding
import com.example.acedrops.model.CategoryList

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private var categoryAdapter = CategoryAdapter()
    private var mLastClickTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.searchBtn.setOnClickListener {
            findNavController().navigate(R.id.action_categoryFragment_to_searchFragment)
        }

        val categoryList = mutableListOf<CategoryList>()
        categoryList.add(CategoryList("Jewellery", R.drawable.jewelleries))
        categoryList.add(CategoryList("Paintings and portraits", R.drawable.paintings_and_portraits))
        categoryList.add(CategoryList("Bakery and chocolates", R.drawable.chocolates_and_bakery))
        categoryList.add(CategoryList("Crystal And resin art", R.drawable.crystal_and_resin_art))
        categoryList.add(CategoryList("Under garments", R.drawable.under_garments))
        categoryList.add(CategoryList("Thrift Shops", R.drawable.thrift_shops))
        categoryList.add(CategoryList("Decorative items", R.drawable.decoratives))
        categoryList.add(CategoryList("Closet and wearable", R.drawable.closet_and_wearablse))
        categoryList.add(CategoryList("Stickers and fun", R.drawable.handicrafts))
        categoryList.add(CategoryList("DIY's", R.drawable.diys))
        categoryList.add(CategoryList("Makeup and accessories", R.drawable.wearables))
        categoryList.add(CategoryList("Others", R.drawable.other))

        binding.categoryRv.adapter = categoryAdapter
        categoryAdapter.updateCategoryList(categoryList)

        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    mLastClickTime = SystemClock.elapsedRealtime()
                    val bundle =
                        bundleOf("CategoryName" to categoryAdapter.categoryList[position].categoryName)
                    view.findNavController()
                        .navigate(R.id.action_categoryFragment_to_allProductsFragment, bundle)
                }
            }
        })
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}