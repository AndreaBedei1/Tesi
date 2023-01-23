<?php
    define("SITE_ROOT", "../");

    require_once(SITE_ROOT.'bootstrap.php');

    $templateParams["pageid"] = "homepage";
    $templateParams["title"] = "Home";
    $templateParams["javascript"] = array("../templates/main_posts/posts.js", "../templates/header/header.js");
    if(!isUserLoggedIn()){
        header("location: login.php");
    }

    $templateParams["pieces"] = array(
        SITE_ROOT . 'templates/header/header.php',
        SITE_ROOT . 'templates/main_posts/posts.php',
        SITE_ROOT . 'templates/footer/footer.php'
    );

    require_once SITE_ROOT . 'templates/base.php';
?>