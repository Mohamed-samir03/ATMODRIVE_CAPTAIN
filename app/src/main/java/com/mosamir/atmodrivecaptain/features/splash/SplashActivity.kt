package com.mosamir.atmodrivecaptain.features.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mosamir.atmodrivecaptain.features.auth.presentation.common.AuthActivity
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.ActivitySplashBinding
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.TripActivity
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (resources.getString(R.string.mode) == "Night"){
            binding.logoSplash.setImageResource(R.drawable.logo_white)
        }else{
            binding.logoSplash.setImageResource(R.drawable.logo_black)
        }

        lifecycleScope.launchWhenStarted {
            delay(3000)

            val token  = SharedPreferencesManager(this@SplashActivity).getString(Constants.REMEMBER_TOKEN_PREFS)
            val registerStep  = SharedPreferencesManager(this@SplashActivity).getString(Constants.REGISTER_STEP_PREFS)

            if (token.isNullOrBlank() || (registerStep != "3" && registerStep != "2") ){
                val intent = Intent(applicationContext, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(applicationContext, TripActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

    }
}