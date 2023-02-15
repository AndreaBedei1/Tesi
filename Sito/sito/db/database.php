<?php
    class DatabaseHelper{
        private $db;

        public function __construct($servername, $username, $password, $dbname, $port){
            $this->db = new mysqli($servername, $username, $password, $dbname, $port);
            mysqli_set_charset($this->db , 'utf8mb4');
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
            $query = 'SELECT a.*, u.Nome, u.Cognome, u.Email FROM avvistamenti a JOIN utenti u ON a.Utente_ID=u.ID WHERE a.Eliminato<>"1" ORDER BY Data DESC';
            $stmt = $this->db->prepare($query);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getDates($id){
            $query = 'SELECT a.*, u.Nome, u.Cognome, u.Email FROM avvistamenti a JOIN utenti u ON a.Utente_ID=u.ID WHERE a.ID=?';
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

        public function updateDates($id, $specie, $sottospecie, $esemplari, $vento, $mare, $note, $lat, $long){
            if($specie==""){
                $query = "UPDATE `avvistamenti` SET `Numero_Esemplari`=?,`Vento`=?,`Mare`=?,`Note`=?, `Anima_Nome`=NULL,`Specie_Anima_Nome`=NULL,`Specie_Nome`=NULL, `Latid`=?, `Long`=?  WHERE `ID`=?"; 
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('sssssss', $esemplari, $vento, $mare, $note, $lat, $long, $id);
            } else if($specie!="" && $sottospecie==""){
                $query = "UPDATE `avvistamenti` SET `Numero_Esemplari`=?,`Vento`=?,`Mare`=?,`Note`=?, `Anima_Nome`=?,`Specie_Anima_Nome`=NULL,`Specie_Nome`=NULL, `Latid`=?, `Long`=? WHERE `ID`=?"; 
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('ssssssss', $esemplari, $vento, $mare, $note, $specie, $lat, $long, $id);
            } else {
                $query = "UPDATE `avvistamenti` SET `Numero_Esemplari`=?,`Vento`=?,`Mare`=?,`Note`=?, `Anima_Nome`=?,`Specie_Anima_Nome`=?,`Specie_Nome`=?, `Latid`=?, `Long`=? WHERE `ID`=?";
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('ssssssssss', $esemplari, $vento, $mare, $note, $specie, $specie, $sottospecie, $lat, $long, $id);
            }
            return $stmt->execute();
        }

        public function deleteAvv($id){
            $query = 'UPDATE `avvistamenti` SET `Eliminato`="1" WHERE `ID`=?'; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            return $stmt->execute();
        }

        public function saveAvv($userID, $data, $esemplari, $latitudine, $longitudine, $specie, $sottospecie, $mare, $vento, $note){
            if($specie==""){
                $query = 'INSERT INTO `avvistamenti`(`Data`, `Numero_Esemplari`, `Vento`, `Mare`, `Note`, `Latid`, `Long`, `Utente_ID`, `Anima_Nome`, `Specie_Anima_Nome`, `Specie_Nome`) VALUES (?,?,?,?,?,?,?,?,NULL,NULL,NULL)';
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('ssssssss', $data, $esemplari, $vento, $mare, $note, $latitudine, $longitudine, $userID);
            } else if($specie!="" && $sottospecie==""){
                $query ='INSERT INTO `avvistamenti`(`Data`, `Numero_Esemplari`, `Vento`, `Mare`, `Note`, `Latid`, `Long`, `Utente_ID`, `Anima_Nome`, `Specie_Anima_Nome`, `Specie_Nome`) VALUES (?,?,?,?,?,?,?,?,?,NULL,NULL)';
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('sssssssss', $data, $esemplari, $vento, $mare, $note, $latitudine, $longitudine, $userID, $specie);
            } else {
                $query ='INSERT INTO `avvistamenti`(`Data`, `Numero_Esemplari`, `Vento`, `Mare`, `Note`, `Latid`, `Long`, `Utente_ID`, `Anima_Nome`, `Specie_Anima_Nome`, `Specie_Nome`) VALUES (?,?,?,?,?,?,?,?,?,?,?)';
                $stmt = $this->db->prepare($query);
                $stmt->bind_param('sssssssssss', $data, $esemplari, $vento, $mare, $note, $latitudine, $longitudine, $userID, $specie, $specie, $sottospecie);
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
            $cm = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
            return $cm;
        }

        public function getSottoimmagini($id){
            $query = 'SELECT s.*, e.`nome` FROM `sottoimmagini` s INNER JOIN `esemplari` e ON s.`Esemp_ID`= e.`ID`  WHERE `Immag_ID`=? ORDER BY  s.`ID` ';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getFerite($id, $img){
            $query = 'SELECT `ID` AS ID_Fer, `Descrizione_Ferita`, `Posizione`, `Sottoi_ID`, `Gravi_Nome` FROM `ferite` WHERE Sottoi_ID=? AND Img_rif=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $id, $img);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getGravita(){
            $query = 'SELECT Nome AS cod_select,  Nome AS descr_select FROM `gravita` ORDER BY Gravita ';
            $stmt = $this->db->prepare($query);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function addEsempl($id, $nome){
            $query = 'INSERT INTO `esemplari`(`ID`, `Nome`) VALUES (?,?)';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $id, $nome);
            return $stmt->execute();
        }

        public function checkID($p, $img){
            $query = 'SELECT * FROM `sottoimmagini` WHERE `Immag_ID`=? AND `ID`=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $img, $p);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function addSottoimmagine($tx, $ty, $bx, $by, $img, $esem, $p){
            $query = 'INSERT INTO `sottoimmagini`(`tl_x`, `tl_y`, `br_x`, `br_y`, `Immag_ID`, `Esemp_ID`, `ID`) VALUES (?,?,?,?,?,?,?)';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("sssssss", $tx, $ty, $bx, $by, $img, $esem, $p);
            return $stmt->execute();
        }

        public function deleteFerite($p, $img){
            $query = 'DELETE FROM `ferite` WHERE `Sottoi_ID`=? AND `Img_rif`=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $p, $img);
            $stmt->execute();
        }

        public function deleteFeritaByID($id){
            $query = 'DELETE FROM `ferite` WHERE `ID`=? ';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            $stmt->execute();
        }

        public function deleteSottoimmagini($p, $img){
            $query = 'DELETE FROM `sottoimmagini` WHERE `ID`=? AND `Immag_ID`=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $p, $img);
            $stmt->execute();
        }

        public function deleteIndiv($id){
            $query = 'DELETE FROM `esemplari` WHERE `ID`=? AND `Nome`<>"Sconosciuto" ';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            $stmt->execute();
        }

        public function deleteImmagini($id){
            $query = 'DELETE FROM `immagini` WHERE `ID`=? ';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            $stmt->execute();
        }
        
        public function getInjury($id){
            $query = 'SELECT * FROM `ferite` WHERE `ID`=? ';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function updateInjury($id, $pos, $grav, $desc){
            $query = "UPDATE `ferite` SET `Descrizione_Ferita`=?, `Posizione`=?, `Gravi_Nome`=? WHERE `ID`=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ssss', $desc, $pos, $grav, $id);
            return $stmt->execute();
        }

        public function addInjury($img, $sottImg, $pos, $grav, $desc){
            $query = "INSERT INTO `ferite`(`Descrizione_Ferita`, `Posizione`, `Img_rif`, `Sottoi_ID`, `Gravi_Nome`) VALUES (?,?,?,?,?)"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('sssss', $desc, $pos, $img, $sottImg, $grav);
            return $stmt->execute();
        }

        public function getEsem(){
            $query = 'SELECT CONCAT(`ID`, "-", `Nome`) AS descr_select,  `ID` AS cod_select FROM `esemplari`';
            $stmt = $this->db->prepare($query);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function updateEsempl($nome, $img, $id){
            $query = "UPDATE `sottoimmagini` SET `Esemp_ID`=? WHERE `Immag_ID`=? AND `ID`=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('sss', $nome, $img, $id);
            return $stmt->execute();
        }

        public function checkEsempID($id){
            $query = 'SELECT `ID` FROM `esemplari` WHERE `ID`=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function resetPwd($email, $pwd){
            $query = 'UPDATE `utenti` SET `Password`=? WHERE `Email`=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $pwd, $email);
            return $stmt->execute();
        }
    }
?>
