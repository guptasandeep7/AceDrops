package com.example.acedrops.view.dash.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentProfileBinding
import com.example.acedrops.model.Message
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var datastore: Datastore
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions
    private var googleId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        datastore = Datastore(requireContext())

        val job = lifecycleScope.launch {
            googleId = datastore.getUserDetails(Datastore.GOOGLE_ID)
        }

        if (job.isCompleted) {
            if (googleId.equals("null")) {
                binding.changePassBtn.visibility = View.VISIBLE
            } else binding.changePassBtn.visibility = View.GONE
        }

        binding.userPhnNo.visibility = View.GONE

        binding.wishlistBtn.setOnClickListener(this)
        binding.changePassBtn.setOnClickListener(this)
        binding.manageAddrBtn.setOnClickListener(this)

        lifecycleScope.launch {
            binding.userName.text = datastore.getUserDetails(Datastore.NAME_KEY)?.lowercase()
            binding.userEmail.text = datastore.getUserDetails(Datastore.EMAIL_KEY)?.lowercase()
        }

        binding.signOutBtn.setOnClickListener {
            alertBox()
        }

        return view
    }

    private fun alertBox() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Logout")
            .setMessage("Are you sure you want to Logout?")
            .setPositiveButton("Logout") { dialog, id ->
                logout()
            }
            .setNeutralButton("Cancel") { dialog, id -> }
        val exit = builder.create()
        exit.show()
    }

    private fun logout() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            signOut(datastore.getUserDetails(Datastore.REF_TOKEN_KEY)!!)
        }
    }

    fun signOut(refToken: String) {
        val request = ServiceBuilder.buildService(null)
        val call = request.logOut(refToken)
        call.enqueue(object : Callback<Message?> {
            override fun onResponse(call: Call<Message?>, response: Response<Message?>) {
                when {
                    response.isSuccessful || response.code() == 400 -> {
                        lifecycleScope.launch {
                            datastore.changeLoginState(false)
                            signout()
                            activity?.finish()
                            findNavController().navigate(R.id.action_profileFragment_to_authActivity)
                        }

                    }
                    else -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            response.body()?.message ?: "Try Again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<Message?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signout(): Boolean {
        var result = false
        googleSignInClient.signOut().addOnCompleteListener {
            result = true
        }.addOnCanceledListener {
            result = false
        }
        return result
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.visibility =
            View.VISIBLE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.wishlist_btn -> {
                findNavController().navigate(
                    R.id.action_profileFragment_to_wishlistFragment
                )
            }
            R.id.change_pass_btn -> findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
            R.id.manage_addr_btn -> findNavController().navigate(R.id.action_profileFragment_to_addressFragment)
        }
    }
}