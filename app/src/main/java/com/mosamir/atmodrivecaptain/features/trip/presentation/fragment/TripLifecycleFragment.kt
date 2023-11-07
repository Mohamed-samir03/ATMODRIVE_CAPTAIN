package com.mosamir.atmodrivecaptain.features.trip.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.mosamir.atmodrivecaptain.databinding.FragmentTripLifecycleBinding
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsData
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.SharedViewModel
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.TripViewModel
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.getData
import com.mosamir.atmodrivecaptain.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TripLifecycleFragment:Fragment() {


    private var _binding: FragmentTripLifecycleBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    private var valueEventListener : ValueEventListener?= null
    private lateinit var database: DatabaseReference

    private val tripViewModel by viewModels<TripViewModel>()

    var model = SharedViewModel()
    var tripId = 0
    var tripStats:String? = ""
    var dropOffLatLng: LatLng? = null
    var dropOffLocName:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTripLifecycleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        onClick()
        listenerOnTripId()
        observer()

    }

    private fun onClick(){
        binding.btnTripLifecycle.setOnClickListener {
            when (tripStats) {
                "accepted" -> {
                    tripViewModel.pickUpTrip(tripId)
                }
                "on_the_way" -> {
                    tripViewModel.arrivedTrip(tripId)
                }
                "arrived" -> {
                    tripViewModel.startTrip(tripId)
                }
                "start_trip" -> {
                    tripViewModel.endTrip(
                        tripId,
                        dropOffLatLng?.latitude.toString(),
                        dropOffLatLng?.longitude.toString(),
                        dropOffLocName,
                        1500.0)
                }
                "end_trip" -> {

                }
            }
        }
    }

    private fun listenerOnTripId() {
        model.tripId.observe(requireActivity(), Observer {

            if (it > 0){
                tripId = it
                tripViewModel.getPassengerDetails(tripId)
                listenerOnTrip()
            }

        })
    }

    private fun listenerOnTrip(){
        valueEventListener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val stats = snapshot.getValue(String::class.java)

                tripStats = stats

                when (tripStats) {
                    null -> {
                        model.setRequestStatus(false)
                    }
                    "accepted" -> {
                        binding.tvTripLifecycle.text = "trip accepted"
                        binding.btnTripLifecycle.text = "On The Way"
                    }
                    "on_the_way" -> {
                        binding.tvTripLifecycle.text = "Going to pickup"
                        binding.btnTripLifecycle.text = "Arrived"
                    }
                    "arrived" -> {
                        binding.tvTripLifecycle.text = "Waiting for passenger"
                        binding.btnTripLifecycle.text = "Start trip"
                    }
                    "start_trip" -> {
                        binding.tvTripLifecycle.text = "Trip running"
                        binding.btnTripLifecycle.text = "Finish trip"
                    }
                    "end_trip" -> {
                        val action = TripLifecycleFragmentDirections.actionTripLifecycleFragment2ToTripFinishedFragment2()
                        mNavController.navigate(action)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        database.child("trips").child(tripId.toString()).child("status")
            .addValueEventListener(valueEventListener!!)
    }

    private fun observer(){
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
        lifecycleScope.launch {
            tripViewModel.pickUpTrip.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<TripStatusResponse>
                        showToast(data.getData()?.message!!)
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

        lifecycleScope.launch {
            tripViewModel.arrivedTrip.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<TripStatusResponse>
                        showToast(data.getData()?.message!!)
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
        lifecycleScope.launch {
            tripViewModel.startTrip.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<TripStatusResponse>
                        showToast(data.getData()?.message!!)
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
        lifecycleScope.launch {
            tripViewModel.endTrip.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<TripStatusResponse>
                        showToast(data.getData()?.message!!)
                        val action = TripLifecycleFragmentDirections.actionTripLifecycleFragment2ToTripFinishedFragment2()
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

    private fun displayPassengerData(data: PassengerDetailsData){
        dropOffLatLng = LatLng(data.dropoff_lat.toDouble(),data.dropoff_lng.toDouble())
        dropOffLocName = data.dropoff_location_name
        binding.apply {
            tvEndLoc.text = data.dropoff_location_name
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        database.child("trips").child(tripId.toString()).child("status")
            .removeEventListener(valueEventListener!!)
    }

}