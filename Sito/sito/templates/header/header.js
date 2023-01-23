$(document).ready(function() {
    checkNewNotifications();
    $('#esci').click(function(event) {
        event.preventDefault();
        const datas = new FormData(); 
        datas.append("request", "exit");
        $.ajax({
            type: "POST",
            url: "templates/header/header_api.php",
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

    $('#form_search').submit(function(event) {
        const data = getFormData("form_search");
        if(data.get("search")!==""){
            window.location.href="search.php?search="+data.get("search");
        }
        event.preventDefault();
    });
});

function checkNewNotifications(){
    const datas = new FormData(); 
    datas.append("request", "checkNotifications");
    $.ajax({
        type: "POST",
        url: "templates/header/header_api.php",
        data:  datas, 
        processData: false,
        contentType: false
    })
    .done(function(data,success,response) {
        if(data===true)
            $("#navNotifiche").addClass("blink");
    })
    .fail(function(response) {
        console.log(response);
    });
}

   
