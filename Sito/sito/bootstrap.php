<?php
    session_start();
    require_once("utils/php/function.php");
    require_once("db/database.php");
    $dbh = new DatabaseHelper("127.0.0.1", "root", "", "logico", 3306);

// define('__ROOT__', dirname(__FILE__)); 
?>