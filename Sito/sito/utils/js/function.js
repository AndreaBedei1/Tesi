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

// Funzioni per criptaggio e decriptaggio
function encrypt(password, key) {
    return CryptoJS.HmacSHA512(password, key).toString();
 }

function getUniqueId(){
    return CryptoJS.SHA1(Date.now().toString()).toString();
}
