<?php
    define("SITE_ROOT", "../");

    require_once(SITE_ROOT.'bootstrap.php');

    $templateParams["pageid"] = "signup";
    $templateParams["title"] = "Iscrizione";
    $templateParams["javascript"] = array("../templates/main_signup/signup.js");

    if(isUserLoggedIn()){
        header("location: homepage.php");
    }

    $templateParams["pieces"] = array(
        SITE_ROOT . 'templates/main_signup/signup.php',
        SITE_ROOT . 'templates/footer/footer.php'
    );

    require_once SITE_ROOT . 'templates/base.php';
?>