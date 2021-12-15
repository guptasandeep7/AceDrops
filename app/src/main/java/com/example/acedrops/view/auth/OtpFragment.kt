package com.example.acedrops.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentForgotBinding
import com.example.acedrops.databinding.FragmentOtpBinding
import com.example.acedrops.viewmodel.AuthViewModel


class OtpFragment : Fragment(), View.OnClickListener {
    var authViewModel = AuthViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOtpBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false)
        binding.viewmodel = authViewModel
        binding.nextBtn.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

    }

    override fun onClick(p0: View?) {
        val navController = findNavController()
        navController.navigate(R.id.action_otpFragment_to_passwordFragment)
    }
}