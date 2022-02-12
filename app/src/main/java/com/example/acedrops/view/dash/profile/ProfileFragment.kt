package com.example.acedrops.view.dash.profile

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentProfileBinding
import com.example.acedrops.model.Message
import com.example.acedrops.network.ServiceBuilder
import com.example.acedrops.repository.Datastore
import com.example.acedrops.repository.Datastore.Companion.PHN_NUMBER
import com.example.acedrops.utill.ApiResponse
import com.example.acedrops.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
    private val profileViewModel: ProfileViewModel by activityViewModels()

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

        lifecycleScope.launch {
            binding.userName.text = datastore.getUserDetails(Datastore.NAME_KEY)?.lowercase()
            binding.userEmail.text = datastore.getUserDetails(Datastore.EMAIL_KEY)?.lowercase()
            binding.userPhnNo.text = datastore.getUserDetails(Datastore.PHN_NUMBER)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wishlistBtn.setOnClickListener(this)
        binding.changePassBtn.setOnClickListener(this)
        binding.manageAddrBtn.setOnClickListener(this)
        binding.orderBtn.setOnClickListener(this)
        binding.phnBtn.setOnClickListener(this)
        binding.signOutBtn.setOnClickListener(this)

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
            R.id.order_btn -> findNavController().navigate(R.id.action_profileFragment_to_myOrdersFragment)
            R.id.sign_out_btn -> alertBox()
            R.id.phn_btn -> bottomSheet()
        }
    }

    private fun bottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.phn_no_dialog, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()
        val cancelBtn = view.findViewById<MaterialButton>(R.id.cancel_btn)
        val addBtn = view.findViewById<MaterialButton>(R.id.add_btn)
        val phnNumber = view.findViewById<TextInputEditText?>(R.id.phn_no).text
        val phnNumberLayout = view.findViewById<TextInputLayout>(R.id.phn_no_layout)
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        addBtn.setOnClickListener {
            if (!phnNumber.isNullOrBlank() && phnNumber.length == 10) {
                addPhnNumber(phnNumber, dialog)
            } else phnNumberLayout.helperText = "Phone number should be 10 digit only"
        }
    }

    private fun addPhnNumber(
        phnNumber: Editable,
        dialog: BottomSheetDialog
    ) {
        profileViewModel.addPhoneNumber(
            phnNumber.trim().toString().toLong(),
            requireContext()
        )?.observe(viewLifecycleOwner, {
            when (it) {
                is ApiResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.userPhnNo.text = phnNumber.trim().toString()
                    lifecycleScope.launch {
                        Datastore(requireContext()).saveUserDetails(
                            PHN_NUMBER,
                            phnNumber.trim().toString()
                        )
                    }
                    dialog.dismiss()
                }
                is ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE
                is ApiResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                }
            }
        })
    }

}