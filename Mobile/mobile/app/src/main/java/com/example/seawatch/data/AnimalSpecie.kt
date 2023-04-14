package com.example.seawatch.data

fun getAnimal(): List<String>{
    return listOf<String>("Altro", "Balena", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
}

fun getSpecieFromAniaml(animal : String): List<String>{
    if(animal == "Tonno"){
        return listOf<String>("Pippo", "Pippo", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
    } else if(animal == "Balena"){
        return listOf<String>("Pippo", "Pippo", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
    }else if(animal == "Delfino"){
        return listOf<String>("Pippo", "Pippo", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
    }else if(animal == "Foca"){
        return listOf<String>("Pippo", "Pippo", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
    }else if(animal == "Razza"){
        return listOf<String>("Pippo", "Pippo", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
    }else if(animal == "Squalo"){
        return listOf<String>("Pippo", "Pippo", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
    }else if(animal == "Tartaruga"){
        return listOf<String>("Pippo", "Pippo", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
    }else {
        return listOf<String>("Pippo", "Pippo", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
    }
}