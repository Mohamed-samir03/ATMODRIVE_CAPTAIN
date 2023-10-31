package com.mosamir.atmodrivecaptain.features.trip.presentation.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mosamir.atmodrivecaptain.databinding.ActivityTripBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripActivity : AppCompatActivity(){

    private lateinit var binding: ActivityTripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}