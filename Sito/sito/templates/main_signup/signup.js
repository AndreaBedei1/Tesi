const fileint = "templates/main_signup/signup_api.php";
$(document).ready(function() {
    $("form").submit(function(event) {
        event.preventDefault();
        if(document.querySelector("form").checkValidity()){
            const datas = getFormData("form_sign");
            datas.append("request", "aggiungiUtente");
            const id = getUniqueId();
            if(datas.get("pwd") == datas.get("pwd2")){
                datas.append("password", encrypt(datas.get("pwd"), id));
                datas.append("key", id);
                datas.delete("pwd");
                $.ajax({
                    type: "POST",
                    url: "templates/main_signup/signup_api.php",
                    data:  datas,
                    processData: false,
                    contentType: false
                })
                .done(function(data,success,response) {
                    if(data["state"]===false){
                        addAlert("alert","alert-danger",data["msg"],"");
                    } else {
                        window.location.href="login.php?iscr=1";
                    }
                })
                .fail(function(response) {
                    console.log(response);
                });
            }
        }
    });
});