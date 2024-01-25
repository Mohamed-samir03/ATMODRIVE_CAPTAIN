package com.mosamir.atmodrivecaptain.util

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CaptainApp:Application() {
    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    private fun createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID,"Trip Tracking",NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "trip tracking for captain"
            channel.lightColor = Color.GREEN
            channel.enableLights(true)
            channel.vibrationPattern = longArrayOf(0, 200, 100, 300)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}