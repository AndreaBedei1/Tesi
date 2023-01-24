<?php

require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]) {
        case 'getKey':
            if(isUserLoggedIn()){
                $rec = $dbh->getKey($_SESSION["user"]);
                $result["key"]=$rec[0]["Key"];
            }
            break;
        case 'changePwd':
            if(isset($_POST["old"]) && isset($_POST["new"]) && isUserLoggedIn()){
                $rec = $dbh->checkPwd($_SESSION["user"], $_POST["old"]);
                if(count($rec)==1){
                    $rec = $dbh->changePwd($_SESSION["user"], $_POST["new"]);
                    if($rec){
                        $result["stato"] = true;
                        $result["msg"] = "Cambiamento password avvenuto con successo.";
                    }
                } else {
                    $result["stato"] = false;
                    $result["msg"] = "La vecchia password non corrisponde.";
                }
            }
            break;
        case 'getUserInfo':
            if(isUserLoggedIn()){
                $result = $dbh->getUserInfo($_SESSION["user"]);    
            }
            break;
        case 'setUserInfo':
            if(isUserLoggedIn() && isset($_POST["nome"]) && isset($_POST["cognome"])){
                $rec = $dbh->setUserInfo($_SESSION["user"], $_POST["nome"], $_POST["cognome"]);
                if($rec){
                    $result["stato"] = true;
                    $result["msg"] = "Cambiamento dati avvenuto con successo.";
                } else {
                    $result["stato"] = false;
                    $result["msg"] = "Cambiamento dati non avvenuto.";
                }
            }
            break;
    }
}


header('Content-Type: application/json');
echo json_encode($result);
?>