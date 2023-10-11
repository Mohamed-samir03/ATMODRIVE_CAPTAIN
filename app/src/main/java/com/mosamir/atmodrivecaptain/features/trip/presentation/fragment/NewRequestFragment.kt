package com.mosamir.atmodrivecaptain.features.trip.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mosamir.atmodrivecaptain.databinding.FragmentNewRequestTripBinding
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager

class NewRequestFragment:Fragment() {

    private var _binding: FragmentNewRequestTripBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    private lateinit var database: DatabaseReference
    private var valueEventListener : ValueEventListener?= null
    private var captainCode = ""

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

        database = Firebase.database.reference
        captainCode = SharedPreferencesManager(requireContext()).getString(Constants.CAPTAIN_CODE_PREFS)

        listenerOnTripId()

        binding.btnAcceptTrip.setOnClickListener {

            val action = NewRequestFragmentDirections.actionNewRequestFragmentToTripLifecycleFragment()
            mNavController.navigate(action)

        }

    }


    private fun listenerOnTripId(){
        valueEventListener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val aa = snapshot.getValue(Int::class.java)

                if (aa != 0){
                    val action = NewRequestFragmentDirections.actionNewRequestFragmentToTripLifecycleFragment()
                    mNavController.navigate(action)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        database.child("Online_captains").child(captainCode).child("tripId")
            .addValueEventListener(valueEventListener!!)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        database.child("Online_captains").child(captainCode).child("tripId")
            .removeEventListener(valueEventListener!!)
    }

}