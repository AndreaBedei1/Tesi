const fileint = "../templates/main_login/login_api.php";
$(document).ready(function() {
    if($("#login").val()==1){
        addAlert("alert","alert-success","Iscrizione completata! Eseguire l'accesso.","");
    }
    
    $("#form_login").submit(function(event) {
        event.preventDefault();
        const datas = getFormData("form_login");
        datas.append("request", "email")
        const pwd = datas.get("pwd");
        datas.delete("pwd");
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
                datas.set("request", "pwd");
                datas.append("password", encrypt(pwd, data.Key));
                $.ajax({
                    type: "POST",
                    url: fileint,
                    data:  datas,
                    processData: false,
                    contentType: false
                })
                .done(function(data,success,response) {
                    console.log(data);
                    if(data["state"]===false){
                        addAlert("alert","alert-danger",data["msg"],"");
                    } else {
                        window.location.href="homepage.php";
                    }
                })
                .fail(function(response) {
                    console.log(response);
                });
            }
        })
        .fail(function(response) {
            console.log(response);
        });
    });
});