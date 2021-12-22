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
import com.example.acedrops.databinding.FragmentSignupBinding
import com.example.acedrops.viewmodel.AuthViewModel

class SignupFragment : Fragment(), View.OnClickListener {
    var authViewModel = AuthViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSignupBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        binding.viewmodel = authViewModel
        binding.signupBtn.setOnClickListener(this)
        binding.signupToSignin.setOnClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onClick(view: View?) {
        val navController = findNavController()
        when (view?.id) {
            R.id.signup_to_signin -> navController.navigateUp()
            R.id.signup_btn -> navController.navigate(R.id.action_signupFragment_to_otpFragment)
        }
    }
}