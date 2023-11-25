package com.mosamir.atmodrivecaptain.features.trip.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mosamir.atmodrivecaptain.databinding.FragmentTripFinishedBinding
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsData
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.SharedViewModel
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.TripViewModel
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
class TripFinishedFragment : Fragment() {


    private var _binding: FragmentTripFinishedBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    private var tripId = 0
    private var tripCost = ""
    var model = SharedViewModel()
    private val tripViewModel by viewModels<TripViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTripFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        tripCost = SharedPreferencesManager(requireContext()).getString(Constants.TRIP_COST)

        onClick()
        observer()
        listenerOnTripId()

    }

    private fun onClick() {
        binding.btnTFConfirm.setOnClickListener {
            val amount = binding.etTFPaidMoney.text.toString()
            if (amount.isNotBlank()){
                tripViewModel.confirmCash(tripId,amount.toDouble())
            }else{
                showToast("enter paid money")
            }
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.passengerDetails.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.confirmCashProgressBar.visibilityGone()
                            val data = networkState.data as IResult<PassengerDetailsResponse>
                            displayPassengerData(data.getData()?.data!!)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.confirmCashProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.confirmCashProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.confirmCashResult.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.confirmCashProgressBar.visibilityGone()
                            val data = networkState.data as IResult<TripStatusResponse>
                            model.setRequestStatus(false)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.confirmCashProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.confirmCashProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun displayPassengerData(data: PassengerDetailsData){
        if (tripCost.isBlank()){
            tripCost = data.cost
        }
        binding.apply {
            tvTFPassengerName.text = data.passenger.full_name
            tvTFTripPrice.text = "$tripCost EGP"
            tvTFFinalPrice.text = "$tripCost EGP"
        }
    }

    private fun listenerOnTripId() {
        model.tripId.observe(requireActivity(), Observer {

            tripId = it
            tripViewModel.getPassengerDetails(it)

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}