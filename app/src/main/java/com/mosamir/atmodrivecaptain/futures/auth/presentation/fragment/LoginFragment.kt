package com.mosamir.atmodrivecaptain.futures.auth.presentation.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.FragmentLoginBinding
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.CheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.presentation.AuthViewModel
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.disable
import com.mosamir.atmodrivecaptain.util.enabled
import com.mosamir.atmodrivecaptain.util.getData
import com.mosamir.atmodrivecaptain.util.showToast
import com.mosamir.atmodrivecaptain.util.visibilityGone
import com.mosamir.atmodrivecaptain.util.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat

@AndroidEntryPoint
class LoginFragment:Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController
    private val loginViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (resources.getString(R.string.mode) == "Night"){
            binding.layoutLogin.setBackgroundResource(R.drawable.mapviewdark)
        }else{
            binding.layoutLogin.setBackgroundResource(R.drawable.mapview)
        }

        binding.tvSendCode.setOnClickListener {
            val mobile = binding.etPhoneNumber.text.toString()
            loginViewModel.sendCode(mobile)
        }
        binding.tvResend.setOnClickListener {
            val mobile = binding.etPhoneNumber.text.toString()
            loginViewModel.sendCode(mobile)
            countDownTimer.onTick(120000)
            countDownTimer.start()
        }
        observeOnSendCode()

        binding.btnContinue.setOnClickListener {
            val mobile = binding.etPhoneNumber.text.toString()
            val otpCode = binding.tvOtpCode.text.toString()
            loginViewModel.checkCode(mobile,otpCode,"device_token:${mobile}")
        }
        observeOnCheckCode()

    }

    private fun observeOnSendCode(){
        lifecycleScope.launch {
            loginViewModel.sendCodeResult.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<SendCodeResponse>
                        showToast(data.getData()!!.message)
                        binding.loginProgressBar.visibilityGone()
                        countDownTimer.onTick(120000)
                        countDownTimer.start()
                    }
                    NetworkState.Status.FAILED ->{
                        showToast(networkState.msg.toString())
                        binding.loginProgressBar.visibilityGone()
                    }
                    NetworkState.Status.RUNNING ->{
                        binding.loginProgressBar.visibilityVisible()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observeOnCheckCode(){
        lifecycleScope.launch {
            loginViewModel.checkCodeResult.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        countDownTimer.cancel()
                        val data = networkState.data as IResult<CheckCodeResponse>
                        if(data.getData()?.is_new == true){
                            val action = LoginFragmentDirections.actionLoginToCreateAccountPersonalInformation()
                            mNavController.navigate(action)
                        }else{
                            // go Home
                            showToast("Successful Go Home")
                        }
                        binding.loginProgressBar.visibilityGone()
                    }
                    NetworkState.Status.FAILED ->{
                        showToast(networkState.msg.toString())
                        binding.loginProgressBar.visibilityGone()
                    }
                    NetworkState.Status.RUNNING ->{
                        binding.loginProgressBar.visibilityVisible()
                    }
                    else -> {}
                }
            }
        }
    }

    private val countDownTimer =
        object : CountDownTimer(120000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResend.apply {
                    val f: NumberFormat = DecimalFormat("00")
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    val mText =
                        "<font color='#B2C3C9'>Resend(${(f.format(min)).toString() + ":" + f.format(sec)}s)</font>"
                    setText(Html.fromHtml(mText), TextView.BufferType.SPANNABLE)
                    disable()
                }
            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                binding.tvResend.apply {
                    val mText = "<font color='#00A6A6'><u>Resend</u></font>"
                    setText(Html.fromHtml(mText), TextView.BufferType.SPANNABLE)
                    enabled()
                }
                cancel()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        countDownTimer.cancel()
    }

}