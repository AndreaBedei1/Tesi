const fileintPost = "templates/main_posts/posts_api.php";
const fileProfile = "profile.php";
function select_file(file, request, id_select, valore, vuoto)
{
    const formdata = new FormData();
    formdata.append("request",request);
    formdata.append("valore_selected",valore);

    $.ajax({
        type: "POST",
        url: file,
        data: formdata, 
        processData: false,
        contentType: false
    })
    .done(function(data, success, response) {
        let rows = '', dati = data;
        $("#" + id_select).html('');

        if(vuoto == 1)
            $("#" + id_select).append('<option></option>');
        if(dati != '' && dati != null)
        {
            for(let i = 0; i < dati.length; i++)
            {
                const dati_s = dati[i];

                rows += '<option value="' + dati_s.cod_select + '"';
                if(dati[i].attr_select != '' && dati[i].attr_select!= null && dati[i].attr_select!= undefined)
                    rows += dati[i].attr_select;

                if(dati_s.cod_select == valore)
                    rows += ' selected';
                rows += '>' + dati_s.descr_select +'</option>';
            }
            $("#" + id_select).append(rows);
        }
    })
    .fail(function(response) {
        return_fail(response);
    });
}


function getFormData(id_form)
{
    const formData = new FormData();
    
    if($("#" + id_form).is( "form" )==true)
    {
        let form = $("#" + id_form);
        let unindexed_array = form.serializeArray();
        unindexed_array.forEach(element => {
            formData.append(element.name, element.value);
        });
    }
    return formData;
}

function addAlert(id_append,classe,message,time_remove)
{
    
    let alert = $('<div class="alert '+classe+'">' + '<button type="button" class="close" data-dismiss="alert" onClick="$(this).parent().remove()">' +
    '&times;</button>' + message + '</div>');

    if(time_remove=='x')
        setTimeout(function () { alert.remove(); }, 5000);
    else if(time_remove!='' && time_remove!=undefined)
        setTimeout(function () { alert.remove(); }, time_remove);

    $('#'+id_append).html(alert);
}

// Sezione dei post
function processaLike(dati){
    let like;
    let dislike;
    let userPostReaction;
    if(dati["reactions"].length===0){
        like=0;
        dislike=0;
        userPostReaction=0;
    } 
    else{
        like=dati["reactions"][0]["NumLike"];
        dislike=dati["reactions"][0]["NumDislike"];
        userPostReaction=dati["post_reaction"];
    }
    return Array(like, dislike, userPostReaction);
}

function commentiPost(numero){
    const datas = getFormData("formPost");
    datas.append("request", "commentiPost");
    datas.append("idPost", numero);
    $.ajax({
        type: "POST",
        url: fileintPost,
        data:  datas, 
        processData: false,
        contentType: false
    })
    .done(function(data,success,response) {
        let row='';
        data.forEach(element => {
            row += `
                <div class="d-flex flex-start w-100 mb-2 border-bottom">
                    <a class="image-wrapper-small me-2" href="${fileProfile}?id=${element.Utente}">
                        <img class="rounded-circle shadow-1-strong" src="images/profile_img/${element["Immagine"]}" alt="Immagine profilo"/>
                    </a>
                    <div class="form-outline w-100">
                        <p class="text-muted small mb-0">${element.Data}</p>
                        <p class="mb-3 comLunghi">${element.Testo}</p>
                    </div>
                </div>
            `;
        });
        $("#contComment"+numero).html(row);
    })
    .fail(function(response) {
        console.log(response);
    });
}

