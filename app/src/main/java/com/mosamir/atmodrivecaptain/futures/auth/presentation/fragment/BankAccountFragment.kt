package com.mosamir.atmodrivecaptain.futures.auth.presentation.fragment

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
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.presentation.common.AuthActivity
import com.mosamir.atmodrivecaptain.futures.auth.presentation.common.AuthViewModel
import com.mosamir.atmodrivecaptain.futures.home.HomeActivity
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
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

        observeOnRegisterBankAccount()

    }

    private fun observeOnRegisterBankAccount(){
        lifecycleScope.launch {
            bankAccountViewModel.registerBankAccountResult.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<SendCodeResponse>
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}