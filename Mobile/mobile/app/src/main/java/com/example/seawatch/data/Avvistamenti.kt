package com.example.seawatch

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "avvistamentiDaCaricare")
data class AvvistamentiDaCaricare(@PrimaryKey var id:String,
                                var avvistatore:String,
                                var data:String,
                                var numeroEsemplari:String,
                                var posizione:String,
                                var animale:String,
                                var specie:String,
                                var mare:String,
                                var vento:String,
                                var note:String,
                                var immagine1:ByteArray? = null,
                                var immagine2:ByteArray? = null,
                                var immagine3:ByteArray? = null,
                                var immagine4:ByteArray? = null,
                                var immagine5:ByteArray? = null,
                                var caricato:Boolean = false
                               ) {
}
