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
    }
} 

header('Content-Type: application/json');
echo json_encode($result);
?>