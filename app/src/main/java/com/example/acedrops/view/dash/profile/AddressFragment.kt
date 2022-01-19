package com.example.acedrops.view.dash.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.adapter.AddressAdapter
import com.example.acedrops.databinding.FragmentAddressBinding
import com.example.acedrops.model.AddressResponse
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.dashboard.AddressRepository
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.generateToken
import com.example.acedrops.view.auth.AuthActivity
import com.example.acedrops.viewModelFactory.AddressViewModelFactory
import com.example.acedrops.viewmodel.AddressViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class AddressFragment : Fragment() {
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var addressViewModel: AddressViewModel
    private lateinit var bottomNavigation: BottomNavigationView
    private val addressAdapter = AddressAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)

        bottomNavigation =
            activity?.findViewById(R.id.bottomNavigationView)!!
        bottomNavigation.visibility = View.GONE

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.productsRecyclerView.adapter = addressAdapter

        addressAdapter.setOnItemClickListener(object : AddressAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressViewModel.address.observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data == null) {
                        binding.empty.visibility = View.VISIBLE
                    } else addressAdapter.updateAddressList(it.data)
                }
                is ApiResponse.Loading -> {
                    binding.empty.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ApiResponse.TokenExpire -> {
                    Toast.makeText(requireContext(), "generateToken expire", Toast.LENGTH_SHORT)
                        .show()
                    Log.w("access generateToken ", "ACC_TOKEN is ${AuthActivity.ACC_TOKEN}")
                    lifecycleScope.launch {
                        generateToken(requireContext())
                    }
                }
                is ApiResponse.Error -> Toast.makeText(
                    requireContext(),
                    it.errorMessage ?: "Something went wrong!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        generateToken.observe(viewLifecycleOwner, {
            if (it == null) {
                findNavController().navigate(R.id.action_addressFragment_to_authActivity)
                activity?.finish()
            } else {
                addressViewModel.getAddress()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
        addressViewModel = ViewModelProvider(
            this,
            AddressViewModelFactory(AddressRepository(ServiceBuilder.buildService(AuthActivity.ACC_TOKEN)))
        )[AddressViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        bottomNavigation.visibility = View.VISIBLE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.VISIBLE
    }
}