package com.mosamir.atmodrivecaptain.features.auth.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mosamir.atmodrivecaptain.databinding.FragmentBankAccountBinding
import com.mosamir.atmodrivecaptain.features.auth.domain.model.register.RegisterResponse
import com.mosamir.atmodrivecaptain.features.auth.presentation.common.AuthViewModel
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.HomeActivity
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager
import com.mosamir.atmodrivecaptain.util.getData
import com.mosamir.atmodrivecaptain.util.showToast
import com.mosamir.atmodrivecaptain.util.visibilityGone
import com.mosamir.atmodrivecaptain.util.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BankAccountFragment:Fragment() {

    private var _binding: FragmentBankAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController
    private val bankAccountViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBankAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmitInformation.setOnClickListener {
            val bankName = binding.etBankName.text.toString()
            val ibanNumber = binding.etIBANNumber.text.toString()
            val accountName = binding.etAccountPersonalName.text.toString()
            val accountNumber = binding.etAccountNumber.text.toString()
            bankAccountViewModel.registerBankAccount(bankName,ibanNumber,accountName,accountNumber)
        }

        binding.btnSkip.setOnClickListener {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        observeOnRegisterBankAccount()

    }

    private fun observeOnRegisterBankAccount(){
        lifecycleScope.launch {
            bankAccountViewModel.registerBankAccountResult.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<RegisterResponse>
                        saveCaptainDate(data)
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                        binding.bankAccountProgressBar.visibilityGone()
                    }
                    NetworkState.Status.FAILED ->{
                        showToast(networkState.msg.toString())
                        binding.bankAccountProgressBar.visibilityGone()
                    }
                    NetworkState.Status.RUNNING ->{
                        binding.bankAccountProgressBar.visibilityVisible()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun saveCaptainDate(userData : IResult<RegisterResponse>){

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}