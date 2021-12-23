package com.example.acedrops.view.auth

import android.os.Bundle
import android.util.Patterns.EMAIL_ADDRESS
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

    private fun isValid(email: String, name: String, pass: String, confirmPass: String): Boolean {
        return when {
            name.isBlank() -> {
                binding.nameLayout.helperText = "Enter Name"
                false
            }
            email.isBlank() -> {
                binding.emailLayout.helperText = "Enter Email Id"
                false
            }
            !EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailLayout.helperText = "Enter valid Email Id"
                false
            }
            validPass() != null -> {
                binding.passLayout.helperText = validPass()
                false
            }
            confirmPass.isBlank() -> {
                binding.confPassLayout.helperText = "Enter Confirm Password"
                false
            }
            confirmPass != pass -> {
                binding.confPassLayout.helperText = "Confirm Password doesn't match"
                false
            }
            else -> true
        }
    }

    private fun validPass(): String? {
        val password = binding.pass.text.toString().trim()
        if (password.length < 8) {
            return "Password must contains 8 Characters"
        }
        if (!password.matches(".*[A-Z].*".toRegex()) && (!password.matches(".*[\$#%@&*/+_=?^!].*".toRegex()))) {
            return "Must contain 1 Special character and 1 upper case character (\$#%@&*/+_=?^!)"
        } else if (!password.matches(".*[a-z].*".toRegex())) {
            return "Must contain 1 Lower case character"
        } else if (!password.matches(".*[\$#%@&*/+_=?^!].*".toRegex())) {
            return "Must contain 1 Special character (\$#%@&*/+_=?^!)"
        } else if (!password.matches(".*[A-Z].*".toRegex())) {
            return "Must contain 1 upper case character"
        }
        return null
    }


    override fun onClick(view: View?) {
        val navController = findNavController()
        when (view?.id) {
            R.id.signup_to_signin -> navController.navigateUp()
            R.id.signup_btn -> {
                with(binding){
                    emailLayout.helperText = ""
                    nameLayout.helperText = ""
                    passLayout.helperText = ""
                    confPassLayout.helperText = ""
                }
                if (isValid(
                        binding.email.text.toString().trim(),
                        binding.name.text.toString().trim(),
                        binding.pass.text.toString().trim(),
                        binding.confirmPass.text.toString().trim()
                    )
                )
                    navController.navigate(R.id.action_signupFragment_to_otpFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}