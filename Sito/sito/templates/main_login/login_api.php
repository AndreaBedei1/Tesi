<?php
require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]){
        case "email":
            if(isset($_POST["email"])){
                $mailResult = $dbh->checkMailAbsent($_POST["email"]);
                if(count($mailResult)==0){
                    $result["state"]=false;
                    $result["msg"]="Errore! Mail inserita non presente!";
                } else {
                    $result["state"]=true;
                    $result["Key"]=$mailResult[0]["Key"];
                }
            }
            break;
        case "pwd":
            $loginResult = $dbh->login($_POST["email"], $_POST["password"]);
            if(count($loginResult)==1){
                $result["state"]=true;
                registerLoggedUser($_POST["email"]);
            }
            else{
                $result["state"]=false;
                $result["msg"]="Password errata!";
            }
            break;
    }
}
header('Content-Type: application/json');
echo json_encode($result);
?>