package com.mosamir.atmodrivecaptain.util

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mosamir.atmodrivecaptain.R

class ForegroundLocationService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notification = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.captain)
                .setContentTitle("Captain Tracking")
                .setContentText("trip running now...")
                .build()

            startForeground(System.currentTimeMillis().toInt(),notification)
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Thread{
            for (i in 1..1000){
                Thread.sleep(1000)
                val intent = Intent("location")
                intent.putExtra("data","$i")
                LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(intent)
            }
        }.start()

        return START_REDELIVER_INTENT
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}