function visualizzaPost(numeroPost, aggiuntaPost=false, utente="", checked=false){
    setAlertLoad();
    const datas = new FormData();
    datas.append("request", "getPosts");
    datas.append("numeroPost", numeroPost);
    datas.append("utente", utente);
    datas.append("checked", checked);
    $.ajax({
        type: "POST",
        url: fileintPost,
        data:  datas, 
        processData: false,
        contentType: false
    })
    .done(function(data,success,response) {
        const dati=data["posts"];
        postsLoaded+=data["posts"].length;
        let row = '';
        for(let i=0; i<dati.length; i++){
            const temp=processaLike(dati[i]);
            const like=temp[0];
            const dislike=temp[1];
            const postReaction = temp[2] == 1 ? " post-liked " : (temp[2] == -1 ? " post-disliked " : ""); 
            row += ` <article id="post${dati[i]["ID"]}" class="card my-2${postReaction}">
                        <div class="card-body"><div class="d-flex flex-start align-items-center">
                            <a class="image-wrapper me-2" href="${fileProfile}?id=${dati[i]["Utente"]}"><img class="rounded-circle shadow-1-strong" src="images/profile_img/${dati[i]["Immagine"]}" alt="avatar user" width="60" height="60" /></a>
                            <div>
                                <a href="${fileProfile}?id=${dati[i]["Utente"]}"><p class="fw-bold mb-1 text-left">${dati[i]["Utente"]}</p></a>
                                <p class="text-muted small mb-0">${dati[i]["Data"]}</p>
                            </div>
                        </div>
                        <div>
                            <h3 class="mt-3 mb-2 pb-2">${dati[i]["Titolo"]}</h3>
            `;
            if(dati[i]["Media"]!==""){
                row += `<div class="text-center">
                            <img src="images/post_img/${dati[i]["Media"]}" alt="Immagine del post intitolato:${dati[i]["Titolo"]}" class="img-responsive"/>
                        </div>`;
            }
            row += `<p class="mt-3 mb-2 pb-2">${dati[i]["Testo"]}</p>
                    <div class="small d-flex justify-content-start">
                        <button id="btnLike${dati[i]["ID"]}" class="d-flex align-items-center me-3 like-button btn btn-3${temp[2] == 1 ? " btn-inverted" : ""} btn-transition-up btn-sm" data-type="like" data-numero="${dati[i]["ID"]}" >Like: ${like}</button>
                        <button id="btnDislike${dati[i]["ID"]}" class="d-flex align-items-center me-3 dislike-button btn btn-4${temp[2] == -1 ? " btn-inverted" : ""} btn-transition-down btn-sm" data-type="dislike" data-numero="${dati[i]["ID"]}">Dislike: ${dislike}</button>
                        <button class="d-flex align-items-center me-3 btn btn-1 btn-sm" data-type="commento" data-numero="${dati[i]["ID"]}">Commenti: ${dati[i]["commenti"]}</button>
                    </div>
                </div>
            </div>
            <div class="card-footer py-3 border-0 collapse" id="divCommento${dati[i]["ID"]}">
                <div id="contComment${dati[i]["ID"]}"></div>
                <div class="d-flex flex-start w-100">
                    <form id="comS${dati[i]["ID"]}" class="form-outline w-100">
                        <label class="form-label" for="inpCommento${dati[i]["ID"]}">Commento: </label>
                        <input class="form-control" id="inpCommento${dati[i]["ID"]}" name="testo" placeholder="Inserisci il testo del commento"/>
                    </form>
                </div>
                <div class="float-end mt-2 pt-1">
                    <button type="button" class="btn btn-1 btn-inverted btn-background btn-sm" data-type="comS" data-numero="${dati[i]["ID"]}">Commenta</button>
                </div>
            </div>
        </article>
            `;                  
        }

        
        $("form").submit(function(event){
            event.preventDefault();
        });
        
        $("#alertC").html("");
        
        if(dati.length!=0){
            caricamento=false;
            if(aggiuntaPost){
                $("#contPosts").html(row);
            } else {
                $("#contPosts").append(row);
            }

        }
    })
    .fail(function(response) {
        console.log(response);
    });
}


