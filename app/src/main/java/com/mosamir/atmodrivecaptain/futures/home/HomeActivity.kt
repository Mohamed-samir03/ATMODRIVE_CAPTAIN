package com.mosamir.atmodrivecaptain.futures.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.futures.auth.presentation.common.AuthActivity
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btn = findViewById<Button>(R.id.logout)
        val tvName = findViewById<TextView>(R.id.home_name)

        btn.setOnClickListener {

            SharedPreferencesManager(this).clearString(Constants.REMEMBER_TOKEN_PREFS)
            val intent = Intent(applicationContext, AuthActivity::class.java)
            startActivity(intent)
            finish()

        }

        val name = SharedPreferencesManager(this).getString(Constants.FULL_NAME_PREFS)

        tvName.text = "Hi Captain $name"

    }
}