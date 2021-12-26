package com.example.acedrops.view.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.acedrops.R
import com.example.acedrops.databinding.FragmentOtpBinding
import com.example.acedrops.repository.OtpRepository
import com.example.acedrops.view.auth.SignupFragment.Companion.Email
import com.example.acedrops.view.auth.SignupFragment.Companion.Name
import com.example.acedrops.view.auth.SignupFragment.Companion.Pass

class OtpFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private lateinit var timerCountDown: CountDownTimer
    private val otpRepository = OtpRepository()

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
                val navController = findNavController()
                if (!binding.otp.text.toString().trim().isBlank()) {
                    timerCountDown.cancel()
                    val progressBar = binding.progressBar
                    val otp = binding.otp.text.toString().trim()
                    progressBar.visibility = View.VISIBLE
                    otpRepository.otp(email = Email, pass = Pass, name = Name, otp = otp)
                    otpRepository.errorMessage.observe(this, {
                        Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    })
                    otpRepository.message.observe(this, {
                        navController.navigate(R.id.action_otpFragment_to_dashboardActivity)
                    })
                } else
                    binding.otpLayout.helperText = "Enter OTP"
            }
            R.id.resend_otp -> {
                Toast.makeText(requireContext(), "OTP resend successfully", Toast.LENGTH_SHORT)
                    .show()
                timerCountDown.start()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}