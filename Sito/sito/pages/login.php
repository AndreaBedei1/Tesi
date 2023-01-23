<?php
    define("SITE_ROOT", "../");

    require_once(SITE_ROOT.'bootstrap.php');

    $templateParams["pageid"] = "login";
    $templateParams["title"] = "Login";
    $templateParams["javascript"] = array("../templates/main_login/login.js");

    if(isUserLoggedIn()){
        header("location: homepage.php");
    }

    $templateParams["pieces"] = array(
        SITE_ROOT . 'templates/main_login/login.php',
        SITE_ROOT . 'templates/footer/footer.php'
    );

    require_once SITE_ROOT . 'templates/base.php';
?>