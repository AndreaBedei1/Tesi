<?php

require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]) {
        case 'getDates':
        {
            if(isUserLoggedIn() && isset($_POST["id"])){
                $id = $_POST["id"];
                $rec = $dbh->getDates($_POST["id"]);
                $data = $rec[0];
                $data["Latid"] = floatval($data["Latid"]);
                $data["Long"] = floatval($data["Long"]);
                $result = $data;
            }
            break;
        }
        case 'slcSpecie':
        {
            if(isUserLoggedIn()){
                $result = $dbh->getTypeS();
            }
            break;
        }
        case 'slcSottospecie':
        {
            if(isUserLoggedIn() && isset($_POST["selector"])){
                $result = $dbh->getTypeSS($_POST["selector"]);
            }
            break;
        }
        case 'saveDates':
        {
            if(isUserLoggedIn() && isset($_POST["id"]) && isset($_POST["specie"]) && isset($_POST["sottospecie"]) && isset($_POST["esemplari"]) && isset($_POST["vento"]) && isset($_POST["mare"]) && isset($_POST["note"])){      
                $result = $dbh->updateDates($_POST["id"], $_POST["specie"], $_POST["sottospecie"], $_POST["esemplari"], $_POST["vento"], $_POST["mare"], $_POST["note"]);
            }
            break;
        }
        case 'delete':
        {
            if(isUserLoggedIn()){      
                $result = $dbh->deleteAvv($_POST["id"]);
            }
            break;
        }
        case 'addImage':
        {
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
        }
        case 'getImages':
        {
            if(isUserLoggedIn() && isset($_POST["id"])){
                $result = $dbh->getImages($_POST["id"]);
            }
            break;
        }
        case 'infoSpecie':
        {
            if(isUserLoggedIn() && isset($_POST["animale"]) && isset($_POST["specie"])){
                $result = $dbh->infoSpecie($_POST["animale"], $_POST["specie"]);
            }
            break;
        }
        case 'getSottoimmagini':
        {
            if(isUserLoggedIn() && isset($_POST["id"])){
                $result["sImmagini"] = $dbh->getSottoimmagini($_POST["id"]);
                for ($i=0; $i < count($result["sImmagini"]); $i++) {
                    if(is_null($result["sImmagini"][$i]["nome"])){
                        $result["sImmagini"][$i]["nome"]="";
                    }
                    $result["sImmagini"][$i]["ferite"]=$dbh->getFerite($result["sImmagini"][$i]["ID"], $_POST["id"]);
                }
                $result["gravita"]=$dbh->getGravita();
            }
            break;
        }
        case 'updateIndv':
        {
            if(isUserLoggedIn() && isset($_POST["id"]) && isset($_POST["nome"])){
                $result = $dbh->updateIndv($_POST["id"], $_POST["nome"]);
            }
            break;
        }
        case 'addSottoimmagine':
        {
            if(isUserLoggedIn() && isset($_POST["id"]) && isset($_POST["bx"]) && isset($_POST["by"]) && isset($_POST["tx"]) && isset($_POST["ty"])){
                $esemID = $dbh->maxEsemplId()[0]["id"]+1;
                $dbh->addEsempl($esemID);
                $p=0;
                do {
                    $p++;
                    $rec = $dbh->checkID($p, $_POST["id"]);
                } while(count($rec)!=0);
                $result = $dbh->addSottoimmagine($_POST["tx"], $_POST["ty"], $_POST["bx"], $_POST["by"], $_POST["id"], $esemID, $p);
            }
            break;
        }
        case 'delateID':
        {
            if(isUserLoggedIn() && isset($_POST["id"]) && isset($_POST["img"])){
                $res = $dbh->checkID($_POST["id"], $_POST["img"]);
                $dbh->deleteFerite($_POST["id"], $_POST["img"]);
                $dbh->deleteSottoimmagini($_POST["id"], $_POST["img"]);
                if(count($res)<2){
                    $dbh->deleteIndiv($res[0]["Esemp_ID"]);
                }
            }
            break;
        }
    }
}


header('Content-Type: application/json');
echo json_encode($result);
?>