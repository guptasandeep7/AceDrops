package com.example.acedrops.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentLandingBinding
import com.example.acedrops.databinding.FragmentLoginBinding
import com.example.acedrops.repository.Datastore
import com.example.acedrops.view.auth.AuthActivity.Companion.ACC_TOKEN
import kotlinx.coroutines.launch

class LandingFragment : Fragment() {

    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!
    lateinit var datastore: Datastore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        val view = binding.root
        datastore = Datastore(requireContext())

        lifecycleScope.launch {
            if (datastore.isLogin()) {
                ACC_TOKEN = datastore.getUserDetails(Datastore.ACCESS_TOKEN_KEY)
                activity?.finish()
                findNavController().navigate(R.id.action_landingFragment_to_dashboardActivity)
            }
        }

        binding.signinBtn.setOnClickListener{
            findNavController().navigate(R.id.action_landingFragment_to_loginFragment)
        }

        binding.signupBtn.setOnClickListener{
            findNavController().navigate(R.id.action_landingFragment_to_signupFragment)
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = android.app.AlertDialog.Builder(activity)
                builder.setTitle("Exit")
                    .setMessage("Are you sure you want to Exit?")
                    .setPositiveButton("Exit") { dialog, id ->
                        activity?.finish()
                    }
                    .setNeutralButton("Cancel") { dialog, id -> }
                val exit = builder.create()
                exit.show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}