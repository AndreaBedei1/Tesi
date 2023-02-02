<?php
    use PHPMailer\PHPMailer\PHPMailer;
    use PHPMailer\PHPMailer\SMTP;
    use PHPMailer\PHPMailer\Exception;
    require '/var/www/html/vendor/autoload.php';

    function ora($ore)
    {
        if(!empty($ore))
        {
            $ore = substr($ore,0,5);
        }

        return $ore;
    }

    function dataIT($data)
    {
        if(!empty($data))
        {
            $data = substr($data,8,2)."/".substr($data,5,2)."/".substr($data,0,4);
        }

        return $data;
    }

    function dataoraIT($data)
    {
        if(!empty($data))
        {
            $data = substr($data,8,2)."/".substr($data,5,2)."/".substr($data,0,4)." ".substr($data,11,5);
        }

        return $data;
    }

    function dataUSA($data)
    {
        if(!empty($data))
        {
            $data = substr($data,6,4)."-".substr($data,3,2)."-".substr($data,0,2);
        }

        return $data;
    }

    function dataoraUSA($data)
    {
        if(!empty($data))
        {
            $data = substr($data,6,4)."-".substr($data,3,2)."-".substr($data,0,2)." ".substr($data,11,7);
        }

        return $data;
    }

    function today()
    {
        return date("Y-m-d H:i:s");
    }

    function isActive($pagename){
        if(basename($_SERVER['PHP_SELF'])==$pagename){
            echo " class='active' ";
        }
    }

    function getIdFromName($name){
        return preg_replace("/[^a-z]/", '', strtolower($name));
    }

    function isUserLoggedIn(){
        return !empty($_SESSION['user']);
    }

    function registerLoggedUser($user){
        $_SESSION["user"] = $user;
    }

    function addFile($file){
        try {
            if(!empty($file)){
                if (!isset($file['error']) || is_array($file['error'])
                ) {
                    throw new RuntimeException('Parametri non validi.');
                }
        
                switch ($file['error']) {
                    case UPLOAD_ERR_OK:
                        break;
                    case UPLOAD_ERR_NO_FILE:
                        throw new RuntimeException('File non inviato.');
                    case UPLOAD_ERR_INI_SIZE:
                    case UPLOAD_ERR_FORM_SIZE:
                        throw new RuntimeException('File troppo grande.');
                    default:
                        throw new RuntimeException('Errore sconosciuto.');
                }
        
                if ($file['size'] > 10*1024*1024) {
                    throw new RuntimeException('Superate le dimensioni massime.: '.$file['size']);
                }
        
                $finfo = new finfo(FILEINFO_MIME_TYPE);
                if (false === $ext = array_search(
                    $finfo->file($file['tmp_name']),
                    array(
                        'jpg' => 'image/jpeg',
                        'png' => 'image/png',
                        'gif' => 'image/gif',
                    ),
                    true
                )) {
                    throw new RuntimeException('Formato non valido.');
                }
        
                $file_name=uniqid();
                if (!move_uploaded_file(
                    $file['tmp_name'],
                    sprintf('../../../img/avvistamenti/%s.%s',
                        $file_name,
                        $ext
                    )
                )) {
                    throw new RuntimeException('Fallito il caricamento del file.');
                }
                $file_name .= ".".$ext;
            } else {
                $file_name = "";
            }
            $result["file"] = $file_name;
            $result["errore"]="";
            return $result;
        } catch (RuntimeException $e) {
            $result["errore"]=$e->getMessage();
            return $result;
        }
    }

    function sendEmail($email, $testo){
        $codice = '<!DOCTYPE html><html lang="it"><head><title>Reset della password</title><style>.container{width:500px;margin:auto;}h1{text-align:center;}.form{width:100%;padding:20px;border:1px solid #ddd;box-sizing:border-box;text-align:center;}.password-field{width:100%;padding:10px;margin-bottom:20px;box-sizing:border-box;font-size:16px;border:1px solid #ddd;text-align:center;}p{font-size:18px;margin-bottom:20px;}</style></head><body><div class="container"><h1>Reset della password</h1><div class="form"><p>Benvenuto! La tua nuova password e\':</p><div class="password-field"><p>'.$testo.'</p></div></div></div></body></html>';
        $mail = new PHPMailer(true);

        $handle = fopen("../../../credenziali.txt", "r");
        if ($handle) {
            $username = rtrim(fgets($handle));
            $password = rtrim(fgets($handle));
            fclose($handle);
        }
        try {
            //Server settings
            $mail->isSMTP();
            $mail->Host       = 'smtp.libero.it';
            $mail->SMTPAuth   = true;
            $mail->Username   = $username;
            $mail->Password   = $password;
            $mail->SMTPSecure = PHPMailer::ENCRYPTION_SMTPS;
            $mail->Port       = 465;

            $mail->setFrom('seaDetector@libero.it', 'seaDetector');
            $mail->addAddress($email, "Egregio Ricercatore");

            //Content
            $mail->isHTML(true);
            $mail->Subject = 'Reset Password!';
            $mail->Body    = $codice;
            $mail->send();
        } catch (Exception $ignored) {
        }
    }

    function HmacSHA512($data, $key) {
        return hash_hmac('sha512', $data, $key);
    }
?>