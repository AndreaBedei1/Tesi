<?php
    header('Content-Type: application/json; charset=UTF-8');
    include("../template/myfunc.php");
    $conn = DB_connect($server,$login,$pwd,$DB);

    switch($_REQUEST['request'])
    {

        case 'tbl_avvistamenti':
        {
            $id = $_REQUEST['id'];
            $query = "SELECT avvistamento.*, utente.Nome, utente.Cognome, specie.Nome AS Specie, sottospecie.Nome AS Sottospecie ";
            $query .="FROM avvistamento JOIN utente ON avvistamento.Utente_ID=utente.ID JOIN sottospecie ON avvistamento.Sottospecie_Nome=sottospecie.Nome JOIN specie ON sottospecie.Specie_Nome=specie.Nome ";
            $query .="WHERE avvistamento.ID=".$id;
            $ristot = DB_query($conn,$query);
            $rec = DB_fetch($ristot);

            $rec[0]["Data"] = dataoraIT($rec[0]["Data"]);
            $rec[0]["Coord_Lon"] = floatval($rec[0]["Coord_Lon"]);
            $rec[0]["Coord_Lat"] = floatval($rec[0]["Coord_Lat"]);

            $return_array = $rec;
            break;
        }

        case 'slcSpecie':
        {
            $query = "SELECT *, Nome AS cod_select, Nome AS descr_select FROM specie ";
            $ristot = DB_query($conn,$query);
            $rec = DB_fetch($ristot);
            $return_array = $rec;
            break;
        }

        case 'slcSottospecie':
        {
            $specie = $_REQUEST['specie'];
            $query = 'SELECT *, Nome AS cod_select, Nome AS descr_select FROM sottospecie WHERE Specie_Nome="'.$specie.'" ';
            $ristot = DB_query($conn,$query);
            $rec = DB_fetch($ristot);
            $return_array = $rec;
            break;
        }

        case 'images':
        {
            $id = $_REQUEST['id'];
            $query = "SELECT Img FROM immagine WHERE Avvistamento_ID=".$id;
            $ristot = DB_query($conn,$query);
            $rec = DB_fetch($ristot);
            $return_array = $rec;
            break;   
        }
    }

    if($conn)
        DB_close($conn);

    echo json_encode($return_array);
?>