package com.acedrops.acedrops.view.dash.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.acedrops.acedrops.R
import com.acedrops.acedrops.databinding.FragmentAddAddressBinding
import com.acedrops.acedrops.utill.ApiResponse
import com.acedrops.acedrops.viewmodel.AddAddressViewModel

class AddAddressFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentAddAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var addAddressViewModel: AddAddressViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addAddressViewModel =
            ViewModelProvider((context as FragmentActivity?)!!)[AddAddressViewModel::class.java]

        binding.viewmodel = addAddressViewModel
        binding.backBtn.setOnClickListener(this)
        binding.saveBtn.setOnClickListener {
            helper()
            if (validDetails()) {
                saveObserver()
            }
        }

    }

    private fun saveObserver() {
        addAddressViewModel.saveAddress(requireContext()).observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Address Successfully Added",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }
                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE

                is ApiResponse.Error -> Toast.makeText(
                    requireContext(),
                    it.errorMessage ?: "Something went wrong!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_btn -> findNavController().popBackStack()
        }
    }

    private fun helper() {
        with(binding) {
            houseNoLayout.helperText = ""
            streetLayout.helperText = ""
            localityLayout.helperText = ""
            cityLayout.helperText = ""
            stateLayout.helperText = ""
        }
    }

    private fun validDetails(): Boolean {
        with(binding) {
            when {
                addAddressViewModel.houseNo.value.isNullOrBlank() -> houseNoLayout.helperText =
                    "House number cannot be blank"
                addAddressViewModel.street.value.isNullOrBlank() -> streetLayout.helperText =
                    "Street name cannot be blank"
                addAddressViewModel.locality.value.isNullOrBlank() -> localityLayout.helperText =
                    "Locality name cannot be blank"
                addAddressViewModel.city.value.isNullOrBlank() -> cityLayout.helperText =
                    "City name cannot be blank"
                addAddressViewModel.state.value.isNullOrBlank() -> stateLayout.helperText =
                    "State name cannot be blank"
                else -> return true
            }
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}