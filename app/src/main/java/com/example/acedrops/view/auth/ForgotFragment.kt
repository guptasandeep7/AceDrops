package com.example.acedrops.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentForgotBinding


class ForgotFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentForgotBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.nextBtn.setOnClickListener(this)
        return view
    }

    override fun onClick(view: View?) {
        val navController = findNavController()
        navController.navigate(R.id.action_forgotFragment_to_otpFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}