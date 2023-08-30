package com.mosamir.atmodrivecaptain.futures.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mosamir.atmodrivecaptain.futures.auth.presentation.AuthActivity
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.ActivityMainBinding
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (resources.getString(R.string.mode) == "Night"){
            binding.logoSplash.setImageResource(R.drawable.logo_white)
        }else{
            binding.logoSplash.setImageResource(R.drawable.logo_black)
        }

        lifecycleScope.launchWhenStarted {
            delay(3000)

            val intent = Intent(applicationContext, AuthActivity::class.java)
            startActivity(intent)
            finish()

        }

    }
}