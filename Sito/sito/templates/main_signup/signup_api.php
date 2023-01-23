<?php
require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    if($_POST["request"] == "interessiPossibili") {
        $rec = $dbh->getInterests();
        $result["dati"] = $rec;
    } else if($_POST["request"] == "aggiungiUtente"){
        if(isset($_POST["username"]) && isset($_POST["email"]) && isset($_POST["nome"]) && isset($_POST["cognome"]) && isset($_POST["genere"]) && isset($_POST["nascita"]) && isset($_POST["password"])){
            $userResult = $dbh->checkUserAbsent($_POST["username"]);
            if(count($userResult)==0){
                $mailResult = $dbh->checkMailAbsent($_POST["email"]);
                if(count($mailResult)==0){
                    $state = $dbh->addUser($_POST["username"], $_POST["nome"], $_POST["cognome"], $_POST["genere"], $_POST["nascita"]);
                    $state2 = $dbh->addCredentials($_POST["username"], $_POST["email"], $_POST["password"], $_POST["key"]);            
                    $val = $dbh->getInterests();
                    $state3=true;
                    foreach ($val as $value) {
                        if(!empty($_POST[$value["Nome"]])){
                            $state3= $dbh->addInterests($_POST["username"], $_POST[$value["Nome"]]);
                            if($state3==false){
                                break;
                            }
                        }
                    }
                    if($state && $state2 && $state3){
                        $result["state"]=true;
                        sendEmail($_POST["email"], $_POST["username"], "Ciao ".$_POST["username"].", la tua iscrizione e' stata completata! \nBenvenuto in U-niversity!!!");
                    }
                    else{
                        $result["state"]=false;
                        $result["msg"]="Errore di inserimento del nuovo utente!";
                    }
                }
                else{
                    $result["state"]=false;
                    $result["msg"]="Errore! Mail duplicata!";
                }
                
            }
            else{
                $result["state"]=false;
                $result["msg"]="Errore! Username già presente!";
            }
        }
    }
}

header('Content-Type: application/json');
echo json_encode($result);
?>