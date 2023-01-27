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
                $data = $rec[0];
                $data["Data"] = dataoraIT($data["Data"]);
                $data["Latid"] = floatval($data["Latid"]);
                $data["Long"] = floatval($data["Long"]);
                $result = $data;
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

        case 'saveDates':
        {
            if(isUserLoggedIn() && isset($_POST["id"]) && isset($_POST["specie"]) && isset($_POST["sottospecie"]) && isset($_POST["esemplari"]) && isset($_POST["vento"]) && isset($_POST["mare"]) && isset($_POST["note"])){      
                $result = $dbh->updateDates($_POST["id"], $_POST["specie"], $_POST["sottospecie"], $_POST["esemplari"], $_POST["vento"], $_POST["mare"], $_POST["note"]);
            }
            break;
        }

        case 'delete':
            {
                if(isUserLoggedIn()){      
                    $result = $dbh->deleteAvv($_POST["id"]);
                }
                break;
            }
    }
}


header('Content-Type: application/json');
echo json_encode($result);
?>