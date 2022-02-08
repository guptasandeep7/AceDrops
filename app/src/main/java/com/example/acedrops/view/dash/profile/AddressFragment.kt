package com.example.acedrops.view.dash.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.AddressAdapter
import com.example.acedrops.databinding.FragmentAddressBinding
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.AddressViewModel
import com.example.acedrops.viewmodel.OrderViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ViewOverlayImpl

class AddressFragment : Fragment() {
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var addressViewModel: AddressViewModel
    private val addressAdapter = AddressAdapter()
    private val orderViewModel = OrderViewModel()
    private var lastFragment: String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[AddressViewModel::class.java]

        getAddress()

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE

        binding.backBtn.setOnClickListener { findNavController().popBackStack() }

        binding.addAddressBtn.setOnClickListener { findNavController().navigate(R.id.action_addressFragment_to_addAddressFragment) }

        addressAdapter.setOnItemClickListener(object : AddressAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if (lastFragment == "Cart")
                    orderCart(position)
            }
        })

    }

    private fun orderCart(position: Int) {
        orderViewModel.orderCart(addressAdapter.addressList[position].id!!, requireContext())
            .observe(viewLifecycleOwner, {
            when(it){
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Order has been successfully placed", Toast.LENGTH_SHORT)
                        .show()
                }

                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE

                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            })
    }

    private fun getAddress() {
        addressViewModel.getAddress(requireContext()).observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data == null) {
                        binding.empty.visibility = View.VISIBLE
                        binding.productsRecyclerView.visibility = View.GONE
                    } else {
                        binding.productsRecyclerView.adapter = addressAdapter
                        addressAdapter.updateAddressList(it.data)
                    }
                }
                is ApiResponse.Loading -> {
                    binding.empty.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        it.errorMessage ?: "Something went wrong!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.GONE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE

        arguments?.let {
            lastFragment = it.getString("Cart") ?: it.getString("Product").toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.VISIBLE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.VISIBLE
    }
}