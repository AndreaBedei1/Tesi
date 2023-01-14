function select_codifiche(tabella, tipo, filtri, id_select, valore, vuoto, id_trigger)
{

    var a_session = getFormData('form_session_generale');
    var a_session_p = getFormData('form_session_generale_p');
    $.extend(a_session, a_session_p);

    var data = {
        'request': 'gestione_select',
        'tabella': tabella,
        'tipo': tipo,
        'filtri': filtri,
        'a_session': a_session
    };

    $.ajax({
        type: "POST",
        url: 'query_func_js.php',
        data: data
    })
    .done(function(data, success, response) {
      //console.log(data);
        var rows = '',
        dati = data.record_set;

        $("#" + id_select).html('');
        if(vuoto == 1)
            $("#" + id_select).append('<option></option>');
        if(dati != '' || dati != null)
        {
            for(var i = 0; i < dati.length; i++) {
                var dati_s = dati[i];

                rows += '<option value="' + dati_s.cod + '"';
                if(dati[i].cod == valore)
                    rows += ' selected';
                rows += '>' + dati_s.descr + '</option>';
            }
            $("#" + id_select).append(rows);
        }

        if(id_trigger != undefined)
        {
            var count = $("#" + id_trigger).val();
            count++;
            $("#" + id_trigger).val(count);
            $("#" + id_trigger).trigger('change');
        }
    })
    .fail(function(response) {
        return_fail(response);
    });
}

function select_file(file, request, filtri_data, id_select, valore, vuoto, id_trigger)
{
    var a_session = getFormData('form_session_generale');
    var a_session_p = getFormData('form_session_generale_p');
    $.extend(a_session, a_session_p);

    if(filtri_data == "")
        var filtri_data = {};
    $.extend(filtri_data, {'request': request,'valore_selected': valore, 'a_session': a_session});
//    console.log(filtri_data);
    $.ajax({
        type: "POST",
        url: file,
        data: filtri_data
    })
    .done(function(data, success, response) {
        //console.log('select_file');
        //console.log(request);
        //console.log(valore);
        //console.log(data);
        var rows = '',
        dati = data.record_set;
        $("#" + id_select).html('');

        if(data.errore_ric != null && data.errore_ric != undefined )
        {
            alert(data.errore_ric);
        }
        else
        {
            if(vuoto == 1)
                $("#" + id_select).append('<option></option>');
            if(dati != '' && dati != null)
            {
                for(var i = 0; i < dati.length; i++)
                {
                    var dati_s = dati[i];

                    rows += '<option value="' + dati_s.cod_select + '"';
                    if(dati[i].attr_select != '' && dati[i].attr_select!= null && dati[i].attr_select!= undefined)
                        rows += dati[i].attr_select;

                    if(dati_s.cod_select == valore)
                        rows += ' selected';
                    rows += '>' + dati_s.descr_select +'</option>';
                }
                $("#" + id_select).append(rows);
            }

            if(id_trigger != undefined)
            {
                var count = $("#" + id_trigger).val();
                count++;
                $("#" + id_trigger).val(count);
                $("#" + id_trigger).trigger('change');
            }
        }
    })
    .fail(function(response) {
        return_fail(response);
    });
}

function stampa_pdf(file, filtri_data, func)
{
    var a_session = getFormData('form_session_generale');
    var a_session_p = getFormData('form_session_generale_p');
    $.extend(a_session, a_session_p);

    if(filtri_data == undefined)
        filtri_data = {};

    $.extend(filtri_data, {'a_session': a_session});

    $.ajax({
        type: "POST",
        url: file,
        data: filtri_data
    })
    .then(function(data, success, response) {

        if(data.filename != '' && data.filename != null)
        {
            window.open(data.filename);

            if(func !== undefined)
            {
                func(data);
            }
        }
        else
        {
            console.log(data);
            alert('Errore nella creazione del file');
        }
    })
    .fail(function(response) {
        return_fail(response);
    });
}

function stampa_pagina(livello,orientation)
{
    var testo = $("#"+livello).html()
    stampa_pdf('query_func_js.php', {'request':'stampa_html','orientation':orientation,'testo':testo});
}

