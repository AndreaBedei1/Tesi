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
    
    document.querySelectorAll("#info button")[0].addEventListener("click",function() {
        $('#info').modal('toggle');
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
        setImages();
    });

    $(".close").click(function() {
        $('#modal').modal('toggle');
    });

    $("#addImags").click(function() {
        const file = $("#file")[0].files[0];
        if(file===undefined){
            addAlert("alert","alert-danger", "Immagine non selezionata","");
        }else{
            const datas = new FormData();
            datas.append("id", id);
            datas.append("file",file);
            datas.append("request","addImage");
            $.ajax({
                type: "POST",
                url: fileint,
                data:  datas, 
                processData: false,
                contentType: false
            })
            .done(function(data,success,response) {
                if(data["state"]===false){
                    addAlert("alert","alert-danger",data["msg"],"");
                } else {
                    addAlert("alert","alert-success","Immagine inserita!","x");
                    $("#frmAddImg")[0].reset();
                    setImages();
                }
            })
            .fail(function(response) {
                console.log(response);
            });
        }
    });

    $("#delete").click(function() {
        document.querySelector("#info .modal-body").innerHTML="<p>Sei sicuro di volere eliminare l'avvistamento?</p>";
        document.querySelector("#info .modal-title").innerText="Eliminazione";
        document.querySelector("#info .modal-footer").innerHTML=`
            <button type="button" class="btn btn-danger" data-dismiss="modal">Sì</button>
            <button type="button" class="btn btn-primary" data-dismiss="modal">No</button>`;

        document.querySelectorAll("#info button")[2].addEventListener("click",function() {
            $('#info').modal('toggle');
        });
        
        document.querySelectorAll("#info button")[1].addEventListener("click",function() {
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
                if(data)
                    document.location.href = "homepage.php";
            })
            .fail(function(response) {
                console.log(response);
            });
        });
        $('#info').modal('toggle');
    });

    $("#save").click(function() {
        document.querySelector("#info .modal-title").innerText="Salvataggio";
        document.querySelector("#info .modal-footer").innerHTML=`
            <button type="button" class="btn btn-primary">OK</button>`;

        document.querySelectorAll("#info button")[1].addEventListener("click",function() {
            $('#info').modal('toggle');
        });
        
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
                document.querySelector("#info .modal-body").innerHTML="<p>Dati aggiornati!</p>";
                
            }else{
                document.querySelector("#info .modal-body").innerHTML="<p>Errore nell'aggiornamento.</p>";
            }
            $('#info').modal('toggle');
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

function setImages(){
    const datas = new FormData();
    datas.append("id", id);
    datas.append("request","getImages");
    $.ajax({
        type: "POST",
        url: fileint,
        data:  datas, 
        processData: false,
        contentType: false
    })
    .done(function(data,success,response) {
        if(data.length>0){
            document.getElementById("imgs").innerHTML=`
                <div id="carouselImages" class="carousel slide" data-ride="carousel" data-type="multi" data-interval="false">   
                    <div id="imgCarous" class="carousel-inner">
                    </div>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselImages" data-bs-slide-to="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselImages" data-bs-slide-to="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                </div>
            `;
            document.getElementById("imgCarous").innerHTML=`
                <div class="carousel-item active">
                    <img src="../../img/avvistamenti/${data[0].Img}" class="d-block w-100" alt="Immagine dell'avvistamento" />
                </div>
            `;
            for (let i = 1; i < data.length; i++) {
                document.getElementById("imgCarous").innerHTML+=`
                    <div class="carousel-item">
                        <img src="../../img/avvistamenti/${data[i].Img}" class="d-block w-100" alt="Exotic Fruits" />
                    </div>
                `;
            }
        } else {
            document.getElementById("imgs").innerHTML="<p>Nessuna immagine presente.</p>";
        }
    })
    .fail(function(response) {
        console.log(response);
    });
}
    