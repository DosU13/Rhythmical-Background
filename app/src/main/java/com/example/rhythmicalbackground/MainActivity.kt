package com.example.rhythmicalbackground

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.rhythmicalbackground.audio_service.AudioService
import com.example.rhythmicalbackground.audio_service.AudioServiceManager
import com.example.rhythmicalbackground.data.local.MidiTxtReader
import com.example.rhythmicalbackground.util.AnimAlgorithm

class MainActivity : AppCompatActivity(), ServiceConnection{
    private lateinit var handler: Handler
    private var audioService: AudioService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val audioManager = AudioServiceManager(this)
        findViewById<Button>(R.id.btn).setOnClickListener{buttonPressed(audioManager)}
        val mainLayout = findViewById<ConstraintLayout>(R.id.main_layout)
        val txtView = findViewById<TextView>(R.id.textView)
        val rect1 = findViewById<View>(R.id.rectangle1)
        val rect2 = findViewById<View>(R.id.rectangle2)
        val rect3 = findViewById<View>(R.id.rectangle3)
        val imgView = findViewById<ImageView>(R.id.logo)
        val midiTxtReader = MidiTxtReader(this)
        val animAlgorithm = AnimAlgorithm(midiTxtReader.midiMsgList)
        handler =Handler(Looper.getMainLooper())
        this@MainActivity.runOnUiThread(runnable {
            handler.postDelayed(this, 20)
            if(audioService!=null) {
                rect1.setBackgroundColor(animAlgorithm.getBackGrClr(audioService!!.curPos))
                rect2.setBackgroundColor(animAlgorithm.getBackGrClr(audioService!!.curPos))
                rect3.setBackgroundColor(animAlgorithm.getBackGrClr(audioService!!.curPos))
                var imgSize = if(mainLayout.width < mainLayout.height) mainLayout.width else mainLayout.height
                imgSize = (imgSize * animAlgorithm.getAnimFactor(audioService!!.curPos)).toInt()
                //Log.e("Before", imgView.layoutParams.width.toString())
                imgView.layoutParams.width = imgSize
                imgView.layoutParams.height = imgSize
                imgView.requestLayout()
                //Log.e("After", imgView.layoutParams.width.toString())
                //makeTxt(imgSize.toString())
            }
        })
    }

    var cnt = 0
    fun makeTxt(str: String){
        cnt++
        if(cnt%100==0) Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    private fun buttonPressed(audioManager: AudioServiceManager) {
        val btn = findViewById<Button>(R.id.btn)
        if(!audioManager.isMyServiceRunning(AudioService::class.java)){
            btn.text = getString(R.string.started)
            val intent = Intent(this, AudioService::class.java)
            applicationContext.bindService(intent, this, Context.BIND_AUTO_CREATE)
            startService(intent)
        } else{
            btn.text = getString(R.string.stopped)
            val intent = Intent(this, AudioService::class.java)
            applicationContext.unbindService(this)
            stopService(intent)
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.e("Main Activity", "OnServiceConnected called")
        val myBinder = service as AudioService.MyBinder
        audioService = myBinder.service
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        audioService = null
    }

    private fun runnable(body: Runnable.(Runnable)->Unit) = object : Runnable {
        override fun run() {
            this.body(this)
        }
    }
}