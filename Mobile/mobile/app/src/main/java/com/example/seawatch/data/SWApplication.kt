package com.example.seawatch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SWApplication: Application() {
    private val database by lazy{
        SWDatabase.getDatabase(this)
    }
    val repository by lazy{
        AvvistamentiRepository(database.avvistamentiDAO())
    }
}