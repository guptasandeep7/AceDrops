package com.example.acedrops.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentSignupBinding

class SignupFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.signupBtn.setOnClickListener(this)
        binding.signupToSignin.setOnClickListener(this)

        return view
    }


    override fun onClick(view: View?) {
        val navController = findNavController()
        when (view?.id) {
            R.id.signup_to_signin -> navController.navigateUp()
            R.id.signup_btn -> navController.navigate(R.id.action_signupFragment_to_otpFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}