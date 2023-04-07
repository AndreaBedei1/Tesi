package com.example.seawatch

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "avvistamentiDaCaricare")
data class AvvistamentiDaCaricare(@PrimaryKey var id:Int,
                               var avvistatore:String,
                               var data:String,
                               var numeroEsemplari:Int,
                               var posizione:String,
                               var animale:String,
                               var specie:String,
                               var mare:Int,
                               var vento:Int,
                               var note:String,
                               var immagini:String)
