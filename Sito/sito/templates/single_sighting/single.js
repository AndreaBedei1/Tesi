const fileint = "../templates/single_sighting/single_api.php";
$(document).ready(function() {
    var id = document.getElementById("idcod").value;
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
    .done(function(data,success,response) {
        console.log(data);
        var dati = data[0];
        createMap(dati);
        $("#utente").val(dati["Cognome"]+" "+dati["Nome"]);
        $("#data").val(dati["Data"]);
        // Mettere a posto la sottospecie.
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
    