package com.acedrops.acedrops.view.dash.home

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.acedrops.acedrops.R
import com.acedrops.acedrops.adapter.ShopAdapter
import com.acedrops.acedrops.databinding.FragmentAllShopsBinding
import com.acedrops.acedrops.model.home.Shop

class AllShopsFragment : Fragment() {
    private var mLastClickTime:Long = 0
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
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                } else {
                    val bundle = bundleOf("ShopId" to shopAdapter.shopsList[position].id)
                    findNavController().navigate(
                        R.id.action_allShopsFragment_to_shopFragment,
                        bundle
                    )
                }
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