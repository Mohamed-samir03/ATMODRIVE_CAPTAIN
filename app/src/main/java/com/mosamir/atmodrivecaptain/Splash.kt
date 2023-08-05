package com.mosamir.atmodrivecaptain

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mosamir.atmodrivecaptain.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay

class Splash:Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        lifecycleScope.launchWhenStarted {
            delay(3000)

            val action = SplashDirections.actionSplashToLogin()
            mNavController.navigate(action)

        }

        if (resources.getString(R.string.mode) == "Night"){
            binding.logoSplash.setImageResource(R.drawable.logo_white)
        }else{
            binding.logoSplash.setImageResource(R.drawable.logo_black)
        }

//        Handler(Looper.getMainLooper()).postDelayed({
//            val action = SplashDirections.actionSplashToLogin()
//            mNavController.navigate(action)
//        },3000)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}