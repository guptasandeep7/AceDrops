package com.example.acedrops.view.dash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.CategoryAdapter
import com.example.acedrops.databinding.FragmentCategoryBinding
import com.example.acedrops.model.CategoryList

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private var categoryAdapter = CategoryAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        val categoryList = mutableListOf<CategoryList>()
        categoryList.add(CategoryList("Jewellery", R.drawable.ic_jwellery))
        categoryList.add(CategoryList("Paintings and portraits", R.drawable.ic_paintings))
        categoryList.add(CategoryList("Bakery and chocolates", R.drawable.ic_choco_bakery))
        categoryList.add(CategoryList("Crystal And resin art", R.drawable.ic_crystal_art))
        categoryList.add(CategoryList("Under garments", R.drawable.ic_women_fashion))
        categoryList.add(CategoryList("Thrift Shops", R.drawable.ic_women_fashion))
        categoryList.add(CategoryList("Decorative items", R.drawable.ic_women_fashion))
        categoryList.add(CategoryList("Closet and wearable", R.drawable.ic_women_fashion))
        categoryList.add(CategoryList("Stickers and fun", R.drawable.ic_women_fashion))
        categoryList.add(CategoryList("DIY's", R.drawable.ic_women_fashion))
        categoryList.add(CategoryList("Makeup and accessories", R.drawable.ic_women_fashion))
        categoryList.add(CategoryList("Others", R.drawable.ic_women_fashion))


        binding.categoryRv.adapter = categoryAdapter
        categoryAdapter.updateCategoryList(categoryList)

        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle =
                    bundleOf("CategoryName" to categoryAdapter.categoryList[position].categoryName)
                view.findNavController()
                    .navigate(R.id.action_categoryFragment_to_allProductsFragment, bundle)
            }
        })
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}