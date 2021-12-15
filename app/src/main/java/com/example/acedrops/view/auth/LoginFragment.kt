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
import com.example.acedrops.databinding.FragmentLoginBinding
import com.example.acedrops.viewmodel.AuthViewModel


class LoginFragment : Fragment(), View.OnClickListener {
    private var authViewModel = AuthViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.viewmodel = authViewModel
        binding.signinToSignup.setOnClickListener(this)
        binding.forgotTxt.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onClick(view: View?) {
        val navController = findNavController()
        when (view?.id) {
            R.id.signin_to_signup -> navController.navigate(R.id.action_loginFragment_to_signupFragment)
            R.id.forgot_txt -> navController.navigate(R.id.action_loginFragment_to_forgotFragment)
        }
    }
}