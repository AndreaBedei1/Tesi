<?php

require("../../bootstrap.php");

function riconoscimento($img){
    $command = 'sudo -i -u andrea /var/www/html/Tesi/Sito/sito/templates/single_sighting/bash.sh '.$img;
    exec($command, $output, $status);
    $ris="";
    if ($status) {
       $ris="Il comando non è stato eseguito correttamente.";
    } else {
       foreach ($output as $line) {
          $ris .= $line;
       }
       $ris = explode("Risultato: ", $ris)[1];
       $ris = explode("-", $ris);
    }
    return $ris;
}

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]) {
        case 'getDates':
            if(isUserLoggedIn() && isset($_POST["id"])){
                $id = $_POST["id"];
                $rec = $dbh->getDates($_POST["id"]);
                $data = $rec[0];
                $data["Latid"] = floatval($data["Latid"]);
                $data["Long"] = floatval($data["Long"]);
                $result = $data;
            }
            break;
        case 'slcSpecie':
            if(isUserLoggedIn()){
                $result = $dbh->getTypeS();
            }
            break;
        case 'slcSottospecie':
            if(isUserLoggedIn() && isset($_POST["selector"])){
                $result = $dbh->getTypeSS($_POST["selector"]);
            }
            break;
        case 'saveDates':
            if(isUserLoggedIn() && isset($_POST["id"]) && isset($_POST["specie"]) && isset($_POST["sottospecie"]) && isset($_POST["esemplari"]) && isset($_POST["vento"]) && isset($_POST["mare"]) && isset($_POST["note"]) && isset($_POST["latitudine"]) && isset($_POST["longitudine"])){      
                $result = $dbh->updateDates($_POST["id"], $_POST["specie"], $_POST["sottospecie"], $_POST["esemplari"], $_POST["vento"], $_POST["mare"], $_POST["note"], $_POST["latitudine"], $_POST["longitudine"]);
            }
            break;
        case 'delete':
            if(isUserLoggedIn()){      
                $result = $dbh->deleteAvv($_POST["id"]);
            }
            break;
        case 'addImage':
            if(isset($_POST["id"])  && isUserLoggedIn()){
                $rs_file = $rs_file = addFile($_FILES["file"]);
                if($rs_file["errore"]=="")
                    $postResult = $dbh->addImage($_POST["id"], $rs_file["file"]);
                    if($postResult){
                        $result["state"]=true;
                    }
                else {
                    $result["state"]=false;
                    $result["msg"]="Errore nel caricamento dell'immagine:  ".$rs_file["errore"];
                }
                break;
            } else {
                $result["state"]=false;
                $result["msg"]="Immagine non valida!";
            }
        case 'getImages':
            if(isUserLoggedIn() && isset($_POST["id"])){
                $result = $dbh->getImages($_POST["id"]);
            }
            break;
        case 'infoSpecie':
            if(isUserLoggedIn() && isset($_POST["animale"]) && isset($_POST["specie"])){
                $result = $dbh->infoSpecie($_POST["animale"], $_POST["specie"]);
            }
            break;
        case 'getSottoimmagini':
            if(isUserLoggedIn() && isset($_POST["id"])){
                $result["sImmagini"] = $dbh->getSottoimmagini($_POST["id"]);
                for ($i=0; $i < count($result["sImmagini"]); $i++) {
                    if(is_null($result["sImmagini"][$i]["nome"])){
                        $result["sImmagini"][$i]["nome"]="";
                    }
                    $result["sImmagini"][$i]["ferite"]=$dbh->getFerite($result["sImmagini"][$i]["ID"], $_POST["id"]);
                }
            }
            break;
        case 'updateIndv':
            if(isUserLoggedIn() && isset($_POST["id"]) && isset($_POST["nome"])){
                $result = $dbh->updateIndv($_POST["id"], $_POST["nome"]);
            }
            break;
        case 'addSottoimmagine':
            if(isUserLoggedIn() && isset($_POST["id"]) && isset($_POST["bx"]) && isset($_POST["by"]) && isset($_POST["tx"]) && isset($_POST["ty"]) && isset($_POST["esemID"])){
                $p=0;
                do {
                    $p++;
                    $rec = $dbh->checkID($p, $_POST["id"]);
                } while(count($rec)!=0);
                $result = $dbh->addSottoimmagine($_POST["tx"], $_POST["ty"], $_POST["bx"], $_POST["by"], $_POST["id"], $_POST["esemID"], $p);
            }
            break;
        case 'delateID':
            if(isUserLoggedIn() && isset($_POST["id"]) && isset($_POST["img"])){
                $res = $dbh->checkID($_POST["id"], $_POST["img"]);
                $dbh->deleteFerite($_POST["id"], $_POST["img"]);
                $dbh->deleteSottoimmagini($_POST["id"], $_POST["img"]);
                if(count($res)<2){
                    $dbh->deleteIndiv($res[0]["Esemp_ID"]);
                }
            }
            break;
        case 'deleteImmg':
            if(isUserLoggedIn() && isset($_POST["id"])){
                $res = $dbh->getSottoimmagini($_POST["id"]);
                foreach ($res as $k) {
                    $res = $dbh->checkID($k["ID"], $_POST["id"]);
                    $dbh->deleteFerite($k["ID"], $_POST["id"]);
                    $dbh->deleteSottoimmagini($k["ID"], $_POST["id"]);
                    if(count($res)<2){
                        $dbh->deleteIndiv($res[0]["Esemp_ID"]);
                    }
                }
                $dbh->deleteImmagini($_POST["id"]);
            }
            break;
        case 'deleteFerita':
            if(isUserLoggedIn() && isset($_POST["id"])){
                $dbh->deleteFeritaByID($_POST["id"]);
            }
            break;
        case 'getGravita':
            if(isUserLoggedIn()){
                $result=$dbh->getGravita();
            }
            break;
        case 'getInjury':
            if(isUserLoggedIn() && isset($_POST["id"])){
                $result=$dbh->getInjury($_POST["id"]);
            }
            break;
        case 'updateInjury':
            if(isUserLoggedIn() && isset($_POST["id"]) && isset($_POST["posizione"])  && isset($_POST["gravita"]) && isset($_POST["descrizione"])){
                $result=$dbh->updateInjury($_POST["id"], $_POST["posizione"], $_POST["gravita"], $_POST["descrizione"]);
            }
            break;
        case 'addInjury':
            if(isUserLoggedIn() && isset($_POST["img"]) && isset($_POST["sottImg"]) && isset($_POST["posizione"])  && isset($_POST["gravita"]) && isset($_POST["descrizione"])){
                $result=$dbh->addInjury($_POST["img"], $_POST["sottImg"], $_POST["posizione"], $_POST["gravita"], $_POST["descrizione"]);
            }
            break;
        case 'getEsem':
            if(isUserLoggedIn()){
                $result=$dbh->getEsem();
            }
            break;
        case 'updateEsempl':
            if(isUserLoggedIn()  && isset($_POST["nomeSlc"])  && isset($_POST["img"]) && isset($_POST["id"])){
                $res = $dbh->checkID($_POST["id"], $_POST["img"]);
                $result=$dbh->updateEsempl($_POST["nomeSlc"], $_POST["img"], $_POST["id"]);
                if(count($res)==1){
                    $dbh->deleteIndiv($res[0]["Esemp_ID"]);
                }
            }
            break;
        case 'createEsempl':
            if(isUserLoggedIn() && isset($_POST["nome"]) && isset($_POST["img"]) && isset($_POST["id"])){
                $res = $dbh->checkID($_POST["id"], $_POST["img"]);
                $p=0;
                do {
                    $p++;
                    $rec = $dbh->checkEsempID($p);
                } while(count($rec)!=0);
                $agg = $dbh->addEsempl($p, $_POST["nome"]);
                if($agg){
                    $dbh->updateEsempl($p, $_POST["img"], $_POST["id"]);
                    if(count($res)==1){
                        $dbh->deleteIndiv($res[0]["Esemp_ID"]);
                    }
                } else {
                    return false;
                }
            }
            break;
        case 'recognition':
            if (isUserLoggedIn() && isset($_POST["id"])) {
                $imgs = $dbh->getImages($_POST["id"]);
                if(count($imgs)>=1){
                    $result["state"] = true;
                    $arr = array();
                    $str = "";
                    foreach ($imgs as $k) {
                        $str.=" ".$k["Img"]." ";
                    }
                    $arr = riconoscimento($str);
                    $result["data"] = $arr;

                }else{
                    $result["state"] = false;
                }
            }
            break;
    }
}

header('Content-Type: application/json; charset=UTF-8');
echo json_encode($result, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
?>