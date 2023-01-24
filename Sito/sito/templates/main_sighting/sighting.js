const fileint = "../templates/main_sighting/sighting_api.php";

function createMap(data)
{
    // Calcolare la media latitudine e longitudine e fare le operazioni sia della data, sia del parse a lato server.
    let mapOptions = {
        center: [averageCoord(data, 'Latid'), averageCoord(data, 'Long')],
        zoom: 15
    }
    var map =  L.map("my-map", mapOptions);

    for(let i=0; i<data.length; i++)
    {
        let marker = L.marker([data[i]['Latid'], data[i]['Long']]).addTo(map);
        marker.bindPopup("Data: " + data[i]['Data']+"<br> Sogg: " + data[i]['Specie_Nome']);
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
            row += '<tr>';
            row += '<td header="utente">'+data[i]['Nome']+" "+data[i]['Cognome']+'</td>';
            row += '<td header="data">'+data[i]['Data']+'</td>';
            row += '<td header="coordinate" rowspan="2">'+data[i]['Latid']+' '+ data[i]['Long'] +'</td>';
            row += '<td header="animale">'+data[i]['Anima_Nome']+'</td>';
            row += '<td header="visualizza"><button type="button" class="btn btn-primary" data-id="'+data[i]['ID']+'">Visualizza</button></td>';
            row += '</tr>';
        }
        tbl_avvist.html(row);
    })
    .fail(function(response) {
        console.log(response);
    });

    $("#tblAvvistamenti tbody").on('click','button',function() {
        alert('da fare');
        // let btn = $(this);
        // btn.data("id")
        // window.open('../avvistamento/avvistamento.php?id='+btn.data("id"), '_blank');
    });
});