function getFormData(id_form)
{
    var indexed_array = {};
//    console.log(id_form);
    if($("#" + id_form).is( "form" )==true)
    {
        var $form = $("#" + id_form);
        var unindexed_array = $form.serializeArray();

        $.map(unindexed_array, function(n, i) {

            if(n['name'].substring(n['name'].length - 2, n['name'].length)=='[]')
            {
                if(indexed_array[n['name'].substring(0, n['name'].length-2)]==undefined)
                    indexed_array[n['name'].substring(0, n['name'].length-2)] = [];

                indexed_array[n['name'].substring(0, n['name'].length-2)].push(n['value']);
            }
            else
                indexed_array[n['name']] = n['value'];
        });

        if(id_form=='form_session_generale' && $("#form_session_generale_p").is( "form" )==true) //recupero anche i dati di sessione della somministrazione desktop se ci sono
        {
            var indexed_array_p = getFormData('form_session_generale_p');
            $.extend(indexed_array, indexed_array_p);
        }
    }
    else if(id_form=='form_session_generale')//caso dell'iframe
    {
        var window_parent = window.parent;

        while(window_parent != window.top)
            window_parent = window_parent.parent;

        var $form = $("#" + id_form, window_parent.parent.document);
        var unindexed_array = $form.serializeArray();

        $.map(unindexed_array, function(n, i) {
            indexed_array[n['name']] = n['value'];
        });
    }
    else
    {
        $("#"+id_form).each(function() {
            $(this).find('input:text, input:hidden, input:password, input:file, input:checkbox, select, textarea').each(function() {
                if($(this)[0].type=='checkbox')
                {
                    if($(this)[0].checked==true)
                        indexed_array[$(this)[0].name] = $(this)[0].value;
                    else
                        indexed_array[$(this)[0].name] = '';
                }
                else
                    indexed_array[$(this)[0].name] = $(this)[0].value;
            });
        });
    }

    return indexed_array;
}

function load_frame(id_frame,file,dati)
{

    var n = file.indexOf("?");
    var file_get = file.substring(0,n);
    var dati_file_get = file.substring(n+1);

    var dati_get = parseStr(dati_file_get);

    if(file_get!='')
        file = file_get;

    if(dati==undefined)
        dati = dati_get;
    else
        $.extend(dati, dati_get);

    var frame = $("#"+id_frame);

    //creo e inizializzo evento custom
    var load_frame_event = document.createEvent('Event');
    load_frame_event.initEvent('event_load', true, true);
    //faccio partire l'evento
    //in princ_att_farm.js � presente il listener su questo evento per gestire la presa in carico, in tutti gli altri casi l'evento viene ignorato
    frame[0].dispatchEvent(load_frame_event);

//    frame.empty();
    frame.load(file,dati, function (responseText, textStatus, req) {
        if (textStatus == "error")
        {
            alert( "Sorry, an error has occured, Requested page "+file+" not found!");
            $.ajax({
                type: "POST",
                url: 'query_func_js.php',
                data: {
                    'request':'log_load_frame',
                    'testo_errore': textStatus+' file:'+file,
                    'a_session':getFormData('form_session_generale')
                }
            })
            .then(function(data, success, response) {
            })
        }
    }).scrollTop(0);

}

function addAlert(id_append,classe,message,time_remove)
{
    var alert = $('<div class="alert '+classe+'">' +
            '<button type="button" class="close" data-dismiss="alert" onClick="$(this).parent().remove()">' +
            '&times;</button>' + message + '</div>');

    if(time_remove=='x')
        setTimeout(function () { alert.remove(); }, 5000);
    else if(time_remove!='' && time_remove!=undefined)
        setTimeout(function () { alert.remove(); }, time_remove);

    $('#'+id_append).append(alert);
}

function controlla_valore(campo, val_min, val_max, valore_in)
{
    var valore = campo.val();
    if(typeof (valore_in) == 'undefined')
        valore_in = "";
    //alert(valore_in);
    //alert(valore+'-'+val_min+'-'+val_max);
    if(isNaN(valore) || valore == '')
    {
        var testo = "Inserire un valore numerico ";
        if(val_min != null)
            testo = testo + " >= " + val_min;
        if(val_min != null && val_max != null)
            testo = testo + " e";
        if(val_max != null)
            testo = testo + " <= " + val_max;
        alert(testo);
        if(valore_in == "")
            campo.val("");
        else
            campo.val(valore_in);
        campo.select();
        return false;
    }
    else if(valore != "" && parseFloat(valore) > parseFloat(val_max))
    {
        alert("Valore massimo: " + val_max);
        if(valore_in == "")
            campo.val("");
        else
            campo.val(valore_in);
        campo.select();
        return false;
    }
    else if(valore != "" && parseFloat(valore) < parseFloat(val_min))
    {
        alert("Valore minimo: " + val_min);
        if(valore_in == "")
            campo.val("");
        else
            campo.val(valore_in);
        campo.select();
        return false;
    }
    else
        return true;
}

