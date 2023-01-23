const fileint2 = "../templates/header/header_api.php";
$(document).ready(function() {
    $('#esci').click(function(event) {
        event.preventDefault();
        const datas = new FormData(); 
        datas.append("request", "exit");
        $.ajax({
            type: "POST",
            url: fileint2,
            data:  datas, 
            processData: false,
            contentType: false
        })
        .done(function(data,success,response) {
            window.location.href="login.php";
        })
        .fail(function(response) {
            console.log(response);
        });
    });
});

   
