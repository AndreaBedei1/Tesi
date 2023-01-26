const fileint = "../templates/single_sighting/single_api.php";
$(document).ready(function() {
    id = document.getElementById("idcod").value;
    const datas = new FormData();
    datas.append("id", id);
    datas.append("request", "tbl_avvistamenti");
    $.ajax({
        method: "POST",
        url: fileint,
        data:  datas,
        processData: false,
        contentType: false
    })
    .done(function(dati,success,response) {
        createMap(dati);
        $("#utente").val(dati["Cognome"]+" "+dati["Nome"]);
        $("#data").val(dati["Data"]);
        $("#latitudine").val(dati["Latid"]);
        $("#longitudine").val(dati["Long"]);
        $("#nEsemplari").val(dati["Numero_Esemplari"]);
        $("#vento").val(dati["Vento"]);
        $("#mare").val(dati["Mare"]);
        $("#note").val(dati["Note"]);
        select_file(fileint, "slcSpecie", "", "slcSpecie", dati["Specie_Anima_Nome"], 1);
        select_file(fileint, "slcSottospecie", {specie:dati["Specie_Anima_Nome"]}, "slcSottospecie", dati["Specie_Nome"], 1);
    })
    .fail(function(response) {
        console.log(response);
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

    $("#btn_visual").click(function() {
        $('#modal').modal('toggle');
    });

    $("#delete").click(function() {
        const datas = new FormData();
        datas.append("id", id);
        datas.append("request", "delete");
        $.ajax({
            method: "POST",
            url: fileint,
            data:  datas,
            processData: false,
            contentType: false
        })
        .done(function(data,success,response) {
            if(data){
                document.location.href = "homepage.php";
            } else {
                addAlert("alert","alert-danger","Non è stato possibile eliminare l'avvistamento","x");
            }
        })
        .fail(function(response) {
            console.log(response);
        });
    });

    $("#save").click(function() {
        const datas = getFormData("avvDates")
        datas.append("id", id);
        if(!datas.has("sottospecie"))
            datas.append("sottospecie", "");
        datas.append("request", "saveDates");
        $.ajax({
            method: "POST",
            url: fileint,
            data:  datas,
            processData: false,
            contentType: false
        })
        .done(function(data,success,response) {
            if(data){
                addAlert("alert","alert-success","Dati aggiornati correttamente.","x");
            }else{
                addAlert("alert","alert-danger","Non è stato possibile aggiornare i dati.","x");
            }
        })
        .fail(function(response) {
            console.log(response);
        });
    });
});
    
function createMap(data)
{
    var mapOptions = {
        center: [data['Latid'], data['Long']],
        zoom: 12
    }
    var map =  L.map("my-map", mapOptions);
    var marker = L.marker([data['Latid'], data['Long']]).addTo(map);
    marker.bindPopup("Data: " + data['Data']+"<br> Sogg: " + data['Specie_Nome']);
    var layer = new L.TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
    map.addLayer(layer);
}
    