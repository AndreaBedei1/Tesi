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
            if(isset($_POST["email"]) && isset($_POST["password"])){
                $loginResult = $dbh->login($_POST["email"], $_POST["password"]);
                if(count($loginResult)==1){
                    $result["state"]=true;
                    registerLoggedUser($_POST["email"]);
                }
                else{
                    $result["state"]=false;
                    $result["msg"]="Password errata!";
                }
            }
            break;
        case "resetPwd":
            if(isset($_POST["email"])){
                $mailResult = $dbh->checkMailAbsent($_POST["email"]);
                if(count($mailResult)==0){
                    $result["state"]=false;
                    $result["msg"]="Errore! Mail inserita non presente!";
                } else {
                    $starter = uniqid(random_int(0,90)."Numero");
                    $pwd = HmacSHA512($starter, $mailResult[0]["Key"]);
                    $res = $dbh->resetPwd($_POST["email"], $pwd);
                    if($res){
                        // $testo = "La nuova password temporanea e': ".$starter;
                        sendEmail($_POST["email"], $starter);
                        $result["state"]=true;
                    } else {
                        $result["state"]=false;
                        $result["msg"]="Errore imprevisto!";
                    }
                }
            }          
            break;
    }
}
header('Content-Type: application/json');
echo json_encode($result);
?>