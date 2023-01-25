const fileint = "../templates/main_sighting/sighting_api.php";

function createMap(data)
{
    // Calcolare la media latitudine e longitudine e fare le operazioni sia della data, sia del parse a lato server.
    let mapOptions = {
        center: [averageCoord(data, 'Latid'), averageCoord(data, 'Long')],
        zoom: 12
    }
    var map =  L.map("my-map", mapOptions);

    for(let i=0; i<data.length; i++)
    {
        let marker = L.marker([data[i]['Latid'], data[i]['Long']]).addTo(map);
        marker.bindPopup("Data: " + data[i]['Data']+"<br> Sogg: " + data[i]['Specie_Nome']);
        marker.on('mouseover', function (e) {
        this.openPopup();
        });
        marker.on('mouseout', function (e) {
        this.closePopup();
        });
        marker.on('click', function (e) {
            window.open('single.php?id='+data[i]['ID'], '_blank');
        });
    }
    var layer = new L.TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
    map.addLayer(layer);
}

function averageCoord(arr, str){
    let sum = 0;
    arr.forEach(function(num) { sum += num[str] });
    let average = sum / arr.length;
    average = Math.round(average * 1000000) / 1000000;
    return average;
}

$(document).ready(function() {
    const datas = new FormData();
    datas.append("request", "tbl_avvistamenti");
    $.ajax({
        method: "POST",
        url: fileint,
        data: datas,
        processData: false,
        contentType: false
    })
    .done(function(data,success,response) {
        tbl_avvist = $("#tblAvvistamenti tbody");
        createMap(data);
        row = "";
        for(let i = 0; i<data.length; i++)
        {
            row += '<tr class="rig">';
            row += '<td header="utente" class="px-1">'+data[i]['Nome']+" "+data[i]['Cognome']+'</td>';
            row += '<td header="data" class="px-1">'+data[i]['Data']+'</td>';
            row += '<td header="coordinate" class="px-1">'+data[i]['Latid']+' '+ data[i]['Long'] +'</td>';
            row += '<td header="animale" class="px-1">'+data[i]['Anima_Nome']+'</td>';
            row += '<td header="visualizza" class="px-1"><button type="button" class="btn btn-primary btn-sm py-0 my-0 px-lg-4 px-1 mx-1" data-id="'+data[i]['ID']+'">';
            row += '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">';
            row += '<path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"/>';
            row += '</svg>';
            row += '</button></td>';
            row += '</tr>';
        }
        tbl_avvist.html(row);
    })
    .fail(function(response) {
        console.log(response);
    });

    $("#tblAvvistamenti tbody").on('click','button',function() {
        let btn = $(this);
        window.open('single.php?id='+btn.data("id"), '_blank');
    });
});