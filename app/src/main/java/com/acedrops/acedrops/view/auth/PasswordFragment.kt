package com.acedrops.acedrops.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.acedrops.acedrops.R
import com.acedrops.acedrops.databinding.FragmentPasswordBinding
import com.acedrops.acedrops.model.Message
import com.acedrops.acedrops.model.UserData
import com.acedrops.acedrops.network.ServiceBuilder
import com.acedrops.acedrops.utill.validPass
import com.acedrops.acedrops.view.auth.SignupFragment.Companion.Email
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordFragment : Fragment() {
    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        binding.progressBar.visibility = View.VISIBLE
        newPass(Email, pass = pass)
    }

    fun newPass(email: String, pass: String) {
        val request = ServiceBuilder.buildService(null)
        val call = request.newPass(UserData(email = email, newpass = pass))
        call.enqueue(object : Callback<Message?> {
            override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                when {
                    response.isSuccessful -> findNavController().navigate(R.id.action_passwordFragment_to_loginFragment)
                    response.code() == 422 -> errorMessage("Enter valid password")
                    response.code() == 401 -> errorMessage("Session expired")
                    response.code() == 400 -> errorMessage("Try again")
                    else -> errorMessage("Something went wrong! Try again")
                }
            }

            override fun onFailure(call: Call<Message?>, t: Throwable) {
                errorMessage(t.message.toString())
            }
        })
    }

    private fun errorMessage(it: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        binding.signBtn.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}