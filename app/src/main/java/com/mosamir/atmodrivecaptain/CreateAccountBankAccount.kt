package com.mosamir.atmodrivecaptain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mosamir.atmodrivecaptain.databinding.FragmentCreateAccountBankAccountBinding

class CreateAccountBankAccount:Fragment() {

    private var _binding: FragmentCreateAccountBankAccountBinding? = null
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
        _binding = FragmentCreateAccountBankAccountBinding.inflate(inflater, container, false)

        binding.btnCASubmitInformation.setOnClickListener {



        }

        binding.CABankAccountGoBack.setOnClickListener {
            val action =
                CreateAccountBankAccountDirections.actionCreateAccountBankAccountToCreateAccountVehicleInformation()
            mNavController.navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}