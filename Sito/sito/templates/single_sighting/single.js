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
            <form id="frmAddImg" action="#" method="post">
                <div class="form-group">
                    <div class="">
                        <label for="file">Carica Immagine:</label><br/>
                        <input type="file" class="form-control" id="file" accept="image/*"/>
                    </div>
                </div>
            </form>
        `;
        document.querySelector("#info .modal-title").innerText="Carica";
        document.querySelector("#info .modal-footer").innerHTML=`
            <button id="addImags" type="button" class="btn btn-primary">Carica</button>
        `;
        
        document.querySelectorAll("#info button")[1].addEventListener("click",function() {
            addAlert("alert","alert-warning","Caricamento immagine in corso...","");
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

    $("#btn_Rico").click(function() {
        document.querySelector("#info .modal-title").innerText="Riconoscimento esemplari";
        document.querySelector("#info .modal-footer").innerHTML=`
            <button type="button" class="btn btn-primary">OK</button>`;
        document.querySelector("#info .modal-body").innerHTML=`
            <div id="loading" role="status" aria-live="polite">
                Caricamento in corso
            </div>
        `;
        let dots = 0;
        setInterval(function() {
            dots = (dots + 1) % 4;
            $("#loading").text("Caricamento in corso" + ".".repeat(dots));
        }, 1000);

        document.querySelectorAll("#info button")[1].addEventListener("click",function() {
            $('#info').modal('toggle');
        });
        $('#info').modal('toggle');
        const datas = new FormData();
        datas.append("id", id);
        datas.append("request", "recognition");
        $.ajax({
            method: "POST",
            url: fileint,
            data:  datas,
            processData: false,
            contentType: false
        })
        .done(function(data,success,response) {
            if(data.state){
                document.querySelector("#info .modal-body").innerHTML="<p>Dalle immagini, la visione artificiale ha riscontratto che si potrebbe trattare di: ";
                let ris = removeDuplicates(data.data);
                ris = ris.filter(str => str.trim() !== "");
                ris.forEach(e => {
                    document.querySelector("#info .modal-body").innerHTML+="<p>"+e+"</p>";
                });
            } else {
                document.querySelector("#info .modal-body").innerHTML="<p>Non ci sono immagini relative all'avvistamento, caricarne almeno una.</p>";
            }
        })
        .fail(function(response) {
            console.log(response);
        });
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

    $("#infoRic").click(function() {
        modalInfo();
        document.querySelector("#info .modal-body").innerHTML=`
            <p>
            Il sistema utilizza un algoritmo di riconoscimento di visione artificiale.<br/> 
            Attraverso tutte le immagini caricate per un avvistamento il sistema consiglia la specie dell'esemplare che più si avvicina.<br/>
            Per una maggiore precisione si consiglia di aggiungere gli esemplari ritagliandoli dalle foto nella sezione sottostante, altrimenti il sistema potrebbe non avere un alto grado di precisione. 
            </p>
        `;
        $('#info').modal('toggle');

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
            if(document.querySelector(".carousel-item.active canvas")!==null){
                selected = document.querySelector(".carousel-item.active canvas").getAttribute("id");
                setImages(window.innerWidth, window.innerHeight);
            }
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
    w = Math.round(w);
    h = Math.round(h);

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
            $(".rSotto").show();
            document.querySelector(".card").removeAttribute("style");
            document.getElementById("divDel").innerHTML=`
                <div class="col-3 col-md-5"></diV> 
                <div class="col-6 col-md-2">                
                    <button id="deleteImg" class="btn btn-danger" type="button" aria-label="Elimina immagine"><span class="fas fa-trash"></span> Immagine</button>
                </div>
                <div class="col-3 col-md-5"></diV>
            `;
            document.getElementById("deleteImg").addEventListener("click", function(){
                deleteImmg();
            });
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
            if(data.length==1){
                $('.carousel-control-next').prop('disabled', true);
                $('.carousel-control-prev').prop('disabled', true);
                $('.carousel-control-next').hide();
                $('.carousel-control-prev').hide();
            }
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
                                ctx.fillText("ID:"+e.ID,tx+3,ty+14);
                            });
                        }
                    })
                    .fail(function(response) {
                        console.log(response);
                    });
                };
            }
            if(selected!=""){
                document.getElementById(selected).parentNode.classList.add("active");
            } else {
                document.querySelector(".carousel-item").classList.add("active");
            }
            document.querySelector("#sighting").innerHTML=`
                <header class="d-flex justify-content-between">
                    <h2>Esemplari</h2>
                    <button id="addNewInd" class="btn btn-success"aria-label="Aggiungi esemplare"><span class="fas fa-plus"></span> Esemplare</button>
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
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Annulla</button>
                    <button type="button" class="btn btn-success" data-dismiss="modal">Aggiungi</button>
                    `;

                document.querySelectorAll("#info button")[1].addEventListener("click",function() {
                    $('#info').modal('toggle');
                });
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

                canvas.addEventListener("touchstart", function (event) {
                    isDrawing = true;
                    let rect = canvas.getBoundingClientRect();
                    startX = event.touches[0].clientX - rect.left;
                    startY = event.touches[0].clientY - rect.top;
                }, { passive: true });
                
                canvas.addEventListener("touchend", function (event) {
                    isDrawing = false;
                    let rect = canvas.getBoundingClientRect();
                    endX = event.changedTouches[0].clientX - rect.left;
                    endY = event.changedTouches[0].clientY - rect.top;
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    ctx.drawImage(canvas2, 0, 0);
                    ctx.beginPath();
                    ctx.rect(startX, startY, endX - startX, endY - startY);
                    ctx.stroke();
                }, { passive: true });
                
                canvas.addEventListener("touchmove", function (event) {
                    if (!isDrawing)
                        return;
                    let rect = canvas.getBoundingClientRect();
                    endX = event.touches[0].clientX - rect.left;
                    endY = event.touches[0].clientY - rect.top;
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    ctx.drawImage(canvas2, 0, 0);
                    ctx.beginPath();
                    ctx.rect(startX, startY, endX - startX, endY - startY);
                    ctx.stroke();
                }, { passive: true });

                document.querySelectorAll("#info button")[2].addEventListener("click",function() {
                    const datas = new FormData();
                    const c = document.querySelector(".carousel-item.active canvas");
                    const id = c.getAttribute("id").split("_")[1];
                    datas.append("id", id);
                    datas.append("esemID", "0");
                    if(startY>endY && startX>endX){
                        datas.append("bx", startX/c.offsetWidth);
                        datas.append("by", startY/c.offsetHeight);
                        datas.append("tx", endX/c.offsetWidth);
                        datas.append("ty", endY/c.offsetHeight);
                    } else if(startY<endY && startX>endX){
                        datas.append("bx", startX/c.offsetWidth);
                        datas.append("by", endY/c.offsetHeight);
                        datas.append("tx", endX/c.offsetWidth);
                        datas.append("ty", startY/c.offsetHeight);
                    } else if(startY>endY && startX<endX){
                        datas.append("bx", endX/c.offsetWidth);
                        datas.append("by", startY/c.offsetHeight);
                        datas.append("tx", startX/c.offsetWidth);
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
            $(".rSotto").hide();
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
        const active = document.querySelector(".carousel-item.active canvas");
        let id = active.getAttribute("id").split("_")[1];
        let dati = data.sImmagini;
        const div = document.getElementById("listInd");
        div.innerHTML="";
        const cont = document.createElement("div");
        cont.setAttribute("class", "container");
        const row = document.createElement("div");
        row.setAttribute("class", "row");
        cont.appendChild(row);
        div.appendChild(cont);
        if(dati.length>0){
            dati.forEach(element => {
                let riga = `
                    <div class="col-sm-12">
                        <div class="card indiv my-2 border-secondary">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-6">
                                        <h3 class="card-title">ID: ${element.ID}</h3>
                                    </div>
                                    <div class="col-6 text-end px-2">
                                        <button class="btn btn-primary modifyIndv btn-sm" data-id="${element.ID}" data-img="${id}" data-nome="${element.nome}" data-indiv="${element.Esemp_ID}" aria-label="Modifica Esemplare"><span class="fas fa-edit"></span></button>
                                        <button class="btn btn-danger deleteIndv btn-sm" type="button" data-id="${element.ID}" data-img="${id}" aria-label="Elimina Esemplare"><span class="fas fa-trash"></span></button>
                                    </div>
                                </div>
                            <div class="row mx-4 my-2 border-bottom border-secondary"></div>
                            <dl class="row mb-1">
                                <dt class="col-sm-4">Nome:</dt>
                                <dd class="col-sm-8">${element.nome}</dd>
                            </dl>   
                `;
                riga += `
                    <div class="row">
                        <div class="col-6 p-0 px-2 m-0">
                            <h3 class="card-subtitle mb-3 mt-3 text-muted">Ferite:</h3>
                        </div>
                        <div class="col-6 text-end px-2 align-self-center">
                            <button class="btn btn-success btn-sm addInjury" data-img="${element.Immag_ID}" data-simg="${element.ID}" type="button" aria-label="Aggiungi ferita"><span class="fas fa-plus"></span> Ferita</button>  
                        </div>
                    </div>
                `;
                if (element.ferite.length > 0) {
                    element.ferite.forEach(el2 => {
                    riga += `
                        <div class="mx-2 my-2 border border-secondary rounded">
                            <header class="text-end mt-1 px-2">
                                <button class="btn btn-primary btn-sm btnModificaFerita" data-id="${el2.ID_Fer}" aria-label="Modifica"><span class="fas fa-edit"></span></button>
                                <button class="btn btn-danger btn-sm btnEliminaFerita" data-id="${el2.ID_Fer}" aria-label="Elimina"><span class="fas fa-trash"></span></button>
                            </header>
                            <dl class="row mx-1">
                                <dt class="col-sm-4">Posizione:</dt>
                                <dd class="col-sm-8">${el2.Posizione}</dd>
                                <dt class="col-sm-4">Gravità:</dt>
                                <dd class="col-sm-8">${el2.Gravi_Nome}</dd>
                                <dt class="col-sm-4">Descrizione:</dt>
                                <dd class="col-sm-8">${el2.Descrizione_Ferita}</dd>
                            </dl>                  
                        </div>
                    `;
                    });
                } else {
                    riga +="<p>Nessuna ferita presente.</p>"
                }
                riga += `
                        </div>
                    </div>
                </div>
                `;
                row.innerHTML += riga;
            });

            $(".addInjury").click(function() {
                addInjury($(this));
            });

            $(".btnModificaFerita").click(function() {
                modifyInjured($(this));
            });

            $(".btnEliminaFerita").click(function() {
                const id = $(this).data("id");
                document.querySelector("#info .modal-body").innerHTML="<p>Sei sicuro di volere eliminare la ferita?</p>";
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
                    datas.append("request", "deleteFerita");
                    $.ajax({
                        method: "POST",
                        url: fileint,
                        data:  datas,
                        processData: false,
                        contentType: false
                    })
                    .done(function(dati,success,response) {
                        setImages(window.innerWidth, window.innerHeight);
                        $('#info').modal('toggle'); 
                    })
                    .fail(function(response) {
                        console.log(response);
                    });
                });
                $('#info').modal('toggle');
            });

            $(".deleteIndv").click(function() {
                delateIndv($(this));       
            });

            $(".modifyIndv").click(function() {
                modifyIndv($(this));
            });
        } else {
            div.innerHTML="<p>Nessun esemplare presente.</p>";
        }
    })
    .fail(function(response) {
        console.log("Errore:"+response);
    });
}

function modifyIndv(btn){
    const id = btn.data("id");
    const img = btn.data("img");
    const nome = btn.data("nome");
    const indv = btn.data("indiv");
    document.querySelector("#info .modal-body").innerHTML=`
        <div class="w-100 px-2 py-1">
            <div class="row mb-1">
                <div class="col-12">
                    <p>Nome corrente: <strong>${nome}</strong></p>
                </div>
            </div>
            <form id="frmModifyEsempl" class="form-group" action="#" method="post">
                <div id="newElem">
                    <label for="newName">Immettere il nome del nuovo esemplare: </label>
                    <input type="text" class="form-control" id="newName" name="nome"/>
                </div>
                <div id="oldElem">
                    <label for="slcEsempl">Scegliere l'esemplare: </label>
                    <select id="slcEsempl" class="form-select" name="nomeSlc">
                    </select>
                </div>
            </form>
            <div class="text-center">
                <button id="switch" type="button" class="btn btn-secondary mx-1 my-2">Nuovo Esemplare</button>
            </div>
        </div>
    `;
    $("#newElem").hide();
    $("#switch").click(function() {
        $("#newElem").toggle();
        $("#oldElem").toggle();
        if(this.innerText=="Nuovo Esemplare")
            this.innerText="Esemplare Riconosciuto";
        else
            this.innerText="Nuovo Esemplare";
    });
    select_file(fileint, "getEsem", '', "slcEsempl", indv, "");

    document.querySelector("#info .modal-title").innerText="Modifica";
    document.querySelector("#info .modal-footer").innerHTML=`
        <button id="slvInd" type="button" class="btn btn-primary" data-dismiss="modal">Salva</button>`;
    
    document.querySelector("#slvInd").addEventListener("click",function() {
        const datas = getFormData("frmModifyEsempl");
        if($("#switch").text() != "Esemplare Riconosciuto"){
            datas.append("id", id);
            datas.append("img", img);
            datas.append("request", "updateEsempl");
        } else {
            datas.append("id", id);
            datas.append("img", img);
            datas.append("request", "createEsempl");
        }

        $.ajax({
            method: "POST",
            url: fileint,
            data:  datas,
            processData: false,
            contentType: false
        })
        .done(function(dati,success,response) {
            if(dati){
                setImages(window.innerWidth, window.innerHeight);
                $('#info').modal('toggle'); 
            }
        })
        .fail(function(response) {
            console.log(response);
        });
    });
    $('#info').modal('toggle');
}

function delateIndv(btn){
    const id = btn.data("id");
    const img = btn.data("img");
    document.querySelector("#info .modal-body").innerHTML="<p>Sei sicuro di volere eliminare l'elemento?</p>";
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
        datas.append("img", img);
        datas.append("request", "delateID");
        $.ajax({
            method: "POST",
            url: fileint,
            data:  datas,
            processData: false,
            contentType: false
        })
        .done(function(dati,success,response) {
            setImages(window.innerWidth, window.innerHeight);
            $('#info').modal('toggle'); 
        })
        .fail(function(response) {
            console.log(response);
        });
    });
    $('#info').modal('toggle');
}

function modalInfo(){
    document.querySelector("#info .modal-title").innerText="Informazioni";
    document.querySelector("#info .modal-footer").innerHTML=`
        <button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>`;
        
    document.querySelectorAll("#info button")[1].addEventListener("click",function() {
        $('#info').modal('toggle');
    });
}

function deleteImmg(){
    document.querySelector("#info .modal-body").innerHTML="<p>Sei sicuro di volere eliminare l'immagine e tutto ciò a cui essa è associata?</p>";
    document.querySelector("#info .modal-title").innerText="Eliminazione";
    document.querySelector("#info .modal-footer").innerHTML=`
        <button type="button" class="btn btn-danger" data-dismiss="modal">Sì</button>
        <button type="button" class="btn btn-primary" data-dismiss="modal">No</button>`;

    document.querySelectorAll("#info button")[2].addEventListener("click",function() {
        $('#info').modal('toggle');
    });
    
    document.querySelectorAll("#info button")[1].addEventListener("click",function() {
        const active = document.querySelector(".carousel-item.active canvas");
        let id = active.getAttribute("id").split("_")[1];
        const datas = new FormData();
        datas.append("id", id);
        datas.append("request", "deleteImmg");
        $.ajax({
            method: "POST",
            url: fileint,
            data:  datas,
            processData: false,
            contentType: false
        })
        .done(function(dati,success,response) {
            setImages(window.innerWidth, window.innerHeight);
            $('#info').modal('toggle'); 
        })
        .fail(function(response) {
            console.log(response);
        });
    });
    $('#info').modal('toggle');
}

function modifyInjured(btn){
    const id=btn.data("id");
    const datas = new FormData();
    datas.append("id", id);
    datas.append("request", "getInjury");
    $.ajax({
        method: "POST",
        url: fileint,
        data:  datas,
        processData: false,
        contentType: false
    })
    .done(function(data,success,response) {
        const dati = data[0];
        document.querySelector("#info .modal-body").innerHTML=`
        <div class="w-100 px-2 py-1">
            <form id="frmModifyIn" action="#" method="post">
                <div class="row form-group mb-1">
                    <div class="col-12">
                        <label class="form-label mb-1" for="posizione">Posizione: </label>
                        <input id="posizione" class="form-control" type="text" name="posizione" maxlength="40" value="${dati.Posizione}"/>
                    </div>
                </div>
                <div class="row form-group mb-1">
                    <div class="col-12">
                        <label class="form-label mb-1" for="slcGravita">Gravità: </label>
                        <select name="gravita" id="slcGravita" class="form-select"></select>
                    </div>
                </div>
                <div class="row form-group mb-1">
                    <div class="col-12">
                        <label class="form-label mb-1" for="descrizione">Descrizione: </label>
                        <textarea name="descrizione" id="descrizione" class="form-control">${dati.Descrizione_Ferita}</textarea>
                    </div>
                </div>
            </form>
        </div>
        `;
        select_file(fileint, "getGravita", '', "slcGravita", dati.Gravi_Nome, "");
        document.querySelector("#info .modal-title").innerText="Modifica Ferita";
        document.querySelector("#info .modal-footer").innerHTML=`
            <button type="button" class="btn btn-success" data-dismiss="modal">Modifica</button>`;
        document.querySelectorAll("#info button")[1].addEventListener("click",function() {
            const datas = getFormData("frmModifyIn");
            datas.append("id", id);
            datas.append("request", "updateInjury");
            $.ajax({
                method: "POST",
                url: fileint,
                data:  datas,
                processData: false,
                contentType: false
            })
            .done(function(dati,success,response) {
                setImages(window.innerWidth, window.innerHeight);
                $('#info').modal('toggle'); 
            })
            .fail(function(response) {
                console.log(response);
            });
        });
        $('#info').modal('toggle');
    })
    .fail(function(response) {
        console.log(response);
    });
}

function addInjury(btn){
    const img=btn.data("img");
    const sottImg=btn.data("simg");
    document.querySelector("#info .modal-body").innerHTML=`
            <div class="w-100 px-2 py-1">
                <form id="frmAddIn" action="#" method="post">
                    <div class="row form-group mb-1">
                        <div class="col-12">
                            <label class="form-label mb-1" for="posizione">Posizione: </label>
                            <input id="posizione" class="form-control" type="text" name="posizione"/>
                        </div>
                    </div>
                    <div class="row form-group mb-1">
                        <div class="col-6">
                            <label class="form-label mb-1" for="slcGravita">Gravità: </label>
                            <select name="gravita" id="slcGravita" class="form-select"></select>
                        </div>
                    </div>
                    <div class="row form-group mb-1">
                        <div class="col-12">
                            <label class="form-label mb-1" for="descrizione">Descrizione: </label>
                            <textarea name="descrizione" id="descrizione" class="form-control"></textarea>
                        </div>
                    </div>
                </form>
            </div>
        `;
        select_file(fileint, "getGravita", '', "slcGravita", "", "");
        document.querySelector("#info .modal-title").innerText="Aggiungi Ferita";
        document.querySelector("#info .modal-footer").innerHTML=`
            <button type="button" class="btn btn-success" data-dismiss="modal">Inserisci</button>`;
        document.querySelectorAll("#info button")[1].addEventListener("click",function() {
            const datas = getFormData("frmAddIn");
            datas.append("img", img);
            datas.append("sottImg", sottImg);
            datas.append("request", "addInjury");
            $.ajax({
                method: "POST",
                url: fileint,
                data:  datas,
                processData: false,
                contentType: false
            })
            .done(function(dati,success,response) {
                setImages(window.innerWidth, window.innerHeight);
                $('#info').modal('toggle'); 
            })
            .fail(function(response) {
                console.log(response);
            });
        });
        $('#info').modal('toggle');
}

function removeDuplicates(arr) {
    return arr.filter((item, index) => arr.indexOf(item) === index);
}

