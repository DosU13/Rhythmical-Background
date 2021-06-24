package com.example.rhythmicalbackground.audio_service

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.Button
import com.example.rhythmicalbackground.MainActivity
import com.example.rhythmicalbackground.R

class AudioServiceManager(private val mainActivity: MainActivity){
    fun isMyServiceRunning(serviceClass: Class<AudioService>): Boolean {
        val manager : ActivityManager = mainActivity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.getRunningServices(Integer.MAX_VALUE)
            .forEach { service : ActivityManager.RunningServiceInfo ->
                if(serviceClass.name.equals(service.service.className)){
                    return true
                }
            }
        return false
    }
}