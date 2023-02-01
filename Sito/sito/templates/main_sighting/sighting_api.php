<?php

require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]) {
        case 'tbl_avvistamenti':
            if(isUserLoggedIn()){
                $rec = $dbh->getSighting();
                for($i=0; $i<count($rec); $i++)
                {
                    $rec[$i]["Data"] = dataoraIT($rec[$i]["Data"]);
                    $rec[$i]["Latid"] = floatval($rec[$i]["Latid"]);
                    $rec[$i]["Long"] = floatval($rec[$i]["Long"]);
                    if(is_null($rec[$i]["Anima_Nome"]))
                        $rec[$i]["Anima_Nome"] = "?";
                }
                $result = $rec;
            }
        break;
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
        case 'saveAvv':
        {
            if(isUserLoggedIn() && isset($_POST["data"]) && isset($_POST["esemplari"]) && isset($_POST["latitudine"]) && isset($_POST["longitudine"]) && isset($_POST["specie"]) && isset($_POST["sottospecie"]) && isset($_POST["mare"]) && isset($_POST["vento"]) && isset($_POST["note"])){
                $user = $dbh->getUserInfo($_SESSION["user"]);
                $result = $dbh->saveAvv($user[0]["ID"], $_POST["data"], $_POST["esemplari"], $_POST["latitudine"], $_POST["longitudine"], $_POST["specie"], $_POST["sottospecie"], $_POST["mare"], $_POST["vento"], $_POST["note"]);
            }
            break;

        }
    }
}

header('Content-Type: application/json');
echo json_encode($result);


?>