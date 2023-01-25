<?php

require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]) {
        case 'tbl_avvistamenti':
        {
            if(isUserLoggedIn() && isset($_POST["id"])){
                $id = $_POST["id"];
                $rec = $dbh->getDates($_POST["id"]);
                for($i=0; $i<count($rec); $i++)
                {
                    $rec[$i]["Data"] = dataoraIT($rec[$i]["Data"]);
                    $rec[$i]["Latid"] = floatval($rec[$i]["Latid"]);
                    $rec[$i]["Long"] = floatval($rec[$i]["Long"]);
                }
                $result = $rec;
            }
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
    }
}


header('Content-Type: application/json');
echo json_encode($result);
?>