<?php
//dati per  connessione al db

$server = 'localhost';
$login = 'root';
$pwd = '';
$DB = 'app';

 #File allegati
 $file_types = array('pdf','doc','docx','rtf','odt','txt','xls','xlsx','ods','csv','jpg','jpeg','png','gif','bmp');
 $file_types_str = 'Formati file supportati: pdf, doc, docx, rtf, odt, txt, xls, xlsx, ods, csv, jpg, jpeg, png, gif, bmp';
 
//funzioni
function ora($ore)
{
    if(!empty($ore))
    {
        $ore = substr($ore,0,5);
    }

    return $ore;
}

function dataIT($data)
{
    if(!empty($data))
    {
        $data = substr($data,8,2)."/".substr($data,5,2)."/".substr($data,0,4);
    }

    return $data;
}

function dataoraIT($data)
{
    if(!empty($data))
    {
        $data = substr($data,8,2)."/".substr($data,5,2)."/".substr($data,0,4)." ".substr($data,11,5);
    }

    return $data;
}

function dataUSA($data)
{
    if(!empty($data))
    {
        $data = substr($data,6,4)."-".substr($data,3,2)."-".substr($data,0,2);
    }

    return $data;
}

function dataoraUSA($data)
{
    if(!empty($data))
    {
        $data = substr($data,6,4)."-".substr($data,3,2)."-".substr($data,0,2)." ".substr($data,11,7);
    }

    return $data;
}

function today()
{
    return date("Y-m-d H:i:s");
}

// FUNZIONI DB

function DB_connect($server,$login,$pwd,$DB)
{
    $conn = new mysqli($server,$login,$pwd,$DB);
    if($conn->connect_error)
    {
        die("Connessione fallita al db");
    }

    return $conn;
}

function DB_query($conn,$query)
{
    $result = $conn->query($query);
    return $result;
}

function DB_fetch($ris)
{
    $i=0;
    $result = array();
    while ($row = $ris->fetch_assoc()) {
        $result[$i] = $row;
        $i++; 
    }
    return $result;
}

function DB_close($conn)
{
    $conn->close();
}
