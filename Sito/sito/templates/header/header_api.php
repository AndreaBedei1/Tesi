<?php
require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]){
        case "exit":
            if(isUserLoggedIn()){
                unset($_SESSION["user"]);
                $result="ok";
            }
            break;
        case "getUser":
            if(isUserLoggedIn()){
                $result = $_SESSION["user"];
            }
            break;
        case "addNotification":
            if(isUserLoggedIn() && isset($_POST["utente"]) && isset($_POST["visualizzata"]) && isset($_POST["testo"]) && isset($_POST["seguace"]) && isset($_POST["tipologia"])){
                $dbh->addNotification($_POST["utente"], $_POST["tipologia"], $_POST["testo"], $_POST["visualizzata"], today(), $_POST["seguace"]);
            }
            $result = "ok";
            break;
        case "checkNotifications":
            if(isUserLoggedIn()){
                $count = $dbh->checkNotificPresent($_SESSION["user"]);
                $num = $count[0]["Numero"];
                if($num != 0){
                    $result = true;
                } else {
                    $result = false;
                }
            }
            break;
    }
} 

header('Content-Type: application/json');
echo json_encode($result);
?>