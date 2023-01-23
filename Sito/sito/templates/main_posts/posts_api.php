<?php

require("../../bootstrap.php");

$result = array();
if(isset($_POST["request"])){
    switch ($_POST["request"]) {
        
    }
}


header('Content-Type: application/json');
echo json_encode($result);


?>