<?php

require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]) {
        case 'getKey':
            if(isUserLoggedIn()){
                $rec = $dbh->getKey($_SESSION["user"]);
                $result["key"]=$rec[0]["Chiave"];
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
        case 'getInterests':
            if(isUserLoggedIn()){
                $result["interests"] = $dbh->getInterestsUser($_SESSION["user"]);
            }
            break;
        case 'changeIntr':
            if(isUserLoggedIn()){
                if($dbh->deleteInterests($_SESSION["user"]))
                {
                    $val = $dbh->getInterests();
                    $state3 = true;
                    foreach ($val as $value) {
                        if(!empty($_POST[$value["Nome"]])){
                            $state3= $dbh->addInterests($_SESSION["user"], $_POST[$value["Nome"]]);
                            if($state3==false){
                                break;
                            }
                        }
                    }
                    if($state3){
                        $result["msg"] = "Modifica avvenuta con successo";
                        $result["stato"] = true;
                    }
                    else { 
                        $result["msg"] = "Errore inserimento interessi.";
                        $result["stato"] = false;
                    }
                } else {
                    $result["msg"] = "Errore del server.";
                    $result["stato"] = false;
                }
            }
            break;
        case 'getUserInfo':
            if(isUserLoggedIn()){
                $rec = $dbh->getUserInfo($_SESSION["user"]);
                $result = $rec[0]["Immagine"];       
            }
            break;
        case 'changeImg':
            if(isUserLoggedIn()){
                $rs_file = addFile($_FILES["file"], "profile_img");
                if($rs_file["errore"]==""){
                    if($rs_file["file"]==""){
                        $rs_file["file"] = "profilo.jpg";
                    }

                    if($dbh->changeProfile($rs_file["file"], $_SESSION["user"])){
                        $result["msg"] = "Modifica avvenuta con successo";
                        $result["stato"] = true;
                        $result["file"] = $rs_file["file"];

                    } else { 
                        $result["msg"] = "Errore cambio foto.";
                        $result["stato"] = false;
                    }
                } else {
                    $result["msg"]="Errore caricamento foto, ".$rs_file["errore"];
                    $result["stato"]=false;
                }
            }
            break;
    }
}


header('Content-Type: application/json');
echo json_encode($result);
?>