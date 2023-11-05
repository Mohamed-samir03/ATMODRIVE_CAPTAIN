package com.mosamir.atmodrivecaptain.features.trip.presentation.fragment

import android.location.Geocoder
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.FragmentNewRequestTripBinding
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsData
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.UpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.SharedViewModel
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.TripViewModel
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager
import com.mosamir.atmodrivecaptain.util.disable
import com.mosamir.atmodrivecaptain.util.enabled
import com.mosamir.atmodrivecaptain.util.getData
import com.mosamir.atmodrivecaptain.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class NewRequestFragment:Fragment() {

    private var _binding: FragmentNewRequestTripBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    private var mTimer:Long = 60000
    private var countdownTimer: CountDownTimer? = null

    private lateinit var database: DatabaseReference
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
            if(mTimer != 60000.toLong()){
                startCountdownTimer()
                countdownTimer?.start()
            }
        }
        startCountdownTimer()
        countdownTimer?.start()

        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        database = Firebase.database.reference
        captainId = SharedPreferencesManager(requireContext()).getString(Constants.CAPTAIN_ID_PREFS)

        onClick()
        listenerOnTripId()
        observeOnPassengerDetails()
        observeOnAcceptTrip()
        observeOnCancelTrip()

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
            tripViewModel.cancelTrip(tripId)
        }
    }

    private fun observeOnPassengerDetails(){
        lifecycleScope.launch {
            tripViewModel.passengerDetails.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<PassengerDetailsResponse>
                        displayPassengerData(data.getData()?.data!!)
                    }
                    NetworkState.Status.FAILED ->{
                        showToast(networkState.msg.toString())
                    }
                    NetworkState.Status.RUNNING ->{
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observeOnAcceptTrip(){
        lifecycleScope.launch {
            tripViewModel.acceptTrip.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<TripStatusResponse>
                        model.setRequestStatus(true)
                        val action = NewRequestFragmentDirections.actionNewRequestFragment2ToTripLifecycleFragment2()
                        mNavController.navigate(action)
                    }
                    NetworkState.Status.FAILED ->{
                        showToast(networkState.msg.toString())
                    }
                    NetworkState.Status.RUNNING ->{
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observeOnCancelTrip(){
        lifecycleScope.launch {
            tripViewModel.cancelTrip.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<TripStatusResponse>
                        model.setRequestStatus(false)
                    }
                    NetworkState.Status.FAILED ->{
                        showToast(networkState.msg.toString())
                    }
                    NetworkState.Status.RUNNING ->{
                    }
                    else -> {}
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
        }
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
                    tripViewModel.cancelTrip(tripId)
                }
                cancel()
            }
        }
    }

    private fun getAddressFromLatLng(latLng: LatLng): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses?.isNotEmpty()!!) {
                val address = addresses[0]
                // You can format the address as per your requirements
                return address.getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            showToast("Error getting address")
        }
        return "Address not found"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        countdownTimer?.cancel()
    }

}