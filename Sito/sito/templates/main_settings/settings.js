const fileint = "templates/main_settings/settings_api.php";

$(document).ready(function() {
    setInterests();
    checkInterests();
    insertProfileImage();

    $("main nav").on("click",'button',function(){
        const typBtn = $(this).data("type");
        $(".section").hide().removeClass('d-none');
        $("#"+typBtn).show();
        $(".btnmenu button").removeClass("active")
        $('[data-type="'+typBtn+'"]').addClass("active");
        $("#alert").html("");
    });

    $("#security").on("click",'button',function(){
        const datas = new FormData();
        datas.append("request", "getKey");
        $.ajax({
            type: "POST",
            url: fileint,
            data:  datas, 
            processData: false,
            contentType: false
        })
        .done(function(data,success,response) {
            changePWD(data.key)
        })
        .fail(function(response) {
            console.log(response);
        });
    });

    $("#interests").on("click",'button',function(){
        const datas = getFormData("frmInterests");
        datas.append("request", "changeIntr");
        $.ajax({
            type: "POST",
            url: fileint,
            data:  datas, 
            processData: false,
            contentType: false
        })
        .done(function(data,success,response) {
            if(data.stato === false){
                addAlert("alert","alert-danger", data.msg,"");
            } else {
                addAlert("alert","alert-success", data.msg,"x");
            }
        })
        .fail(function(response) {
            console.log(response);
        });

    });

    $("#profile").on("click",'button',function(){
        const datas = new FormData();
        let file = $("#image")[0].files[0];
        if(file===undefined)
            file = "";
        datas.append("file",file);
        datas.append("request", "changeImg");
        $.ajax({
            type: "POST",
            url: fileint,
            data:  datas, 
            processData: false,
            contentType: false
        })
        .done(function(data,success,response) {
            if(data.stato === false){
                addAlert("alert","alert-danger", data.msg,"");
            } else {
                addAlert("alert","alert-success", data.msg,"x");
                $("#imgUtente").attr("src", "images/profile_img/"+data.file);
            }
        })
        .fail(function(response) {
            console.log(response);
        });
    });
});

function changePWD(key){
    const datas = getFormData("frmSecurity");
    if(datas.get("new") === datas.get("newk")){
        datas.delete("newk");
        datas.set("old", encrypt(datas.get("old"), key));
        datas.set("new", encrypt(datas.get("new"), key));
        datas.append("request", "changePwd");
        $.ajax({
            type: "POST",
            url: fileint,
            data:  datas, 
            processData: false,
            contentType: false
        })
        .done(function(data,success,response) {
            if(data.stato === false){
                addAlert("alert","alert-danger", data.msg,"");
            } else {
                addAlert("alert","alert-success", data.msg,"x");
                $("#frmSecurity").trigger("reset");
            }
        })
        .fail(function(response) {
            console.log(response);
        });
    } else {
        addAlert("alert","alert-danger", "Le password non corrispondono!","");
    }
}

function insertProfileImage(){
    const datas = new FormData();
    datas.append("request", "getUserInfo");
    $.ajax({
        type: "POST",
        url: fileint,
        data:  datas, 
        processData: false,
        contentType: false
    })
    .done(function(data,success,response) {
        $("#imgUtente").attr("src", "images/profile_img/"+data);
    })
    .fail(function(response) {
        console.log(response);
    });
}

function checkInterests(){
    const datas = new FormData();
    datas.append("request", "getInterests");

    $.ajax({
        type: "POST",
        url: fileint,
        data:  datas, 
        processData: false,
        contentType: false
    })
    .done(function(data,success,response) {
        data.interests.forEach(element => {
            $("#"+element.InterNome).prop('checked', true);
        });
    })
    .fail(function(response) {
        console.log(response);
    });
}