function mg_ml_val(id_campo, dose, rapp, um, um_ml, tipo)
{
    var a_session = getFormData('form_session_generale');
    var a_session_p = getFormData('form_session_generale_p');
    $.extend(a_session, a_session_p);

    if(um=='') um='mg';
    if(um_ml=='') um_ml='mg/ml';
    //$tipo -> '/' -> divisione per il passaggio da peso a volume (da mg a ml)
    //$tipo -> '*/*' -> moltiplicazione per il passaggio da volume a peso (da ml a mg)
    if(tipo=='') tipo='/';

    $.ajax({
        type: "POST",
        url: 'query_func_js.php',
        data: {"request": "mg_ml_val","dose":dose,"rapp":rapp,"um":um,"um_ml":um_ml,"tipo":tipo,'a_session':a_session}
    })
    .then(function(data, success, response) {
        if(data.dose_conv=='ERRORE')
        {
            alert('Errore conversione');
            $("#"+id_campo).val('');
        }
        else
            $("#"+id_campo).val(data.dose_conv);
    })
    .fail(function(response) {
        return_fail(response);
    });
}

function sup_max(sup, id_sup)
{
    if(id_sup == null)
        id_sup = 'sup';

    $.ajax({
        type: "POST",
        url: 'query_func_js.php',
        data: {"request": "sup_max"}
    })
    .then(function(data, success, response) {
        if(sup > data.sup_max_b)
        {
            alert('Superata superficie massima consentita: ' + sup + ' > ' + data.sup_max_b + '. Modificato valore superficie a ' + data.sup_max_b + '.');
            $("#" + id_sup).val(data.sup_max_b);
            sup = data.sup_max_b;
        }
        if(data.sup_max_a != '' && sup > data.sup_max_a)
        {
            if(confirm('Superata superficie massima di allerta: ' + sup + ' > ' + data.sup_max_a + '. Modificare valore superficie a ' + data.sup_max_a + '?'))
                $("#" + id_sup).val(data.sup_max_a);
        }
    })
    .fail(function(response) {
        return_fail(response);
    });
}

function get_label(string)
{
    var a_session = getFormData('form_session_generale');
    var a_session_p = getFormData('form_session_generale_p');
    $.extend(a_session, a_session_p);

    var lang = $("#lang").val();
    var response = "";

    if(lang == '' || lang == '1')
    {
        response = string;
    }
    else
    {
        //console.log('../join/query_func_js.php?request=get_label&string='+string);
        $.ajax({
            async: false,
            url: 'query_func_js.php',
            data: {'request': 'get_label', string: string, lang: lang, a_session: a_session}
        })
        .done(function(data)
        {
            response = data.word;
        });
    }
    return response;
}

/*uo/cce/cartella.js:    var data_rep = tblReparti("AND c_rep = '"+u_op+"' ");
 uo/cce/fut.js:    var data_rep = tblReparti("AND c_rep = '"+u_op+"' ");
 uo/cce/pr_farm_conf_el.js:    var data_rep = tblReparti("AND c_rep = '"+u_op+"' ");
 */
function tblReparti(filtro)
{
    var a_session = getFormData('form_session_generale');
    var a_session_p = getFormData('form_session_generale_p');
    $.extend(a_session, a_session_p);

//    console.log('../join/query_func_js.php?request=tblReparti&filtro='+filtro);
    var response = null;
    $.ajax({
        async: false,
        url: 'query_func_js.php',
        data: {'request': 'tblReparti', filtro: filtro, a_session: a_session}
    })
    .done(function(data)
    {
//        console.log(data);
        response = data.record_set;
    })
    .fail(function(response) {
        return_fail(response);
    });

    return response;
}

function titolo_reparto(c_rep)
{
    var a_session = getFormData('form_session_generale');
    var a_session_p = getFormData('form_session_generale_p');
    $.extend(a_session, a_session_p);

    var filtro = "AND c_rep = '" + c_rep + "' ";

    var data = {
        'request': 'tblReparti',
        'filtro': filtro,
        'a_session': a_session
    };

    $.ajax({
        url: 'query_func_js.php',
        type: 'post',
        data: data
    })
    .then(function(data, success, response) {
        var titolo = data.record_set[0].d_rep;

        $(".div_titolo_reparto").html(titolo);
    })
    .fail(function(response) {
        return_fail(response);
    });
}

function titolo_paziente()
{
    var a_session = getFormData('form_session_generale');
    var a_session_p = getFormData('form_session_generale_p');
    $.extend(a_session, a_session_p);

    var data = {
        'request': 'titolo_paziente',
        'a_session': a_session
    };

    $.ajax({
        url: 'query_func_js.php',
        type: 'post',
        data: data
    })

    .then(function(data, success, response) {
        var dati = data.record_set;
        var titolo = '';
        titolo += dati.cogn + ' ' + dati.nome + ' ' + dati.d_nas_it;

        $(".div_titolo_paziente").html(titolo);
    })
    .fail(function(response) {
        return_fail(response);
    });
}

