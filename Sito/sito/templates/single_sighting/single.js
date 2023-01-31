const fileint = "../templates/single_sighting/single_api.php";
let selected = "";

$(document).ready(function() {
    id = document.getElementById("idcod").value;
    setImages(window.innerWidth, window.innerHeight);
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
                        setImages(window.innerWidth, window.innerHeight);
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
            modalInfo();
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

    let resizeTimer;
    window.addEventListener("resize", function(){
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function() {
            selected = document.querySelector(".carousel-item.active canvas").getAttribute("id");
            setImages(window.innerWidth, window.innerHeight);
        }, 250);
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

function setImages(w, h){
    if(w<347){
        w=w/1.6;
    }
    else if(w>=374 && w<=950){
        w=w/2;
    } else {
        w=w/3.5;
    }
    if(h<=800){
        h=h/2.5;
    } else {
        h=h/2.5;
    }
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
                    <button class="caros carousel-control-next" type="button" data-target="#carouselImages">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                    <button class="caros carousel-control-prev" type="button" data-target="#carouselImages">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                </div>
            `;
            const contImg = document.getElementById("imgCarous");
            for (let i = 0; i < data.length; i++) {
                const div = document.createElement("div");
                const canvas = document.createElement("canvas");
                canvas.setAttribute("width",w);
                canvas.setAttribute("height",h);
                canvas.setAttribute("id", "c_"+data[i].ID);
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
                        let dati = data.sImmagini;
                        const c = document.querySelector(".carousel-item.active canvas");
                        if(dati.length>0){
                            dati.forEach(e => {
                                const tx = e.tl_x*c.offsetWidth;
                                const ty = e.tl_y*c.offsetHeight;
                                const bx = e.br_x*c.offsetWidth;
                                const by = e.br_y*c.offsetHeight;
                                ctx.strokeRect(tx, ty, (bx-tx), (by-ty));
                                ctx.font = "14px Arial";
                                ctx.fillText("ID:"+e.Esemp_ID,tx+3,ty+14);
                            });
                        }
                    })
                    .fail(function(response) {
                        console.log(response);
                    });
                };
            }
            if(selected!=""){
                console.log(selected);
                document.getElementById(selected).parentNode.classList.add("active");
            } else {
                document.querySelector(".carousel-item").classList.add("active");
            }
            document.querySelector("#sighting").innerHTML=`
                <header class="d-flex justify-content-between">
                    <h2>Esemplari</h2>
                    <button id="addNewInd" class="btn btn-success">Aggiungi Esemplare</button>
                </header>
                <div id="listInd">
                </div>
            `;
            setCreature();
            $(".caros").click(function() {
                $('#carouselImages').carousel('pause');
            });
            $(".carousel-control-next").click(function() {
                $('#carouselImages').carousel('next');
            });
            $(".carousel-control-prev").click(function() {
                $('#carouselImages').carousel('prev');
            });
            $('#carouselImages').on('slid.bs.carousel', function () {
                setCreature();
            });

            $("#addNewInd").click(function() {
                const canvas = document.createElement("canvas");
                canvas.setAttribute("width",w);
                canvas.setAttribute("height",h);
                document.querySelector("#info .modal-body").innerHTML=`
                    <p>Seleziona la parte di immagine che rappresenta un esemplare</p>`;
                document.querySelector("#info .modal-body").classList.add("text-center");
                document.querySelector("#info .modal-body").appendChild(canvas);
                document.querySelector("#info .modal-title").innerText="Aggiunta";
                document.querySelector("#info .modal-footer").innerHTML=`
                    <button type="button" class="btn btn-success" data-dismiss="modal">Aggiungi</button>`;
                const canvas2 = document.querySelector(".carousel-item.active canvas");
                let ctx = canvas.getContext("2d");
                ctx.drawImage(canvas2, 0, 0);
                ctx.strokeStyle = "red";
                ctx.lineWidth = 2;
                var isDrawing = false;
                var startX, startY, endX, endY;
                canvas.addEventListener("mousedown", function (event) {
                    isDrawing = true;
                    startX = event.offsetX;
                    startY = event.offsetY;
                });
                  
                canvas.addEventListener("mouseup", function (event) {
                    isDrawing = false;
                    endX = event.offsetX;
                    endY = event.offsetY;
                    
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    ctx.drawImage(canvas2, 0, 0);
                    ctx.beginPath();
                    ctx.rect(startX, startY, endX - startX, endY - startY);
                    ctx.stroke();
                });
                  
                canvas.addEventListener("mousemove", function (event) {
                    if (!isDrawing) 
                        return;
                    endX = event.offsetX;
                    endY = event.offsetY;
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    ctx.drawImage(canvas2, 0, 0);
                    ctx.beginPath();
                    ctx.rect(startX, startY, endX - startX, endY - startY);
                    ctx.stroke();
                });

                document.querySelectorAll("#info button")[1].addEventListener("click",function() {
                    const datas = new FormData();
                    const c = document.querySelector(".carousel-item.active canvas");
                    const id = c.getAttribute("id").split("_")[1];
                    datas.append("id", id);
                    if(startY>endY){
                        datas.append("bx", startX/c.offsetWidth);
                        datas.append("by", startY/c.offsetHeight);
                        datas.append("tx", endX/c.offsetWidth);
                        datas.append("ty", endY/c.offsetHeight);
                    } else {
                        datas.append("tx", startX/c.offsetWidth);
                        datas.append("ty", startY/c.offsetHeight);
                        datas.append("bx", endX/c.offsetWidth);
                        datas.append("by", endY/c.offsetHeight);
                    }
                    datas.append("request", "addSottoimmagine");
                    $.ajax({
                        method: "POST",
                        url: fileint,
                        data:  datas,
                        processData: false,
                        contentType: false
                    })
                    .done(function(data,success,response) {
                        selected = document.querySelector(".carousel-item.active canvas").getAttribute("id");
                        setImages(window.innerWidth, window.innerHeight);
                        $('#info').modal('toggle');
                    })
                    .fail(function(response) {
                        console.log(response);
                    });
                });
                $('#info').modal('toggle');
            });
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
    let id = active.getAttribute("id");
    id=id.split("_")[1];
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
        let dati = data.sImmagini;
        console.log(data);
        const div = document.getElementById("listInd");
        if(dati.length>0){
            div.innerHTML=`
                <div class="container">
                    <div class="row">
            `;
            dati.forEach(element => {
                riga = `
                    <div class="my-2 fieldset">    
                        <form id="f_${element.Esemp_ID}" method="post">
                            <legend>ID: ${element.Esemp_ID}</legend>
                            <label class="mx-1 d-inline-flex">Nome<input class="input-group mx-1" type="text" name="nome" value="${element.nome}"/></label>
                `;
                if(element.ferite.length>0){
                    riga+="<h3>Ferite:</h3>";
                }
                element.ferite.forEach(el2 => {
                    riga += `
                        <dl>
                            <dt>Posizione:</dt>
                            <dd>${el2.Posizione}</dd>
                            <dt>Gravità:</dt>
                            <dd>${el2.Gravi_Nome}</dd>
                            <dt>Descrizione Ferita:</dt>
                            <dd>${el2.Descrizione_Ferita}</dd>
                        </dl>
                    `;
                    //     <div class="row">
                    //         <div class="col-6"
                    //             <label class="d-inline-flex">Posizione <input class="input-group" type="text" name="posizione" value="${el2.Posizione}"/></label>
                    //         </div>
                    //         <div class="col-6"
                    //             <label for="slcGrav">Gravità </label>
                    //             <select class="d-inline-flex" name="specie" id="slcGrav" class="form-select">
                    //                 <option></option>
                    //             </select>
                    //         </div>
                    //     </div>
                    //         <label class="d-inline-flex">Descrizione <textarea name="descrizioneF" class="input-group"></textarea>${el2.Gravi_Nome}</label>
                    // `;

                });
                riga +=`
                            <button class="btn btn-success btn-sm align-baseline salvaElem" type="button" data-id="${element.Esemp_ID}">Salva</button>
                        </form>
                    </div>
                `;
                div.innerHTML+=riga;
            });
            div.innerHTML+=`
                    </div>
                </div>
            `;

            $(".salvaElem").click(function() {
                const datas = getFormData("f_"+$(this).data("id"));
                datas.append("id", $(this).data("id"));
                datas.append("request", "updateIndv");
                $.ajax({
                    method: "POST",
                    url: fileint,
                    data:  datas,
                    processData: false,
                    contentType: false
                })
                .done(function(data,success,response) {
                    modalInfo();
                    if(data){
                        document.querySelector("#info .modal-body").innerText="Aggiornamentto avvenuto con successo!";
                    } else {
                        document.querySelector("#info .modal-body").innerText="Errore aggiornamento!";
                    }
                    $('#info').modal('toggle');
                })
                .fail(function(response) {
                    console.log(response);
                });
            });
        } else {
            div.innerHTML="<p>Nessun esemplare presente.</p>";
        }
    })
    .fail(function(response) {
        console.log(response);
    });
}

function modalInfo(){
    document.querySelector("#info .modal-title").innerText="Informazioni";
    document.querySelector("#info .modal-footer").innerHTML=`
        <button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>`;
        
    document.querySelectorAll("#info button")[1].addEventListener("click",function() {
        $('#info').modal('toggle');
    });
}