function gestioneBottoni(button){
    const btn = button, type = btn.data('type'), numero = btn.data('numero');
    switch(type){
        case "commento":
            $("#divCommento"+numero).toggle();
            commentiPost(numero);
            break;
        case "comS":
            const datas = getFormData("comS"+numero);
            if(datas.get("testo")!=""){
                datas.append("request", "aggiungiCommento");
                datas.append("nPost", numero);
                $.ajax({
                    type: "POST",
                    url: fileintPost,
                    data:  datas, 
                    processData: false,
                    contentType: false
                })
                .done(function(data,success,response) {
                    const post = data.dati[0];
                    $("#inpCommento"+numero).val("");
                    commentiPost(numero);
                    addNotification("commento", post.Utente, post.Titolo, datas.get("testo"));
                    $('button[data-type="commento"]').text("Commenti: "+data.commenti);
                })
                .fail(function(response) {
                    console.log(response);
                });
            }
            break;
        case "like":
            const like = new FormData();
            like.append("request", "aggiungiReaction");
            like.append("nPost", numero);
            like.append("like", 1);
            like.append("dislike", 0);
            like.append("type", "Like");
            $.ajax({
                type: "POST",
                url: fileintPost,
                data:  like, 
                processData: false,
                contentType: false
            })
            .done(function(data,success,response) {
                const temp=processaLike(data);
                const like=temp[0];
                const dislike=temp[1];
                const postReaction = temp[2];
                
                const btnLike = $("#btnLike"+numero);
                const btnDislike = $("#btnDislike"+numero);
                
                btnLike.text("Like: "+like);
                btnDislike.text("Dislike: "+dislike);
                
                let article = $("#post"+numero);
                let prevReaction = article.hasClass("post-liked") ? 1 : (article.hasClass("post-disliked") ? -1 : 0);
                
                if (postReaction != prevReaction) {
                    if (prevReaction != 0) {
                        let prevReactionClass = prevReaction == 1 ? "post-liked" : "post-disliked";
                        article.removeClass(prevReactionClass);
                        btnLike.removeClass("btn-inverted");
                        btnDislike.removeClass("btn-inverted"); // Easier to try and remove from both buttons than checking which one has it.
                    }
                    
                    if (postReaction != 0) {
                        article.addClass("post-liked");
                        btnLike.addClass("btn-inverted");
                        const post = data.reactions[0];
                        addNotification("like", post.Utente, post.Titolo);
                    }
                }
            })
            .fail(function(response) {
                console.log(response);
            });
            break;
        case "dislike":
            const dislike = new FormData();
            dislike.append("request", "aggiungiReaction");
            dislike.append("nPost", numero);
            dislike.append("like", 0);
            dislike.append("dislike", 1);
            dislike.append("type", "Dislike");
            $.ajax({
                type: "POST",
                url: fileintPost,
                data:  dislike, 
                processData: false,
                contentType: false
            })
            .done(function(data,success,response) {
                const temp=processaLike(data);
                const like=temp[0];
                const dislike=temp[1];
                const postReaction = temp[2];

                const btnLike = $("#btnLike"+numero);
                const btnDislike = $("#btnDislike"+numero);

                btnLike.text("Like: "+like);
                btnDislike.text("Dislike: "+dislike);

                let article = $("#post"+numero);
                let prevReaction = article.hasClass("post-liked") ? 1 : (article.hasClass("post-disliked") ? -1 : 0);
                
                if (postReaction != prevReaction) {
                    if (prevReaction != 0) {
                        let prevReactionClass = prevReaction == 1 ? "post-liked" : "post-disliked";
                        article.removeClass(prevReactionClass);
                        btnLike.removeClass("btn-inverted");
                        btnDislike.removeClass("btn-inverted");
                    }
                    
                    if (postReaction != 0) {
                        article.addClass("post-disliked");
                        btnDislike.addClass("btn-inverted");
                        const post = data.reactions[0];
                        addNotification("dislike", post.Utente, post.Titolo);
                    }
                }
            })
            .fail(function(response) {
                console.log(response);
            });
            break;
    }
    checkNewNotifications();
}

function setAlertLoad(){
    addAlert("alertC", "alert-warning", "Caricamento...", "");
}



// FUnzione per aggiungere una notifica.
function addNotification(tipo, utente, titolo, commento=""){
    const formdata = new FormData();
    formdata.append("request", "getUser");
    $.ajax({
        type: "POST",
        url: "templates/header/header_api.php",
        data:  formdata,
        processData: false,
        contentType: false
    })
    .done(function(data,success,response) {
        const seguace = data;
        formdata.set("request", "addNotification");
        formdata.append("utente", utente);
        formdata.append("visualizzata", "0");
        formdata.append("seguace", seguace);
        let testo="";
        switch (tipo){
            case 'seguace':
                if(titolo == "Segui")
                    testo = "L'utente " + seguace + " ha iniziato a seguirti!";
                else
                    testo = "L'utente " + seguace + " ha smesso di seguirti!";
                formdata.append("tipologia", tipo);
                break;
            case 'like':
                testo = "L'utente " + seguace + " ha messo like al post intitolato: '"+titolo+"'!";
                formdata.append("tipologia", tipo);
                break;
            case 'dislike':
                testo = "L'utente " + seguace + " ha messo dislike al post intitolato: "+titolo+"!";
                formdata.append("tipologia", tipo);
                break;
            case 'commento':
                if(commento.length>40){
                    commento = commento.slice(0,41);
                }
                testo = "L'utente " + seguace + " ha commentato il post intitolato: "+titolo+"!<br/>"+'"'+commento+'"';
                formdata.append("tipologia", tipo);
                break;
        }
        formdata.append("testo", testo);
        $.ajax({
            type: "POST",
            url: "templates/header/header_api.php",
            data:  formdata,
            processData: false,
            contentType: false
        })
        .done(function(data,success,response) {
        })
        .fail(function(response) {
            console.log(response);
        });    
    })
    .fail(function(response) {
        console.log(response);
    });    
}

// Funzioni per criptaggio e decriptaggio
function encrypt(password, key) {
    return CryptoJS.HmacSHA512(password, key).toString();
 }

function getUniqueId(){
    return CryptoJS.SHA1(Date.now().toString()).toString();
}
