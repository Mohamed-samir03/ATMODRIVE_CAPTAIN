package com.mosamir.atmodrivecaptain.features.trip.presentation.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.FragmentNewRequestTripBinding
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.SharedViewModel
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager
import com.mosamir.atmodrivecaptain.util.disable
import com.mosamir.atmodrivecaptain.util.enabled
import java.text.DecimalFormat
import java.text.NumberFormat

class NewRequestFragment:Fragment() {

    private var _binding: FragmentNewRequestTripBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    private var mTimer:Long = 60000
    private var countdownTimer: CountDownTimer? = null

    private lateinit var database: DatabaseReference
    private var valueEventListener : ValueEventListener?= null
    private var captainId = ""

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


        val model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        binding.btnAcceptTrip.setOnClickListener {
            model.setRequestStatus(true)
            val action = NewRequestFragmentDirections.actionNewRequestFragmentToTripLifecycleFragment()
            mNavController.navigate(action)
        }

        binding.btnRejectTrip.setOnClickListener {
            model.setRequestStatus(false)
        }

        database = Firebase.database.reference
        captainId = SharedPreferencesManager(requireContext()).getString(Constants.CAPTAIN_ID_PREFS)

        listenerOnTripId()

    }


    private fun listenerOnTripId(){
        valueEventListener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val aa = snapshot.getValue(Int::class.java)

                if (aa != 0){
//                    val action = NewRequestFragmentDirections.actionNewRequestFragmentToTripLifecycleFragment()
//                    mNavController.navigate(action)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        database.child("OnlineCaptains").child(captainId).child("tripId")
            .addValueEventListener(valueEventListener!!)
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
                }
                cancel()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        countdownTimer?.cancel()
        database.child("OnlineCaptains").child(captainId).child("tripId")
            .removeEventListener(valueEventListener!!)
    }

}