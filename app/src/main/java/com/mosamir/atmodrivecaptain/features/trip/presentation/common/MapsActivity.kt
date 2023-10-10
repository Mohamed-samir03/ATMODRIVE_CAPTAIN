package com.mosamir.atmodrivecaptain.features.trip.presentation.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mosamir.atmodrivecaptain.databinding.ActivityMapsBinding
import com.mosamir.atmodrivecaptain.util.visibilityGone

class MapsActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}