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
            if(isUserLoggedIn()){
                $result = $dbh->getTypeS();
            }
            break;
        }

        case 'slcSottospecie':
        {
            if(isUserLoggedIn() && isset($_POST["selector"])){
                $result = $dbh->getTypeSS($_POST["selector"]);
            }
            break;
        }
    }
}


header('Content-Type: application/json');
echo json_encode($result);
?>