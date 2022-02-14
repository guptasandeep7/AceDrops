package com.example.acedrops.view.dash.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.ShopAdapter
import com.example.acedrops.databinding.FragmentAllShopsBinding
import com.example.acedrops.model.home.Shop

class AllShopsFragment : Fragment() {
    private var _binding: FragmentAllShopsBinding? = null
    private val binding get() = _binding!!
    private var shopAdapter = ShopAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllShopsBinding.inflate(inflater, container, false)
        val view = binding.root

        val shopList = arguments?.getSerializable("ShopList") as List<Shop>

        binding.allShopRecyclerView.adapter = shopAdapter

        shopAdapter.setShopList(shopList)

        shopAdapter.setOnItemClickListener(object : ShopAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle = bundleOf("ShopId" to shopAdapter.shopsList[position].id)
                findNavController().navigate(R.id.action_allShopsFragment_to_shopFragment, bundle)
            }
        })

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}