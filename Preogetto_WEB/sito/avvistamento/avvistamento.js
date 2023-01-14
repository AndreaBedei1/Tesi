function createMap(data)
{
    var mapOptions = {
        center: [data[0]['Coord_Lat'], data[0]['Coord_Lon']],
        zoom: 15
    }
    var map =  L.map("my-map", mapOptions);
    var marker = L.marker([data[0]['Coord_Lat'], data[0]['Coord_Lon']]).addTo(map);
    marker.bindPopup("Data: " + data[0]['Data']+"<br> Sogg: " + data[0]['Sottospecie_Nome']);
    var layer = new L.TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
    map.addLayer(layer);
}

function addSelect(file, request, filtri_data, id_select, valore, vuoto)
{
    if(filtri_data == "")
        var filtri_data = {};
    $.extend(filtri_data, {'request': request,'valore_selected': valore});
    $.ajax({
        type: "POST",
        url: file,
        data: filtri_data
    })
    .done(function(data, success, response) {
        var rows = '',
        dati = data;
        $("#" + id_select).html('');

        if(data.errore_ric != null && data.errore_ric != undefined )
        {
            alert(data.errore_ric);
        }
        else
        {
            if(vuoto == 1)
                $("#" + id_select).append('<option></option>');
            if(dati != '' && dati != null)
            {
                for(var i = 0; i < dati.length; i++)
                {
                    var dati_s = dati[i];

                    rows += '<option value="' + dati_s.cod_select + '"';
                    if(dati[i].attr_select != '' && dati[i].attr_select!= null && dati[i].attr_select!= undefined)
                        rows += dati[i].attr_select;

                    if(dati_s.cod_select == valore)
                        rows += ' selected';
                        rows += '>' + dati_s.descr_select +'</option>';
                    }
                    $("#" + id_select).append(rows);
                }
            }
        })
    .fail(function(response) {
        console.log(response);
    });
}

function images(id){
    $.ajax({
        method: "POST",
        url: "avvistamento_q.php",
        data:  {"id":id,  request:"images"}
    })
    .done(function(data,success,response) {
        console.log(data)
        $("#divimg").html('<img src="data:image/jpeg;base64,R0lGODlhDAAMAKIFAF5LAP/zxAAAANyuAP/gaP///wAAAAAAACH5BAEAAAUALAAAAAAMAAwAAAMlWLPcGjDKFYi9lxKBOaGcF35DhWHamZUW0K4mAbiwWtuf0uxFAgA7">');
    })
    .fail(function(response) {
        console.log(response);
    });
}

// SIstemare il fatto che in php gli elementi possono essere null e sostituire con ""

$(document).ready(function() {
    // Prendo l'id della specie.
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    var id = urlParams.get('id');
    
    $( "#slcSpecie" ).change(function() {
        var sp = $("#slcSpecie").val(); 
        addSelect("avvistamento_q.php", "slcSottospecie", {specie:sp}, "slcSottospecie", "", 1);
    });

    // COmpleto i campi.
    $.ajax({
        method: "POST",
        url: "avvistamento_q.php",
        data:  {"id":id,  request:"tbl_avvistamenti"}
    })
    .done(function(data,success,response) {
        console.log(data);
        createMap(data);
        var dati = data[0];
        $("#utente").val(dati["Cognome"]+" "+dati["Nome"]);
        $("#data").val(dati["Data"]);
        // Mettere a posto la specie.
        // Mettere a posto la sottospecie.
        $("#latitudine").val(dati["Coord_Lat"]);
        $("#longitudine").val(dati["Coord_Lon"]);
        $("#nEsemplari").val(dati["Numero_Esemplari"]);
        $("#vento").val(dati["Vento"]);
        $("#mare").val(dati["Mare"]);
        $("#note").val(dati["Note"]);
        addSelect("avvistamento_q.php", "slcSpecie", "", "slcSpecie", dati["Specie"], 1);
        addSelect("avvistamento_q.php", "slcSottospecie", {specie:dati["Specie"]}, "slcSottospecie", dati["Sottospecie"], 1);
        // images(id)
    })
    .fail(function(response) {
        console.log(response);
    });
});