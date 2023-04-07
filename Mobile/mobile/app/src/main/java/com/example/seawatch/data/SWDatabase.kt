package com.example.seawatch

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[AvvistamentiDaCaricare::class], version=1)
abstract class SWDatabase:RoomDatabase(){
    abstract fun avvistamentiDAO():AvvistamentiDAO
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