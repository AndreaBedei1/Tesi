package com.example.seawatch

import android.app.Application
import com.example.seawatch.data.FavouriteRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SWApplication: Application() {
    private val database by lazy{
        SWDatabase.getDatabase(this)
    }
    val repository by lazy{
        AvvistamentiRepository(database.avvistamentiDAO())
    }

    val repository2 by lazy{
        FavouriteRepository(database.favouriteDAO())
    }

}