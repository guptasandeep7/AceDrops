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
import com.example.acedrops.databinding.FragmentAddAddressBinding
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.dashboard.AddAddressRepository
import com.example.acedrops.repository.dashboard.AddressRepository
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.utill.generateToken
import com.example.acedrops.view.auth.AuthActivity
import com.example.acedrops.viewModelFactory.AddAddressViewModelFactory
import com.example.acedrops.viewModelFactory.AddressViewModelFactory
import com.example.acedrops.viewmodel.AddAddressViewModel
import com.example.acedrops.viewmodel.AddressViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class AddAddressFragment : Fragment(), View.OnClickListener{
    private var _binding: FragmentAddAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var addAddressViewModel:AddAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE

        binding.viewmodel = addAddressViewModel
        binding.backBtn.setOnClickListener(this)
        binding.saveBtn.setOnClickListener{
            helper()
            if(validDetails()) {
                addAddressViewModel.saveAddress()
                saveObserver()
            }
        }

        return binding.root
    }

    private fun saveObserver() {
        addAddressViewModel.result.observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Address Successfully Added",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE

                is ApiResponse.TokenExpire -> {
    //                    Toast.makeText(requireContext(), "generateToken expire", Toast.LENGTH_SHORT)
    //                        .show()
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
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE

        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
        addAddressViewModel = ViewModelProvider(
            this,
            AddAddressViewModelFactory(AddAddressRepository(ServiceBuilder.buildService(AuthActivity.ACC_TOKEN)))
        )[AddAddressViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.VISIBLE
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_btn -> findNavController().popBackStack()
        }
    }

    private fun helper() {
        with(binding){
            houseNoLayout.helperText = ""
            streetLayout.helperText = ""
            localityLayout.helperText = ""
            cityLayout.helperText = ""
            stateLayout.helperText = ""
        }
    }

    private fun validDetails():Boolean{
        with(binding){
            when{
                addAddressViewModel.houseNo.value.isNullOrBlank() -> houseNoLayout.helperText = "House number cannot be blank"
                addAddressViewModel.street.value.isNullOrBlank() -> streetLayout.helperText = "Street name cannot be blank"
                addAddressViewModel.locality.value.isNullOrBlank() -> localityLayout.helperText = "Locality name cannot be blank"
                addAddressViewModel.city.value.isNullOrBlank() -> cityLayout.helperText = "City name cannot be blank"
                addAddressViewModel.state.value.isNullOrBlank() -> stateLayout.helperText = "State name cannot be blank"
                else -> return true
            }
        }
        return false
    }

}