<?php
    class DatabaseHelper{
        private $db;

        public function __construct($servername, $username, $password, $dbname, $port){
            $this->db = new mysqli($servername, $username, $password, $dbname, $port);
            if ($this->db->connect_error) {
                die("Connessione al database fallita: " . $this->db->connect_error);
            }        
        }

        public function addUser($nome, $cognome, $pwd, $email, $key){
            $query = 'INSERT INTO `utenti`(`Nome`, `Cognome`, `Email`, `Password`, `Key`) VALUES (?,?,?,?,?)'; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('sssss', $nome, $cognome, $email, $pwd, $key);
            return $stmt->execute();          
        }

        public function checkMailAbsent($mail){
            $query = 'SELECT Email, `Key` FROM utenti WHERE Email=?'; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $mail);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getKey($user){
            $query = "SELECT `Key` FROM utenti WHERE Email=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $user);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function login($mail, $password){
            $query = "SELECT * FROM utenti WHERE Email=? AND `Password`=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $mail, $password);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }
        
        public function checkPwd($mail, $password){
            $query = "SELECT * FROM utenti WHERE Email=? AND `Password`=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $mail, $password);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }
        
        public function changePwd($mail, $password){
            $query = "UPDATE utenti SET `Password`=? WHERE Email=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $password, $mail);
            return $stmt->execute();
        }

        public function getUserInfo($mail){
            $query = 'SELECT `ID`, `Nome`, `Cognome`, `Email` FROM `utenti` WHERE `Email`=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $mail);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function setUserInfo($mail, $nome, $cognome){
            $query = "UPDATE `utenti` SET `Nome`=?, `Cognome`=? WHERE `Email`=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('sss', $nome, $cognome, $mail);
            return $stmt->execute();
        }

        public function getSighting(){
            $query = "SELECT a.*, u.Nome, u.Cognome FROM avvistamenti a JOIN utenti u ON a.Utente_ID=u.ID ORDER BY Data DESC";
            $stmt = $this->db->prepare($query);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getDates($id){
            $query = 'SELECT a.*, u.Nome, u.Cognome FROM avvistamenti a JOIN utenti u ON a.Utente_ID=u.ID WHERE a.ID=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getTypeS(){
            $query = "SELECT *, Nome AS cod_select, Nome AS descr_select FROM animali ";
            $stmt = $this->db->prepare($query);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getTypeSS($sel){
            $query = "SELECT *, Nome AS cod_select, Nome AS descr_select FROM specie WHERE Anima_Nome=?";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $sel);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function updateDates($id, $specie, $sottospecie, $esemplari, $vento, $mare, $note){
            if($specie==""){
                $query = "UPDATE `avvistamenti` SET `Numero_Esemplari`=?,`Vento`=?,`Mare`=?,`Note`=?, `Anima_Nome`=NULL,`Specie_Anima_Nome`=NULL,`Specie_Nome`=NULL WHERE `ID`=?"; 
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('sssss', $esemplari, $vento, $mare, $note, $id);
            } else if($specie!="" && $sottospecie==""){
                $query = "UPDATE `avvistamenti` SET `Numero_Esemplari`=?,`Vento`=?,`Mare`=?,`Note`=?, `Anima_Nome`=?,`Specie_Anima_Nome`=NULL,`Specie_Nome`=NULL WHERE `ID`=?"; 
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('ssssss', $esemplari, $vento, $mare, $note, $specie, $id);
            } else {
                $query = "UPDATE `avvistamenti` SET `Numero_Esemplari`=?,`Vento`=?,`Mare`=?,`Note`=?, `Anima_Nome`=?,`Specie_Anima_Nome`=?,`Specie_Nome`=? WHERE `ID`=?";
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('ssssssss', $esemplari, $vento, $mare, $note, $specie, $specie, $sottospecie, $id);
            }
            return $stmt->execute();
        }

        public function deleteAvv($id){
            $query = "DELETE FROM `avvistamenti` WHERE `ID`=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            return $stmt->execute();
        }

        public function addCoord($lat, $lon){
            $query = "INSERT INTO `coordinate_geografiche`(`Latitudine`, `Longitudine`) VALUES (?,?)"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $lat, $lon);
            return $stmt->execute();
        }

        public function saveAvv($userID, $data, $esemplari, $latitudine, $longitudine, $specie, $sottospecie, $mare, $vento, $note){
            if($specie==""){
                $query = 'INSERT INTO `avvistamenti`(`Data`, `Numero_Esemplari`, `Vento`, `Mare`, `Note`, `Latid`, `Long`, `Utente_ID`, `Anima_Nome`, `Specie_Anima_Nome`, `Specie_Nome`) VALUES (?,?,?,?,?,?,?,?,NULL,NULL,NULL)';
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('ssssssss', $data, $esemplari, $mare, $vento, $note, $latitudine, $longitudine, $userID);
            } else if($specie!="" && $sottospecie==""){
                $query ='INSERT INTO `avvistamenti`(`Data`, `Numero_Esemplari`, `Vento`, `Mare`, `Note`, `Latid`, `Long`, `Utente_ID`, `Anima_Nome`, `Specie_Anima_Nome`, `Specie_Nome`) VALUES (?,?,?,?,?,?,?,?,?,NULL,NULL)';
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('sssssssss', $data, $esemplari, $mare, $vento, $note, $latitudine, $longitudine, $userID, $specie);
            } else {
                $query ='INSERT INTO `avvistamenti`(`Data`, `Numero_Esemplari`, `Vento`, `Mare`, `Note`, `Latid`, `Long`, `Utente_ID`, `Anima_Nome`, `Specie_Anima_Nome`, `Specie_Nome`) VALUES (?,?,?,?,?,?,?,?,?,?,?)';
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('sssssssssss', $data, $esemplari, $mare, $vento, $note, $latitudine, $longitudine, $userID, $specie, $specie, $sottospecie);
            }
            return $stmt->execute();
        }

        public function addImage($id, $str){
            $query = "INSERT INTO `immagini`(`Img`, `Avvis_ID`) VALUES (?,?)"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $str, $id);
            return $stmt->execute();
        }

        public function getImages($id){
            $query = 'SELECT `ID`, `Img` FROM `immagini` WHERE `Avvis_ID`=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function infoSpecie($animale, $specie){
            $query = 'SELECT * FROM `descrizioni` d INNER JOIN specie s ON d.`Nomenclatura_Binomiale`= s.`Nomenclatura_Binomiale` WHERE s.Anima_Nome=? AND s.Nome=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $animale, $specie);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getSottoimmagini($id){
            $query = 'SELECT * FROM `sottoimmagini` s INNER JOIN `esemplari` e ON s.`Esemp_ID`= e.`ID`  WHERE `Immag_ID`=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }
    }
?>
