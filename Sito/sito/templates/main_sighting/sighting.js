const fileint = "../templates/main_sighting/sighting_api.php";


$(document).ready(function() {
    select_file(fileint, "slcSpecie", "", "slcSpecie", "", 1);
    createTableMap();
    
    document.querySelectorAll("#add button")[0].addEventListener("click",function() {
        $('#add').modal('toggle');
    });
    
    document.querySelectorAll("#add button")[2].addEventListener("click",function() {
        $('#add').modal('toggle');
        $("#frmIns")[0].reset();
    });
    
    document.querySelectorAll("#add button")[1].addEventListener("click",function() {
        const datas = getFormData("frmIns")
        if(datas.get("data")!=="" && datas.has("esemplari")!=="" && datas.has("latitudine")!=="" && datas.has("longitudine")!=="" ){
            if(!datas.has("sottospecie"))
            datas.append("sottospecie", "");
            datas.append("request", "saveAvv");
            $.ajax({
                method: "POST",
                url: fileint,
                data:  datas,
                processData: false,
                contentType: false
            })
            .done(function(data,success,response) {
                if(data){
                    addAlert("alert","alert-success","Inseriemnto avvenuto con successo!","x");
                    createTableMap();
                    $("#frmIns")[0].reset();
                }else{
                    addAlert("alert","alert-danger","Errore inserimento dati!","x");
                }
                $('#info').modal('toggle');
            })
            .fail(function(response) {
                console.log(response);
            });
        } else {
            addAlert("alert","alert-danger","Inserire i campi obbligatori!","x");
        }
    });
    
    $("#aggiungi").click(function() {
        $('#add').modal('toggle');
    });
    
    $( "#slcSpecie" ).change(function() {
        var sp = $("#slcSpecie").val();
        if(sp==""){
            $("#slcSottospecie").prop('disabled', true);
        }else{
            $("#slcSottospecie").prop('disabled', false);
        }
        select_file(fileint, "slcSottospecie", {specie:sp}, "slcSottospecie", "", 1);
    });
});

function createMap(data)
{
    let mapOptions = {
        center: [averageCoord(data, 'Latid'), averageCoord(data, 'Long')],
        zoom: 12
    }

    document.getElementById("map").innerHTML='<div id="my-map"></div>';
    let map =  L.map("my-map", mapOptions);

    for(let i=0; i<data.length; i++)
    {
        if(data[i]['Anima_Nome']==null){
            data[i]['Anima_Nome']="?";
        }
        if(data[i]['Specie_Nome']==null){
            data[i]['Specie_Nome']="?";
        }
        let marker = L.marker([data[i]['Latid'], data[i]['Long']]).addTo(map);
        marker.bindPopup("Data: " + data[i]['Data']+"<br> Animale: " + data[i]['Anima_Nome']+"<br> Specie: " + data[i]['Specie_Nome']);
        marker.on('mouseover', function (e) {
            this.openPopup();
        });
        marker.on('mouseout', function (e) {
            this.closePopup();
        });
        marker.on('click', function (e) {
            document.location.href='single.php?id='+data[i]['ID'];
        });
    }
    layer = new L.TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
    map.addLayer(layer);
}

function averageCoord(arr, str){
    let sum = 0;
    arr.forEach(function(num) { sum += num[str] });
    let average = sum / arr.length;
    average = Math.round(average * 1000000) / 1000000;
    return average;
}

function createTableMap(){
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
            row += '<td header="visualizza" class="px-1"><button type="button" class="btn btn-primary btn-sm py-0 my-0 px-lg-4 px-1 mx-1" aria-label="Vedi avvistamento" data-id="'+data[i]['ID']+'"><i class="fas fa-search"></i></button></td>';
            row += '</tr>';
        }
        tbl_avvist.html(row);

        $("#tblAvvistamenti tbody button").click(function() {
            let btn = $(this);
            document.location.href='single.php?id='+btn.data("id");
        });
    })
    .fail(function(response) {
        console.log(response);
    });
}