<?php
require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    if($_POST["request"] == "aggiungiUtente"){
        if(isset($_POST["email"]) && isset($_POST["nome"]) && isset($_POST["cognome"]) && isset($_POST["password"]) && isset($_POST["key"])){
            $mailResult = $dbh->checkMailAbsent($_POST["email"]);
            if(count($mailResult)==0){
                $state = $dbh->addUser($_POST["nome"], $_POST["cognome"], $_POST["password"], $_POST["email"], $_POST["key"]);
                if($state){
                    $result["state"]=true;
                    // sendEmail($_POST["email"], $_POST["username"], "Ciao ".$_POST["username"].", la tua iscrizione e' stata completata! \nBenvenuto in U-niversity!!!");
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
    }
}

header('Content-Type: application/json');
echo json_encode($result);
?>