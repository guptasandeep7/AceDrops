package com.example.acedrops.view.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentLoginBinding
import com.example.acedrops.model.UserData
import com.example.acedrops.repository.Datastore
import com.example.acedrops.repository.Datastore.Companion.ACCESS_TOKEN_KEY
import com.example.acedrops.repository.Datastore.Companion.EMAIL_KEY
import com.example.acedrops.repository.Datastore.Companion.NAME_KEY
import com.example.acedrops.repository.Datastore.Companion.REF_TOKEN_KEY
import com.example.acedrops.repository.auth.LoginRepository
import com.example.acedrops.utill.validPass
import kotlinx.coroutines.launch


class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginRepository: LoginRepository
    lateinit var datastore: Datastore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        datastore = Datastore(requireContext())
        binding.signinToSignup.setOnClickListener(this)
        binding.forgotTxt.setOnClickListener(this)
        binding.signinBtn.setOnClickListener(this)
        return view
    }

    private fun isValid(email: String, pass: String): Boolean {
        return when {
            email.isBlank() -> {
                binding.emailLayout.helperText = "Enter Email Id"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailLayout.helperText = "Enter valid Email Id"
                false
            }
            validPass(pass) != null -> {
                binding.passLayout.helperText = "Incorrect password"
                false
            }
            else -> true
        }
    }

    private fun helper() = with(binding) {
        emailLayout.helperText = ""
        passLayout.helperText = ""
    }

    override fun onClick(view: View?) {
        val navController = findNavController()
        when (view?.id) {
            R.id.signin_to_signup -> navController.navigate(R.id.action_loginFragment_to_signupFragment)
            R.id.forgot_txt -> navController.navigate(R.id.action_loginFragment_to_forgotFragment)
            R.id.signin_btn -> login(navController)
        }
    }

    private fun login(navController: NavController) {
        binding.signinBtn.isEnabled = false
        val email = binding.email.text.toString().trim()
        val pass = binding.pass.text.toString().trim()
        val progressBar = binding.progressBar
        helper()

        if (isValid(email, pass)) {
            loginRepository = LoginRepository()
            progressBar.visibility = View.VISIBLE
            loginRepository.login(email = email, pass = pass)

            loginRepository.userDetails.observe(this, {
                progressBar.visibility = View.GONE
                lifecycleScope.launch {
                    saveToDatastore(it)
                    activity?.finish()
                    findNavController().navigate(R.id.action_loginFragment_to_dashboardActivity)
                }
            })

            loginRepository.errorMessage.observe(this, {
                progressBar.visibility = View.GONE
                binding.signinBtn.isEnabled = true
                Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
            })
        } else binding.signinBtn.isEnabled = true
    }

    suspend fun saveToDatastore(it: UserData) {
        datastore.changeLoginState(true)
        datastore.saveUserDetails(EMAIL_KEY, it.email!!)
        datastore.saveUserDetails(NAME_KEY, it.name!!)
        datastore.saveUserDetails(ACCESS_TOKEN_KEY, it.access_token!!)
        datastore.saveUserDetails(REF_TOKEN_KEY, it.refresh_token!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}