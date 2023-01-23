<?php
    define("SITE_ROOT", "/var/www/html/ElaboratoTW/sito/");

    require_once(SITE_ROOT.'bootstrap.php');

    $templateParams["pageid"] = "privacy";
    $templateParams["title"] = "Uv-Privacy";

    $templateParams["javascript"] = array("../templates/header/header.js");

    $templateParams["pieces"] = array(
        SITE_ROOT . 'templates/header/header.php',
        SITE_ROOT . 'templates/main_privacy/informativa.php',
        SITE_ROOT . 'templates/footer/footer.php'
    );
    require_once SITE_ROOT . 'templates/base.php';
?>