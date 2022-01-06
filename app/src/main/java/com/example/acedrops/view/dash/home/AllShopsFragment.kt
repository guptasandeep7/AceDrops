package com.example.acedrops.view.dash.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
                Toast.makeText(
                    requireContext(),
                    "shop id ${shopAdapter.shopsList[position].id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}