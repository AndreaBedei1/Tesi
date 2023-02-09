<?php
    session_start();
    require_once("utils/php/function.php");
    require_once("db/database.php");
    $dbh = new DatabaseHelper("127.0.0.1", "admin", "password", "logico", 3306);
?>
