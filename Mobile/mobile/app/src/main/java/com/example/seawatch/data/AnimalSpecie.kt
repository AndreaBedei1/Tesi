package com.example.seawatch.data

fun getAnimal(): List<String>{
    return listOf<String>("","Altro", "Balena", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
}

fun getSpecieFromAniaml(animal : String): List<Speci>{
    if(animal == "Tonno"){
        return listOf<Speci>(
            Speci("",null)
        )
    } else if(animal == "Balena"){
        return listOf<Speci>(
            Speci("",null),
            Speci("Balenottera comune", "Balaenoptera physalus")
        )
    }else if(animal == "Delfino"){
        return listOf<Speci>(
            Speci("",null),
            Speci("Bianco atlantico",null),
            Speci("Cefalorinco di Commerson",null),
            Speci("Feresa",null),
            Speci("Globicefalo di Gray",null),
            Speci("Globicephala",null),
            Speci("Lagenodelfino",null),
            Speci("Peponocefalo",null),
            Speci( "Scuro",null),
            Speci("Stenella dal lungo rostro",null),
            Speci("Stenella maculata",null),
            Speci("Steno",null),
            Speci( "Comune","Delphinus delphis")
        )
    }else if(animal == "Foca"){
        return listOf<Speci>(
            Speci("",null),
            Speci("Comune",null)

        )
    }else if(animal == "Razza"){
        return listOf<Speci>(
            Speci("",null),
            Speci("Bavosa", "Dipturus batiscom")
        )
    }else if(animal == "Squalo"){
        return listOf<Speci>(
            Speci("",null),
            Speci("Volpe occhio grosso","Alopias superciliosus"),
            Speci("Volpe","Alopias vulpinus"),
            Speci("Grigio del genere Carcharhinus","Carcharhinus"),
            Speci("Seta", "Carcharhinus falciformis"),
            Speci("Bianco","Carcharodon carcharias"),
            Speci("Elefante", "Cetorhinus maximus")

        )
    }else if(animal == "Tartaruga"){
        return listOf<Speci>(
            Speci("",null),
            Speci("Comune", "Caretta caretta"),
            Speci("Verde","Chelonia mydas") ,
            Speci("Liuto", "Dermochelys coriacea")

        )
    }else {
        return listOf<Speci>()
    }
}