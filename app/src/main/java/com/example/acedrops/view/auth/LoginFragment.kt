package com.example.acedrops.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentLoginBinding
import com.example.acedrops.model.Token
import com.example.acedrops.model.UserData
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import com.example.acedrops.view.auth.AuthActivity.Companion.ACC_TOKEN
import com.example.acedrops.view.dash.DashboardActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var datastore: Datastore

    //google sign in
    var gso: GoogleSignInOptions? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    var RC_SIGN_IN = 101
    var TAG = "AuthActivity"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.signinToSignup.setOnClickListener(this)
        binding.forgotTxt.setOnClickListener(this)
        binding.signinBtn.setOnClickListener(this)
        binding.googleBtn.setOnClickListener(this)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        datastore = Datastore(requireContext())

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso!!)
    }

    override fun onClick(view: View?) {
        val navController = findNavController()
        when (view?.id) {
            R.id.signin_to_signup -> navController.navigate(R.id.action_loginFragment_to_signupFragment)
            R.id.forgot_txt -> navController.navigate(R.id.action_loginFragment_to_forgotFragment)
            R.id.signin_btn -> login()
            R.id.google_btn -> googleSignIn()
        }
    }

    private fun googleSignIn() {
        binding.progressBar.visibility = View.VISIBLE
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        }
        catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=${e.statusCode}")
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            checkToken(account.idToken)
        }
        else Toast.makeText(requireContext(), "Failed to sign with google", Toast.LENGTH_SHORT)
            .show()
    }

    private fun checkToken(idToken: String?) {
        if (idToken != null) {
            gSignUp(idToken)
        }
    }

    private fun gSignUp(idToken: String) {
        val request = ServiceBuilder.buildService(null)
        val call = request.gSignUp(Token(idToken))
        call.enqueue(object : Callback<UserData?> {
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                when {
                    response.isSuccessful -> {
                        binding.progressBar.visibility = View.GONE
                        ACC_TOKEN = response.body()?.access_token
                        runBlocking {
                            ACC_TOKEN = response.body()?.access_token
                            response.body()
                                ?.let { datastore.saveToDatastore(it, requireContext()) }
                            binding.progressBar.visibility = View.GONE
                            startActivity(Intent(activity, DashboardActivity::class.java))
                            activity?.finish()
                        }
                    }
                    response.code() == 503 -> Toast.makeText(
                        requireContext(),
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> Toast.makeText(
                        requireContext(),
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.progressBar.visibility = View.GONE
                binding.signinBtn.isEnabled = true
            }

            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed:${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //Check details are valid or not
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
            else -> true
        }
    }

    //Remove the helper text
    private fun helper() = with(binding) {
        emailLayout.helperText = ""
        passLayout.helperText = ""
    }

    //Custom Login
    private fun login() {
        binding.signinBtn.isEnabled = false
        val email = binding.email.text.toString().trim()
        val pass = binding.pass.text.toString().trim()
        val progressBar = binding.progressBar
        helper()
        if (isValid(email, pass)) {
            progressBar.visibility = View.VISIBLE
            loginApi(email = email, pass = pass)
        } else binding.signinBtn.isEnabled = true
    }

    private fun loginApi(email: String, pass: String) {
        val request = ServiceBuilder.buildService(null)
        val call = request.login(UserData(email = email, password = pass))
        call.enqueue(object : Callback<UserData?> {
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                when {
                    response.isSuccessful -> {
                        val responseBody = response.body()
                        ACC_TOKEN = responseBody?.access_token
                        runBlocking {
                            responseBody?.let { datastore.saveToDatastore(it, requireContext()) }
                            binding.progressBar.visibility = View.GONE
                            activity?.finish()
                            startActivity(Intent(activity, DashboardActivity::class.java))
                        }
                    }
                    response.code() == 401 -> Toast.makeText(
                                                requireContext(),
                                                "Wrong password",
                                                Toast.LENGTH_SHORT
                                            ).show()
                    response.code() == 422 -> Toast.makeText(
                                                requireContext(),
                                                "Enter correct email and password",
                                                Toast.LENGTH_SHORT
                                            ).show()
                    response.code() == 404 -> Toast.makeText(
                                                requireContext(),
                                                "User does not exists please signup",
                                                Toast.LENGTH_SHORT
                                            ).show()
                    else -> Toast.makeText(
                            requireContext(),
                            "User not registered",
                            Toast.LENGTH_SHORT
                        ).show()
                }
                binding.progressBar.visibility = View.GONE
                binding.signinBtn.isEnabled = true
            }

            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed:${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}