// per cambiare tutti i titoli di una classe
function cambia_titolo(classe)
{
    $('.' + classe).each(function(i, obj) {
        var titolo = $(this).text();
        var titolo_n = get_label(titolo);
        $(this).text(titolo_n);
    });
}

function checkbox_codifiche(tabella, tipo, filtri, id_div, nome_campo, valore)
{
    var a_session = getFormData('form_session_generale');
    var a_session_p = getFormData('form_session_generale_p');
    $.extend(a_session, a_session_p);

    var data = {
        'request': 'gestione_select',
        'tabella': tabella,
        'tipo': tipo,
        'filtri': filtri,
        'a_session': a_session
    };

    $.ajax({
        type: "POST",
        url: 'query_func_js.php',
        data: data
    })

    .done(function(data, success, response) {
        //console.log(data);
        var row = '',
        dati = data.record_set,
        temp = valore.split("|");

        $("#" + id_div).html('');

        for(var i = 0; i < dati.length; i++) {
            row += '<label class="campo"><input type="checkbox" name="' + nome_campo + '[]" value="' + data.record_set[i].cod + '"';

            for(var j = 0; j < temp.length; j++)
            {
                if(temp[j] == data.record_set[i].cod)
                    row += ' checked';
            }
            row += '>' + data.record_set[i].descr + '</label>';
        }

        $("#" + id_div).append(row);
    })

    .fail(function(response) {
        return_fail(response);
    });
}

function referto_copia(campo, funzione, n_ind, k_pri, data, f_nascondi)
{
    var a_session = getFormData('form_session_generale');
    var a_session_p = getFormData('form_session_generale_p');
    $.extend(a_session, a_session_p);

    $.ajax({
        url: 'query_func_js.php',
        type: 'post',
        data: {
            'request': 'trova_testo',
            'campo': funzione,
            'n_ind_passato': n_ind,
            'k_pri_passato': k_pri,
            'data': data,
            'a_session': a_session
        }
    })

    .done(function(data, success, response) {
        var testo = data.testo;
        var classe = $("#"+campo).attr('class');

        if(classe == 'summernote textarea_ro')
        {
            $('#' + campo).summernote('code', testo);
            //$("#"+campo).val(testo);
            $("#vis_" + campo).html(testo);
        }
        else if(classe == 'summernote')
        {
            $('#' + campo).summernote('code', testo);
        }
        else
        {
            $('#' + campo).html('');
            if(testo != '')
            {
                $('#' + campo).show();
                $('#' + campo).append(testo);
            }
            else if(f_nascondi == 1)
                $('#' + campo).hide();
        }
    })

    .fail(function(response) {
        return_fail(response);
    });
}


function return_fail(response)
{
    console.log('return_fail');
    console.log(response);
    if(response.responseText=='scaduta')
    {
        window.open(indir,"_top");
        alert("La sessione e\' scaduta");
    }
    else if(response.responseText=='non_attiva')
    {
        window.open(indir,"_top");
        alert("La sessione non e\' attiva.");
    }
    else if(response.responseText==undefined)
    {
        alert("Errore [responseText undefined]");
    }
    else if(response.responseText.substring(0,11)=='errore_conn')
    {
        alert("Errore di connessione "+response.responseText.substring(12));
    }
    else
    {
        alert('Errore: '+response.responseText);

        $.ajax({
            type: "POST",
            url: 'query_func_js.php',
            data: {
                'request':'log_return_fail',
                'testo_errore': response.responseText,
                'a_session':getFormData('form_session_generale')
            }
        })
        .then(function(data, success, response) {
        })
    }
}

function open_modal(id_div_modal)
{
    $("#"+id_div_modal).data("parent", $("#"+id_div_modal).parent());
    $("#"+id_div_modal).appendTo("#div_destro");
    $("#"+id_div_modal).show();
}

function close_modal(id_div_modal)
{
    $("#"+id_div_modal).hide();
    $("#"+id_div_modal).appendTo($("#"+id_div_modal).data("parent"));
}

function parseStr(s)
{
    var rv = {}, decode = window.decodeURIComponent || window.unescape;
    (s == null ? location.search : s).replace(/^[?#]/, "").replace(/%/, "").replace(
    /([^=&]*?)((?:\[\])?)(?:=([^&]*))?(?=&|$)/g,
    function($, n, arr, v) {
        if(n == "")
            return;
        n = decode(n);
        v = decode(v);
        if(arr) {
            if(typeof rv[n] == "object")
                rv[n].push(v);
            else
                rv[n] = [v];
        } else {
            rv[n] = v;
        }
    });

    return rv;
}
