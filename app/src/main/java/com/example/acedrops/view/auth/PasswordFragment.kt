package com.example.acedrops.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentPasswordBinding
import com.example.acedrops.repository.auth.LoginRepository
import com.example.acedrops.repository.auth.PasswordRepository
import com.example.acedrops.utill.validPass
import com.example.acedrops.view.auth.SignupFragment.Companion.Email
import com.example.acedrops.view.auth.SignupFragment.Companion.Pass
import kotlinx.coroutines.launch

class PasswordFragment : Fragment() {
    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var passwordRepository:PasswordRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)

        binding.signBtn.setOnClickListener {
            it.isEnabled = false
            val pass = binding.newPass.text.toString().trim()
            val confPass = binding.confPass.text.toString().trim()
            binding.passLayout.helperText = ""
            binding.confPassLayout.helperText = ""

            when (validPass(pass)) {
                null -> {
                    if (pass == confPass) {
                        signIn(pass)
                    } else {
                        binding.confPassLayout.helperText = "Confirm doesn't match"
                        it.isEnabled = true
                    }
                }
                else -> {
                    binding.passLayout.helperText = validPass(pass)
                    it.isEnabled = true
                }
            }
        }
        return binding.root
    }

    private fun signIn(pass: String) {
        passwordRepository = PasswordRepository()
        binding.progressBar.visibility = View.VISIBLE
        passwordRepository.newPass(Email, pass = pass)

        passwordRepository.message.observe(viewLifecycleOwner, {
            navToDash(pass)
        })

        passwordRepository.errorMessage.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            binding.signBtn.isEnabled = true
        })
    }

    private fun navToDash(pass: String) {
        LoginRepository().login(Email, pass = pass)
        LoginRepository().userDetails.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                LoginFragment().saveToDatastore(it)
                activity?.finish()
                findNavController().navigate(R.id.action_passwordFragment_to_dashboardActivity)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}