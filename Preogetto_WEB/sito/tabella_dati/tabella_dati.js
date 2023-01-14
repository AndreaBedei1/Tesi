function createMap(data)
{
    // Calcolare la media latitudine e longitudine e fare le operazioni sia della data, sia del parse a lato server.
    var mapOptions = {
        center: [averageCoord(data, 'Coord_Lat'), averageCoord(data, 'Coord_Lon')],
        zoom: 15
    }
    var map =  L.map("my-map", mapOptions);

    for(var i=0; i<data.length; i++)
    {
        var marker = L.marker([data[i]['Coord_Lat'], data[i]['Coord_Lon']]).addTo(map);
        marker.bindPopup("Data: " + data[i]['Data']+"<br> Sogg: " + data[i]['Sottospecie_Nome']);
    }
    var layer = new L.TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
    map.addLayer(layer);
}

function averageCoord(arr, str){
    var sum = 0;
    arr.forEach(function(num) { sum += num[str] });
    var average = sum / arr.length;
    average = Math.round(average * 1000000) / 1000000;
    return average;
}

$(document).ready(function() {

    $.ajax({
        method: "POST",
        url: "tabella_dati_q.php",
        data:  {request:"tbl_avvistamenti"}
    })
    .done(function(data,success,response) {
        tbl_avvist = $("#tblAvvistamenti tbody");
        console.log(data);
        createMap(data);
        row = "";
        for(var i = 0; i<data.length; i++)
        {
            row += '<tr>';
            row += '<td header="utente">'+data[i]['Nome']+" "+data[i]['Cognome']+'</td>';
            row += '<td header="data">'+data[i]['Data']+'</td>';
            row += '<td header="latitudine">'+data[i]['Coord_Lat']+'</td>';
            row += '<td header="longitudine">'+data[i]['Coord_Lon']+'</td>';
            row += '<td header="soggetto">'+data[i]['Sottospecie_Nome']+'</td>';
            row += '<td header="visualizza"><button type="button" class="btn btn-primary" data-id="'+data[i]['ID']+'">Visualizza</button></td>';
            row += '</tr>';
        }
        tbl_avvist.html(row);
    })
    .fail(function(response) {
        console.log(response);
    });

    $("#tblAvvistamenti tbody").on('click','button',function() {
        var btn = $(this);
        btn.data("id")
        window.open('../avvistamento/avvistamento.php?id='+btn.data("id"), '_blank');
    });

});