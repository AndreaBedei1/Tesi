<?php

require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]) {
        case 'getUserInfo':
            if(isset($_POST["user"]) && isUserLoggedIn()){
                $result["dati"] = $dbh->getUserInfo($_POST['user']);
                if(count($result["dati"])==0){
                    $result["errore"]=true;
                } else {
                    $result["errore"]=false;
                    $result["dati"][0]["DataNascita"] = dataIT($result["dati"][0]["DataNascita"]);
                    if($result["dati"][0]["Sesso"]=="M"){
                        $result["dati"][0]["Sesso"] = "Maschio";
                    } else if ($result["dati"][0]["Sesso"]=="F"){
                        $result["dati"][0]["Sesso"] = "Femmina";
                    }
                }
            }
            break;
    
        case 'getFriendship':
            if(isset($_POST["user"]) && isUserLoggedIn()){
                $result["dati"]["seguaci"] = $dbh->getFollower($_POST['user']);
                $result["dati"]["seguiti"] = $dbh->getFollow($_POST['user']);
            }
            break;
        case 'checkFollow':
            if(isset($_POST["user"]) && isUserLoggedIn()){
                if($_POST["user"]==$_SESSION["user"]){
                    $result["dati"]["disabled"] = true;
                    $result["dati"]["testo"] = "Segui";
                } else {
                    $check = $dbh->checkFollow($_POST['user'], $_SESSION["user"]);
                    if(count($check)==0){
                        $result["dati"]["disabled"] = false;
                        $result["dati"]["testo"] = "Segui";
                        $result["dati"]["btndata"] = "f";
                    }
                    else{
                        $result["dati"]["disabled"] = false;
                        $result["dati"]["testo"] = "Non Seguire";
                        $result["dati"]["btndata"] = "nf";
                    }
                }
            }
            break;
        case'modifyFollow':
            if(isset($_POST["type"]) && isset($_POST["user"]) && isUserLoggedIn()){
                if($_POST["type"] == "f"){
                    $result["dati"]=$dbh->addFollow($_SESSION["user"], $_POST["user"]);
                    $mail = $dbh->getEmail($_POST["user"]);
                    sendEmail($mail[0]["Mail"], $_POST["user"], "L'utente ".$_SESSION["user"]." ha iniziato a seguirti!");
                } else {
                    $result["dati"]=$dbh->removeFollow($_SESSION["user"], $_POST["user"]);
                }
            }
            break;
    }
}



header('Content-Type: application/json');
echo json_encode($result);


?>