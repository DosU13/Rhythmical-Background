package com.example.rhythmicalbackground.audio_service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.rhythmicalbackground.R

class AudioService : Service() {
    private lateinit var musicPlayer: MediaPlayer
    private var mBinder: IBinder =MyBinder()
    val curPos: Double
        get() {
//            Log.e("CurPosAsked", "in AudioService. It is equal to" + musicPlayer.currentPosition)
            return musicPlayer.currentPosition.toDouble()/1000.0}

    override fun onCreate() {
        super.onCreate()
        musicPlayer = MediaPlayer.create(this, R.raw.audio_highscore_panda_eyes)
        musicPlayer.isLooping = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Music Service started by user", Toast.LENGTH_LONG).show()
        musicPlayer.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer.stop()
        Toast.makeText(this, "Music Service destroyed by user", Toast.LENGTH_LONG).show()
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    inner class MyBinder: Binder(){
        val service : AudioService get() = this@AudioService
    }
}