package com.mosamir.atmodrivecaptain.features.trip.presentation.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
    var passengerMobile = ""

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
                        Constants.captainLatLng?.latitude.toString(),
                        Constants.captainLatLng?.longitude.toString(),
                        getAddressFromLatLng(Constants.captainLatLng!!),
                        1500.0
                    )
                }
                "pay" -> {

                }
            }
        }
        binding.imgCallPassenger.setOnClickListener {
            val phoneNumber = Uri.parse("tel:$passengerMobile")
            val callIntent = Intent(Intent.ACTION_DIAL, phoneNumber)
            startActivity(callIntent)
        }
        binding.imgCancelTrip.setOnClickListener {
            cancelTripDialog()
        }
    }

    private fun cancelTripDialog(){

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cancel Trip")
        builder.setMessage("Do you want to cancel the trip?")

        builder.setPositiveButton("Yes") { dialog, which ->
            tripViewModel.cancelTrip(tripId)
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun listenerOnTripId() {
        model.tripId.observe(requireActivity(), Observer {

            if (it > 0){
                tripId = it
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
                        tripViewModel.getPassengerDetails(tripId)
                        binding.tvTripLifecycle.text = "trip accepted"
                        binding.btnTripLifecycle.text = "On The Way"
                        binding.imgCancelTrip.visibilityVisible()
                        binding.imgCallPassenger.enabled()
                    }
                    "on_the_way" -> {
                        tripViewModel.getPassengerDetails(tripId)
                        binding.tvTripLifecycle.text = "Going to pickup"
                        binding.btnTripLifecycle.text = "Arrived"
                        binding.imgCancelTrip.visibilityVisible()
                        binding.imgCallPassenger.enabled()
                    }
                    "arrived" -> {
                        tripViewModel.getPassengerDetails(tripId)
                        binding.tvTripLifecycle.text = "Waiting for passenger"
                        binding.btnTripLifecycle.text = "Start trip"
                        binding.imgCancelTrip.visibilityVisible()
                        binding.imgCallPassenger.enabled()
                    }
                    "start_trip" -> {
                        tripViewModel.getPassengerDetails(tripId)
                        binding.tvTripLifecycle.text = "Trip running"
                        binding.btnTripLifecycle.text = "Finish trip"
                        binding.imgCancelTrip.visibilityGone()
                        binding.imgCallPassenger.disable()
                    }
                    "pay" -> {
                        val action = TripLifecycleFragmentDirections.actionTripLifecycleFragmentToTripFinishedFragment()
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.passengerDetails.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as IResult<PassengerDetailsResponse>
                            displayPassengerData(data.getData()?.data!!)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.pickUpTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as IResult<TripStatusResponse>
//                        showToast(data.getData()?.message!!)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.arrivedTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as IResult<TripStatusResponse>
//                        showToast(data.getData()?.message!!)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.startTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as IResult<TripStatusResponse>
//                        showToast(data.getData()?.message!!)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.endTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as IResult<TripStatusResponse>
//                        showToast(data.getData()?.message!!)
                            SharedPreferencesManager(requireContext()).saveString(
                                Constants.TRIP_COST,
                                data.getData()?.data?.cost!!.toString()
                            )
//                        val action = TripLifecycleFragmentDirections.actionTripLifecycleFragmentToTripFinishedFragment()
//                        mNavController.navigate(action)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.cancelTrip.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            val data = networkState.data as IResult<TripStatusResponse>
                            showToast(data.getData()?.message!!)
                            model.setRequestStatus(false)
                        }

                        NetworkState.Status.FAILED -> {
                            binding.tripCycleProgressBar.visibilityGone()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                            binding.tripCycleProgressBar.visibilityVisible()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun displayPassengerData(data: PassengerDetailsData){
        dropOffLatLng = LatLng(data.dropoff_lat.toDouble(),data.dropoff_lng.toDouble())
        dropOffLocName = data.dropoff_location_name
        passengerMobile = data.passenger.mobile
        binding.apply {
            tvTripPassengerName.text = data.passenger.full_name
            tvTripLifecyclePrice.text = "${data.cost} EGP"
            tvEndLoc.text = data.dropoff_location_name
            tvStartingLoc.text = data.pickup_location_name
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        database.child("trips").child(tripId.toString()).child("status")
            .removeEventListener(valueEventListener!!)
    }

}