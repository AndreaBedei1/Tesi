package com.example.seawatch

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.seawatch.data.Favourite
import com.example.seawatch.data.FavouriteDAO
import com.example.seawatch.data.User
import com.example.seawatch.data.UserDAO

@Database(entities=[AvvistamentiDaCaricare::class, Favourite::class, User::class], version=5)
abstract class SWDatabase:RoomDatabase(){
    abstract fun avvistamentiDAO():AvvistamentiDAO
    abstract fun favouriteDAO(): FavouriteDAO
    abstract fun userDAO(): UserDAO
    companion object{
        @Volatile
        private var INSTANCE:SWDatabase?=null
        fun getDatabase(context: Context):SWDatabase{
            return INSTANCE?: synchronized(this){
                val instance= Room.databaseBuilder(context.applicationContext, SWDatabase::class.java,"SWDatabase").build()
                INSTANCE=instance
                instance
            }
        }
    }
}