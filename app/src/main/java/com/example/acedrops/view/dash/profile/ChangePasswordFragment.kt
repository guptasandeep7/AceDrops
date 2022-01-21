package com.example.acedrops.view.dash.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentChangePasswordBinding
import com.example.acedrops.repository.Datastore
import com.example.acedrops.repository.auth.PasswordRepository
import com.example.acedrops.utill.validPass
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var passwordRepository: PasswordRepository
    lateinit var userEmail: String
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        bottomNavigation =
            activity?.findViewById(R.id.bottomNavigationView)!!
        bottomNavigation.visibility = View.GONE

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signBtn.setOnClickListener {
            it.isEnabled = false
            val oldPass = binding.oldPass.text.toString().trim()
            val newPass = binding.newPass.text.toString().trim()
            binding.oldPassLayout.helperText = ""
            binding.newPassLayout.helperText = ""

            val error1: String? = validPass(oldPass)
            val error2: String? = validPass(newPass)

            when {
                error1 != null -> {
                    binding.oldPassLayout.helperText = error1
                    it.isEnabled = true
                }
                error2 != null -> {
                    binding.newPassLayout.helperText = error2
                    it.isEnabled = true
                }
                else -> {
                    changePass(oldPass, newPass, userEmail)
                }
            }
        }
        return binding.root
    }

    private fun changePass(oldPass: String, newPass: String, userEmail: String) {
        binding.progressBar.visibility = View.VISIBLE
        passwordRepository.changePass(oldPass, newPass, userEmail)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passwordRepository.message.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = View.GONE
            binding.signBtn.isEnabled = true
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_changePasswordFragment_to_profileFragment)
        })

        passwordRepository.errorMessage.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            binding.signBtn.isEnabled = true
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE

        passwordRepository = PasswordRepository()
        val datastore = Datastore(requireContext())
        lifecycleScope.launch {
            userEmail = datastore.getUserDetails(Datastore.EMAIL_KEY).toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        bottomNavigation.visibility = View.VISIBLE
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.VISIBLE
    }
}