package com.example.acedrops.view.auth

import android.os.Bundle
import android.util.Patterns.EMAIL_ADDRESS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentForgotBinding
import com.example.acedrops.repository.auth.ForgotRepository


class ForgotFragment : Fragment() {

    companion object {
        var forgot = false
    }

    private var _binding: FragmentForgotBinding? = null
    private val binding get() = _binding!!
    private lateinit var forgotRepository: ForgotRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.nextBtn.setOnClickListener {
            val btn = binding.nextBtn
            btn.isEnabled = false
            val email = binding.email.text.toString().trim()
            binding.emailLayout.helperText = ""
            if (isValid(email)) {
                forgotRepository = ForgotRepository()
                binding.progressBar.visibility = View.VISIBLE
                forgotRepository.forgot(email)

                forgotRepository.message.observe(viewLifecycleOwner, {
                    SignupFragment.Email = email
                    forgot = true
                    findNavController().navigate(R.id.action_forgotFragment_to_otpFragment)
                })

                forgotRepository.errorMessage.observe(viewLifecycleOwner, {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    btn.isEnabled = true
                })

            } else btn.isEnabled = true
        }
        return view
    }

    private fun isValid(email: String): Boolean {
        return when {
            email.isBlank() -> {
                binding.emailLayout.helperText = "Enter Email Address"
                false
            }
            !EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailLayout.helperText = "Enter valid Email Address"
                false
            }
            else -> true
        }
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