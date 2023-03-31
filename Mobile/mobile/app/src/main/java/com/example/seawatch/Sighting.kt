package com.example.seawatch

import java.util.Date

class Sighting(date:Date, animal:String, user:User, numberOfSamples:Int, position:String, specie:String, sea:Int, wind:Int, notes:String, images:List<String>) {
    val date=date
    val animal=animal
    val user=user
    val numberOfSamples=numberOfSamples
    val position=position
    val specie=specie
    val sea=sea
    val wind=wind
    val notes=notes
    val images=images

    fun AddToDatabase(){

    }
}