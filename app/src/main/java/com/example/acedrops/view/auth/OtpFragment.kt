package com.example.acedrops.view.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentOtpBinding
import com.example.acedrops.model.UserData
import com.example.acedrops.repository.auth.OtpRepository
import com.example.acedrops.view.auth.ForgotFragment.Companion.forgot
import com.example.acedrops.view.auth.SignupFragment.Companion.Email
import com.example.acedrops.view.auth.SignupFragment.Companion.Name
import com.example.acedrops.view.auth.SignupFragment.Companion.Pass
import kotlinx.coroutines.launch

class OtpFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private lateinit var timerCountDown: CountDownTimer
    private lateinit var otpRepository: OtpRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.nextBtn.setOnClickListener(this)
        binding.resendOtp.setOnClickListener(this)

        timerCountDown = object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendOtp.isEnabled = false
                binding.timer.text = "00:${(millisUntilFinished / 1000)}"
            }

            override fun onFinish() {
                binding.timer.visibility = View.GONE
                binding.resendOtp.isEnabled = true
            }
        }.start()

        return view
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.next_btn -> {
                next()
            }
            R.id.resend_otp -> {
                Toast.makeText(requireContext(), "OTP resend successfully", Toast.LENGTH_SHORT)
                    .show()
                timerCountDown.start()
            }
        }
    }

    private fun next() {
        val progressBar = binding.progressBar
        val otp = binding.otp.text.toString().trim()
        val btn = binding.nextBtn
        btn.isEnabled = false
        if (otp.isNotBlank()) {
            otpRepository = OtpRepository()
            timerCountDown.cancel()
            progressBar.visibility = View.VISIBLE
            if (forgot) otpRepository.forgotOtp(email = Email, otp)
            else otpRepository.otp(email = Email, pass = Pass, name = Name, otp = otp)

            otpRepository.errorMessage.observe(this, {
                Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                btn.isEnabled = true
            })

            progressBar.visibility = View.GONE
            if (forgot) {
                otpRepository.message.observe(this, {
                    findNavController().navigate(R.id.action_otpFragment_to_passwordFragment)
                })
            } else {
                otpRepository.userData.observe(this, {
                    lifecycleScope.launch {
                        LoginFragment().saveToDatastore(
                            UserData(
                                email = Email,
                                name = Name,
                                access_token = it.access_token,
                                refresh_token = it.refresh_token
                            )
                        )
                        activity?.finish()
                        findNavController().navigate(R.id.action_otpFragment_to_dashboardActivity)
                    }
                })
            }
        } else {
            btn.isEnabled = true
            binding.otpLayout.helperText = "Enter OTP"
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
        timerCountDown.cancel()
        _binding = null
    }
}