package com.mosamir.atmodrivecaptain.features.trip.presentation.fragment

import android.os.Bundle
import android.os.CountDownTimer
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
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.FragmentNewRequestTripBinding
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsData
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.SharedViewModel
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.TripViewModel
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager
import com.mosamir.atmodrivecaptain.util.disable
import com.mosamir.atmodrivecaptain.util.enabled
import com.mosamir.atmodrivecaptain.util.getAddressFromLatLng
import com.mosamir.atmodrivecaptain.util.getData
import com.mosamir.atmodrivecaptain.util.showToast
import com.mosamir.atmodrivecaptain.util.visibilityGone
import com.mosamir.atmodrivecaptain.util.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat

@AndroidEntryPoint
class NewRequestFragment:Fragment() {

    private var _binding: FragmentNewRequestTripBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    private lateinit var database: DatabaseReference

    private var mTimer:Long = 60000
    private var countdownTimer: CountDownTimer? = null

    private var captainId = ""
    private var tripId = 0
    var model = SharedViewModel()
    private val tripViewModel by viewModels<TripViewModel>()

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
        _binding = FragmentNewRequestTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            mTimer = savedInstanceState.getLong("time",60000)
        }
        startCountdownTimer()

        database = Firebase.database.reference
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        captainId = SharedPreferencesManager(requireContext()).getString(Constants.CAPTAIN_ID_PREFS)

        onClick()
        listenerOnTripId()
        observer()

    }

    private fun onClick(){
        binding.btnAcceptTrip.setOnClickListener {
            tripViewModel.acceptTrip(
                tripId,
                Constants.captainLatLng?.latitude.toString(),
                Constants.captainLatLng?.longitude.toString(),
                getAddressFromLatLng(Constants.captainLatLng!!)
            )
        }

        binding.btnRejectTrip.setOnClickListener {
            database.child("OnlineCaptains").child(captainId).child("tripId").setValue(0)
            it.disable()
        }
    }

    private fun observer(){
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.passengerDetails.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.newRequestProgressBar.visibilityGone()
                            val data = networkState.data as IResult<PassengerDetailsResponse>
                            displayPassengerData(data.getData()?.data!!)
                            countdownTimer?.start()
                        }

                        NetworkState.Status.FAILED -> {
                            binding.newRequestProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.newRequestProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.acceptTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.newRequestProgressBar.visibilityGone()
                            binding.btnAcceptTrip.enabled()
                            val data = networkState.data as IResult<TripStatusResponse>
                            model.setRequestStatus(true)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.newRequestProgressBar.visibilityGone()
                            binding.btnAcceptTrip.enabled()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.newRequestProgressBar.visibilityVisible()
                            binding.btnAcceptTrip.disable()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun listenerOnTripId() {
        model.tripId.observe(requireActivity(), Observer {

            // get-passenger-details-for-trip
            tripViewModel.getPassengerDetails(it)

        })
    }

    private fun displayPassengerData(data:PassengerDetailsData){
        tripId = data.id
        binding.apply {
            tvDropoffLocRequest.text = data.dropoff_location_name
            tvPickupLocRequest.text = data.pickup_location_name
            tvPassengerName.text = data.passenger.full_name
            tvTripPrice.text = "${data.cost} EGP"
        }
        Constants.pickUpLatLng = LatLng(data.pickup_lat.toDouble(),data.pickup_lng.toDouble())
        Constants.dropOffLatLng = LatLng(data.dropoff_lat.toDouble(),data.dropoff_lng.toDouble())
    }

    private fun startCountdownTimer(){
        countdownTimer = object : CountDownTimer(mTimer, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimerRequest.apply {
                    val f: NumberFormat = DecimalFormat("00")
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    val mText = (f.format(min)).toString() + ":" + f.format(sec)
                    text = mText
                    mTimer = millisUntilFinished
                    binding.progressBarRequestTrip.progressDrawable = resources.getDrawable(R.drawable.request_trip_green_progress_par)
                    if (sec <= 10){
                        binding.progressBarRequestTrip.progressDrawable = resources.getDrawable(R.drawable.request_trip_red_progress_bar)
                    }
                    binding.progressBarRequestTrip.progress = 100-(sec.toInt() * 100 / 60)
                }
            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                binding.tvTimerRequest.apply {
                    text = "00:00"
                    database.child("OnlineCaptains").child(captainId).child("tripId").setValue(0)
                }
                binding.btnRejectTrip.disable()
                binding.btnAcceptTrip.disable()
                cancel()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        countdownTimer = null
        _binding = null
    }

}