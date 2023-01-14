<?php
    header('Content-Type: application/json; charset=UTF-8');
    include("../template/myfunc.php");
    $conn = DB_connect($server,$login,$pwd,$DB);

    switch($_REQUEST['request'])
    {

        case 'tbl_avvistamenti':
        {
            $query = "SELECT avvistamento.*, utente.Nome, utente.Cognome ";
            $query .="FROM avvistamento JOIN utente ON avvistamento.Utente_ID=utente.ID ";
            $query .="ORDER BY Data ";
            $ristot = DB_query($conn,$query);
            $rec = DB_fetch($ristot);

            for($i=0; $i<count($rec); $i++)
            {
                $rec[$i]["Data"] = dataoraIT($rec[$i]["Data"]);
                $rec[$i]["Coord_Lon"] = floatval($rec[$i]["Coord_Lon"]);
                $rec[$i]["Coord_Lat"] = floatval($rec[$i]["Coord_Lat"]);
            }
            $return_array = $rec;
            break;
        }
    }

    if($conn)
        DB_close($conn);

    echo json_encode($return_array);
?>