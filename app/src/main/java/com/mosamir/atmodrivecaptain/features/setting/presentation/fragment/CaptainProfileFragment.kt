package com.mosamir.atmodrivecaptain.features.setting.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.FragmentCaptainProfileBinding

class CaptainProfileFragment : Fragment() {

    private var _binding: FragmentCaptainProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCaptainProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backFromCaptainProfile.setOnClickListener {
            activity?.finish()
        }

        binding.tvCaptainProfileRedeem.setOnClickListener {
            val action = CaptainProfileFragmentDirections.actionCaptainProfileFragmentToRedeemFragment()
            findNavController().navigate(action)
        }

        binding.tripHistoryLayout.setOnClickListener {
            val action = CaptainProfileFragmentDirections.actionCaptainProfileFragmentToTripsHistoryFragment()
            findNavController().navigate(action)
        }

        binding.btnLogout.setOnClickListener {

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}