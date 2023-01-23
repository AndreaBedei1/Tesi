<?php

use LDAP\Result;

require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]) {
        case 'datiTipologie':
            $rec = $dbh->getPostType();
            $result = $rec;
            break;
        case 'getPosts':
            if(isset($_POST["numeroPost"]) && isset($_POST["utente"]) && isset($_POST["checked"]) && isUserLoggedIn()){
                $nPost = $_POST["numeroPost"];
                $utente = $_POST["utente"];
                $checked = $_POST["checked"];
                if($utente=="" && $checked=="false"){
                    $posts = $dbh->getPosts($_SESSION["user"], $nPost, 5);
                } else if($checked=="true"){
                    $posts = $dbh->getPostsFollow($nPost, 5, $_SESSION["user"]);
                }
                else{
                    $posts = $dbh->getUserPosts($nPost, 5, $utente);
                }
                for($i=0; $i<count($posts); $i++){
                    if($posts[$i]["Like"] == 1) {
                        $posts[$i]["post_reaction"] = 1;   // like
                    } else if ($posts[$i]["Dislike"] == 1) {
                        $posts[$i]["post_reaction"] = -1;  // dislike
                    } else {
                        $posts[$i]["post_reaction"] = 0;  // dislike
                    }
                    $posts[$i]['Data']=dataoraIT($posts[$i]['Data']);
                    $posts[$i]["reactions"]=$dbh->getReactions($posts[$i]["ID"]);
                    $posts[$i]["commenti"]=$dbh->getNumberComment($posts[$i]["ID"])[0]["commenti"];
                }
                $result["posts"] = $posts;
            }
            break;
        case 'aggiungiCommento':
            if(isset($_POST["nPost"]) && isset($_POST["testo"]) && isUserLoggedIn()){
                $dbh->addComment($_SESSION['user'], $_POST["nPost"], $_POST["testo"], today());
                $result["dati"] = $dbh->getSelectedPost($_POST["nPost"]);
                $result["commenti"]=$dbh->getNumberComment($_POST["nPost"])[0]["commenti"];
            }
            break;
        case 'aggiungiReaction':
            if(isset($_POST["nPost"]) && isset($_POST["like"]) && isset($_POST["dislike"]) && isset($_POST["type"])  && isUserLoggedIn()){
                $risultato=$dbh->checkReactions($_SESSION['user'], $_POST["nPost"]);
                if(empty($risultato)){
                    $dbh->addReaction($_SESSION['user'], $_POST["nPost"], $_POST["like"], $_POST["dislike"]);
                    if($_POST["like"] == 1) {
                        $result["post_reaction"] = 1;   // like
                    } else if ($_POST["dislike"] == 1) {
                        $result["post_reaction"] = -1;  // dislike
                    } else {
                        $result["post_reaction"] = 0;
                    }
                }
                else {
                    if($risultato[0][$_POST["type"]]==1){
                        $dbh->deleteReaction($_SESSION['user'], $_POST["nPost"]);
                        $result["post_reaction"] = 0;
                    }
                    else{
                        $dbh->switchReaction($_SESSION['user'], $_POST["nPost"], $_POST["like"], $_POST["dislike"]);
                        
                        if($_POST["like"] == 1) {
                            $result["post_reaction"] = 1;   // like
                        } else if ($_POST["dislike"] == 1) {
                            $result["post_reaction"] = -1;  // dislike
                        } else {
                            $result["post_reaction"] = 0;
                        }
                    }
                }
                $result["reactions"]=$dbh->getReactions($_POST["nPost"]);
            }
            break;

        case 'commentiPost':
            if(isset($_POST["idPost"]) && isUserLoggedIn()){
                $result = $dbh->getComments($_POST['idPost']);
                for($i=0; $i<count($result); $i++){
                    $result[$i]['Data']=dataoraIT($result[$i]['Data']);
                }
            }
            break;
        case 'getUserInfo':
            if(isUserLoggedIn()){
                $result["dati"] = $dbh->getUserInfo($_SESSION['user']);
            }
            break;
        case 'nuovoPost':
            if(isset($_POST["testo"]) && isset($_POST["tipo"]) && isset($_POST["titolo"])  && isUserLoggedIn()){
                $rs_file = $rs_file = addFile($_FILES["file"], "post_img");
                if($rs_file["errore"]=="")
                    $postResult = $dbh->addPost($_SESSION["user"], $_POST["testo"], $_POST["tipo"], $_POST["titolo"], today(), $rs_file["file"]);
                    if($postResult){
                        $result["state"]=true;
                    }
                else {
                    $result["state"]=false;
                    $result["msg"]="Errore caricamento foto, ".$rs_file["errore"];
                }
                break;
            } else {
                $result["state"]=false;
                $result["msg"]="Post non inserito, errore nei dati inseriti!";
            }
    }
}


header('Content-Type: application/json');
echo json_encode($result);


?>