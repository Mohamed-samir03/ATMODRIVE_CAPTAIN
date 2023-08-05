package com.mosamir.atmodrivecaptain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mosamir.atmodrivecaptain.databinding.FragmentCreateAccountMobileNumberBinding

class CreateAccountMobileNumber:Fragment() {

    private var _binding: FragmentCreateAccountMobileNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateAccountMobileNumberBinding.inflate(inflater, container, false)

        binding.btnStep1Next.setOnClickListener {
            val action = CreateAccountMobileNumberDirections.actionCreateAccountMobileNumberToCreateAccountVerification2()
            mNavController.navigate(action)
        }

        binding.CAMobileNumberGoBack.setOnClickListener {
            val action = CreateAccountMobileNumberDirections.actionCreateAccountMobileNumberToLogin()
            mNavController.navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}