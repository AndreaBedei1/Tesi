package com.example.seawatch.data

import java.util.Date

class Sighting(id:String,
               avvistatore:String,
               data:String,
               numeroEsemplari:String,
               posizione:String,
               animale:String,
               specie:String,
               mare:String,
               vento:String,
               note:String,
               immagine1:ByteArray? = null,
               immagine2:ByteArray? = null,
               immagine3:ByteArray? = null,
               immagine4:ByteArray? = null,
               immagine5:ByteArray? = null,
               caricato:Boolean = false) {
    val id = id
    val date=data
    val animal=animale
    val user=avvistatore
    val numberOfSamples=numeroEsemplari
    val position=posizione
    val specie=specie
    val sea=mare
    val wind=vento
    val notes=note
    var image1= immagine1
    var image2= immagine2
    var image3= immagine3
    var image4= immagine4
    var image5= immagine5
    var upload=caricato

    fun AddToDatabase(){

    }

    override fun toString(): String {
        return "Sighting(date='$date', animal='$animal', user='$user', numberOfSamples='$numberOfSamples', position='$position', specie='$specie', sea='$sea', wind='$wind', notes='$notes', image1=${image1?.contentToString()}, image2=${image2?.contentToString()}, image3=${image3?.contentToString()}, image4=${image4?.contentToString()}, image5=${image5?.contentToString()})"
    }
}