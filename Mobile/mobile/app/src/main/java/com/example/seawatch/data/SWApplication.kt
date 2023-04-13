package com.example.seawatch

import android.app.Application
import com.example.seawatch.data.FavouriteRepository
import com.example.seawatch.data.UserRepository
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
    val repository3 by lazy{
        UserRepository(database.userDAO())
    }
}