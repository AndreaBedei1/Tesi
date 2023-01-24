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
                }
                $result = $rec;
            }
        break;
    }
}


header('Content-Type: application/json');
echo json_encode($result);


?>