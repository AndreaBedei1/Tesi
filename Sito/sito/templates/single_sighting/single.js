const fileint = "../templates/single_sighting/single_api.php";
$(document).ready(function() {
    id = document.getElementById("idcod").value;
    setImages();
    uploadDates();
    
    document.querySelectorAll("#info button")[0].addEventListener("click",function() {
        $('#info').modal('toggle');
    });
    
    $( "#slcSpecie" ).change(function() {
        var sp = $("#slcSpecie").val();
        if(sp==""){
            $("#slcSottospecie").prop('disabled', true);
            $("#infoSpecie").prop('disabled', true);
        }else{
            $("#slcSottospecie").prop('disabled', false);
            $("#infoSpecie").prop('disabled', false);
        }
        select_file(fileint, "slcSottospecie", {specie:sp}, "slcSottospecie", "", 1);
    });
    
    $("#btn_visual").click(function() {
        document.querySelector("#info .modal-body").innerHTML=`
            <div id="alert" role="alert"></div>
            <form id="frmAddImg" action="" method="post">
                <div class="form-group">
                    <div class="">
                        <label for="file">Carica Immagine:</label><br/>
                        <input type="file" class="form-control" id="file"/>
                    </div>
                </div>
            </form>
        `;
        document.querySelector("#info .modal-title").innerText="Carica";
        document.querySelector("#info .modal-footer").innerHTML=`
            <button id="addImags" type="button" class="btn btn-primary">Carica</button>
        `;
        
        document.querySelectorAll("#info button")[1].addEventListener("click",function() {
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
        $('#info').modal('toggle');
    });
    
    $("#infoSpecie").click(function() {
        if($("#slcSottospecie").val()!=""){
            document.querySelector("#info .modal-title").innerText="Informazioni";
            document.querySelector("#info .modal-footer").innerHTML=`
                <button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>`;
                
            document.querySelectorAll("#info button")[1].addEventListener("click",function() {
                $('#info').modal('toggle');
            });
    
            const datas = new FormData();
            datas.append("animale", $("#slcSpecie").val());
            datas.append("specie", $("#slcSottospecie").val());
            datas.append("request", "infoSpecie");
            $.ajax({
                method: "POST",
                url: fileint,
                data:  datas,
                processData: false,
                contentType: false
            })
            .done(function(data,success,response) {
                console.log(data);
                const dati = data[0];
                if(data.length!=0){
                    document.querySelector("#info .modal-body").innerHTML=`
                        <dl>
                            <dt>Nomenclatura Binomiale</dt>
                            <dd>${dati.Nomenclatura_Binomiale}</dd>
                            <dt>Dimensione</dt>
                            <dd>${dati.Dimensione}</dd>
                            <dt>Descrizione</dt>
                            <dd>${dati.Descrizione}</dd>
                            <dt>Curiosità</dt>
                            <dd>${dati.Curiosita}</dd>
                        </dl>
                    `;
                } else {
                    document.querySelector("#info .modal-body").innerHTML="<p>Nessuna informazione aggiuntiva presente.</p>";
                }
                $('#info').modal('toggle');
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
            uploadDates();
        })
        .fail(function(response) {
            console.log(response);
        });
    });
});

function uploadDates(){
    const datas = new FormData();
    datas.append("id", id);
    datas.append("request", "getDates");
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
        select_file(fileint, "slcSpecie", "", "slcSpecie", dati["Anima_Nome"], 1);
        select_file(fileint, "slcSottospecie", {specie:dati["Anima_Nome"]}, "slcSottospecie", dati["Specie_Nome"], 1);
        console.log(dati["Anima_Nome"]);
        if(dati["Anima_Nome"]=="?"){
            $("#slcSottospecie").prop('disabled', true);
            $("#infoSpecie").prop('disabled', true);
        }
    })
    .fail(function(response) {
        console.log(response);
    });
}
    
function createMap(data)
{
    let mapOptions = {
        center: [data['Latid'], data['Long']],
        zoom: 12
    }
    document.getElementById("map").innerHTML='<div id="my-map"></div>';
    let map =  L.map("my-map", mapOptions);


    let marker = L.marker([data['Latid'], data['Long']]).addTo(map);
    if(data['Anima_Nome']==null){
        data['Anima_Nome']="?";
    }
    if(data['Specie_Nome']==null){
        data['Specie_Nome']="?";
    }
    marker.bindPopup("Data: " + data['Data']+"<br> Animale: " + data['Anima_Nome']+"<br> Specie: " + data['Specie_Nome']);
    marker.on('mouseover', function (e) {
        this.openPopup();
    });
    marker.on('mouseout', function (e) {
        this.closePopup();
    });
    marker.on('click', function (e) {
        this.openPopup();
    });

    layer = new L.TileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png");
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
            document.querySelector(".card").removeAttribute("style");
            document.getElementById("imgs").innerHTML=`
                <div id="carouselImages" class="carousel slide w-100 h-100" data-ride="carousel" data-type="multi" data-interval="false">   
                    <div id="imgCarous" class="carousel-inner w-100 h-100">
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
            const contImg = document.getElementById("imgCarous");
            for (let i = 0; i < data.length; i++) {
                const div = document.createElement("div");
                const canvas = document.createElement("canvas");
                canvas.setAttribute("width","400");
                canvas.setAttribute("height","350");
                canvas.setAttribute("id", "c"+data[i].ID);
                div.setAttribute("class", "carousel-item w-100 h-100");
                div.appendChild(canvas);
                contImg.appendChild(div);
                let ctx = canvas.getContext("2d");
                ctx.imageSmoothingEnabled = true;
                let img = new Image();
                img.src = "../../img/avvistamenti/"+data[i].Img;
                img.onload = function() {
                    ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
                    const datas = new FormData();
                    datas.append("id", data[i].ID);
                    datas.append("request", "getSottoimmagini");
                    $.ajax({
                        method: "POST",
                        url: fileint,
                        data:  datas,
                        processData: false,
                        contentType: false
                    })
                    .done(function(data,success,response) {
                        const c = document.querySelector(".carousel-item.active canvas");
                        if(data.dati.length>0){
                            data.dati.forEach(e => {
                                const tx = e.tl_x*c.offsetWidth;
                                const ty = e.tl_y*c.offsetHeight;
                                const bx = e.br_x*c.offsetWidth;
                                const by = e.br_y*c.offsetHeight;
                                ctx.strokeRect(tx, ty, (bx-tx), (by-ty));
                            });
                        }
                    })
                    .fail(function(response) {
                        console.log(response);
                    });
                };

                canvas.addEventListener("click", function(event) {
                    var x = event.offsetX / canvas.offsetWidth;
                    var y = event.offsetY / canvas.offsetHeight;
                    console.log(x, y);
                });
            }
            $("#addNewInd").click(function() {

            });
            document.querySelector(".carousel-item").classList.add("active");
            document.querySelector("#sighting").innerHTML=`
                <header class="d-flex justify-content-between">
                    <h2 >Individui</h2>
                    <button id="addNewInd" class="btn btn-success">Aggiungi</button>
                </header>
                <div id="listInd">
                    <p>Nessun individuo presente.</p>
                </div>
            `;
            setCreature();
        } else {
            document.getElementById("imgs").innerHTML=`
                <p class="text-center m-0 p-2">Nessuna immagine presente.</p>
            `;
            document.querySelector(".card").setAttribute("style", "height: 100% !important;");
        }
    })
    .fail(function(response) {
        console.log(response);
    });
}


function setCreature(){
    const active = document.querySelector(".carousel-item.active canvas");
    const id = active.getAttribute("id");
    const datas = new FormData();
    datas.append("id", id);
    datas.append("request","getSottoimmagini");
    $.ajax({
        type: "POST",
        url: fileint,
        data:  datas, 
        processData: false,
        contentType: false
    })
    .done(function(data,success,response) {
        $("#listInd").innerHTML="";
        data.dati.forEach(element => {
            $("#listInd").innerHTML+=`
                <div class="card">
                    <div class="card-body">
                        <form action="" method="post">
                            <label>ID<input type="text" name="id" value="${element.ID}" disabled /></label>
                            <label>Nome<input type="text" name="nome" value="${element.Nome}"/></label>
                        </form>
                    </div>
                </div>
            `;
        });
    })
    .fail(function(response) {
        console.log(response);
    });
}



