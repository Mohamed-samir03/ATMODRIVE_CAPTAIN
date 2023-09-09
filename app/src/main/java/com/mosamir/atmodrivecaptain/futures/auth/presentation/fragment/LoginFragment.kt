package com.mosamir.atmodrivecaptain.futures.auth.presentation.fragment

import android.content.Intent
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
import com.mosamir.atmodrivecaptain.futures.auth.presentation.common.AuthViewModel
import com.mosamir.atmodrivecaptain.futures.home.HomeActivity
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager
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
    private var mTimer:Long = 120000
    private var countdownTimer: CountDownTimer? = null


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong("time", mTimer)

    }

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

        if (savedInstanceState != null) {
            mTimer = savedInstanceState.getLong("time",120000)
            startCountdownTimer()
            countdownTimer?.start()
        }

        binding.tvSendCode.setOnClickListener {
            val mobile = binding.etPhoneNumber.text.toString()
            loginViewModel.sendCode(mobile)
            mTimer = 120000
            startCountdownTimer()
        }
        binding.tvResend.setOnClickListener {
            val mobile = binding.etPhoneNumber.text.toString()
            loginViewModel.sendCode(mobile)
            mTimer = 120000
            startCountdownTimer()
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
                        countdownTimer?.start()
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
                        countdownTimer?.cancel()
                        val data = networkState.data as IResult<CheckCodeResponse>
                        val mobile = binding.etPhoneNumber.text.toString()
                        if(data.getData()?.is_new == true){
                            val action = LoginFragmentDirections.actionLoginToCreateAccountPersonalInformation(mobile.toString())
                            mNavController.navigate(action)
                        }else{
                            val data = networkState.data as IResult<CheckCodeResponse>
                            saveCaptainDate(data)
                            if (data.getData()?.data?.register_step == 1){
                                val action = LoginFragmentDirections.actionLoginToCreateAccountVehicleInformation()
                                mNavController.navigate(action)
                            }else{
                                // go Home
                                val intent = Intent(requireContext(), HomeActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            }
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

    private fun saveCaptainDate(userData : IResult<CheckCodeResponse>){

        val data = userData.getData()?.data
        val myPrefs = SharedPreferencesManager(requireContext())

        myPrefs.saveString(Constants.AVATAR_PREFS,data!!.avatar)
        myPrefs.saveString(Constants.EMAIL_PREFS,data.email.toString())
        myPrefs.saveString(Constants.FULL_NAME_PREFS,data.full_name.toString())
        myPrefs.saveString(Constants.IS_DARK_MODE_PREFS,data.is_dark_mode.toString())
        myPrefs.saveString(Constants.LANG_PREFS,data.lang)
        myPrefs.saveString(Constants.MOBILE_PREFS,data.mobile)
        myPrefs.saveString(Constants.CAPTAIN_CODE_PREFS,data.captain_code)
        myPrefs.saveString(Constants.BIRTHDAY_PREFS,data.birthday.toString())
        myPrefs.saveString(Constants.REMEMBER_TOKEN_PREFS,data.remember_token)
        myPrefs.saveString(Constants.GENDER_PREFS,data.gender.toString())
        myPrefs.saveString(Constants.STATUS_PREFS,data.status.toString())
        myPrefs.saveString(Constants.IS_ACTIVE_PREFS,data.is_active.toString())
        myPrefs.saveString(Constants.NATIONALITY_PREFS,data.nationality.toString())
        myPrefs.saveString(Constants.REGISTER_STEP_PREFS,data.register_step.toString())

    }

    private fun startCountdownTimer(){
        countdownTimer = object : CountDownTimer(mTimer, 1000) {

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
                    mTimer = millisUntilFinished
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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        countdownTimer?.cancel()
    }

}