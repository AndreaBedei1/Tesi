package com.example.seawatch

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class BackgroundService: Service() {
    private val INTERVAL = 60 * 1000L // 1 minute interval
    private var timer: Timer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startTimer()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        stopTimer()
        super.onDestroy()
    }

    private fun startTimer() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                // Call your function here
                checkForNewData()
            }
        }, 0, INTERVAL)
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    private fun checkForNewData() {
        // Implement your function here
    }
}