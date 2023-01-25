<?php
    class DatabaseHelper{
        private $db;

        public function __construct($servername, $username, $password, $dbname, $port){
            $this->db = new mysqli($servername, $username, $password, $dbname, $port);
            if ($this->db->connect_error) {
                die("Connessione al database fallita: " . $this->db->connect_error);
            }        
        }

        #Aggiungere funzone che cripta la password
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

        // FIno a qui corretto!!!!!!
        public function getPostType(){
            $query = "SELECT Nome AS cod_select, Nome AS descr_select FROM TipologiaPost ";
            $stmt = $this->db->prepare($query);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function addPost($utente, $testo, $tipo, $titolo, $date, $media=""){
            $query = "INSERT INTO Post (Utente, Tipologia, Media, Testo, Data, Titolo) VALUES (?,?,?,?,?,?)"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ssssss', $utente, $tipo, $media, $testo, $date, $titolo);
            return $stmt->execute();          
        }

        public function getEmail($user){
            $query = "SELECT Mail FROM Credenziali WHERE Utente=?";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $user);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getInterests(){
            $query = "SELECT Nome FROM Interesse";
            $stmt = $this->db->prepare($query);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function addInterests($user, $interest){
            $query = "INSERT INTO Preferenza (InterNome, Username) VALUES (?,?) "; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $interest, $user);
            return $stmt->execute();      
        }

        public function getInterestsUser($user){
            $query = "SELECT InterNome FROM Preferenza WHERE Username=?";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $user);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function deleteInterests($user){
            $query = "DELETE FROM Preferenza WHERE Username=?";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $user);
            return $stmt->execute();
        }

        public function getPosts($user, $nPost, $limit){
            $query = 'SELECT p.*, u.Immagine, r.* FROM Post p JOIN Utente u ON p.Utente=u.Username LEFT JOIN Reazione r ON (p.ID=r.PostID AND (r.Username=? OR r.Username IS NULL)) ORDER BY Data DESC LIMIT ?, ?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('sss', $user, $nPost, $limit);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getUserPosts($nPost, $limit, $id){
            $query = 'SELECT p.*, u.Immagine, r.* FROM Post p JOIN Utente u ON p.Utente=u.Username LEFT JOIN Reazione r ON (p.ID=r.PostID AND (r.Username=? OR r.Username IS NULL)) WHERE Utente=? ORDER BY Data DESC LIMIT ?, ?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ssss', $id, $id, $nPost, $limit);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getPostsFollow($nPost, $limit, $id){
            $query = 'SELECT p.*, u.Immagine, r.* FROM Post p JOIN Utente u ON p.Utente=u.Username LEFT JOIN Reazione r ON (p.ID=r.PostID AND (r.Username=? OR r.Username IS NULL)) WHERE p.Utente in (SELECT Seguito FROM Follower WHERE Seguace=?) ORDER BY Data DESC LIMIT ?, ?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ssss', $id, $id, $nPost, $limit);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getSelectedPost($id){
            $query = 'SELECT Utente, Titolo FROM Post WHERE ID=?';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $id);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }



        public function changeProfile($img, $user)
        {
            $query = "UPDATE Utente SET Immagine=? WHERE Username=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $img, $user);
            return $stmt->execute();
        }

        public function addComment($utente, $post, $testo, $data){
            $query = "INSERT INTO Commento (Testo, Data, Post, Utente) VALUES (?, ?, ?, ?)"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ssss', $testo, $data, $post, $utente);
            return $stmt->execute();          
        }

        public function getComments($idPost){
            $query = 'SELECT c.*, u.Immagine FROM Commento c JOIN Utente u ON c.Utente=u.Username  WHERE Post=? ORDER BY Data';
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("s", $idPost);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getReactions($id){
            $query = "SELECT p.Titolo, p.Utente, PostID, SUM(CASE WHEN `Like` = 1 THEN 1 ELSE 0 END) AS NumLike, SUM(CASE WHEN Dislike = 1 THEN 1 ELSE 0 END) AS NumDislike FROM Reazione r JOIN Post p ON r.PostID=p.ID  WHERE PostId = ? GROUP BY PostId";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("s", $id);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }
        
        public function getNumberComment($id){
            $query = "SELECT COUNT(*) AS commenti FROM Commento WHERE Post=?";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("s", $id);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function checkReactions($user, $id){
            $query = "SELECT * FROM `Reazione` WHERE `PostID`=? AND `Username`=?";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("ss", $id, $user);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function addReaction($user, $id, $like, $dislike){
            $query = "INSERT INTO `Reazione`(`PostID`, `Username`, `Dislike`, `Like`) VALUES (?, ?, ?, ?)"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ssss', $id, $user, $dislike, $like);
            return $stmt->execute();     
        }

        public function deleteReaction($user, $id){
            $query = "DELETE FROM `Reazione` WHERE PostId=? AND Username=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ss', $id, $user);
            return $stmt->execute();     
        }

        public function switchReaction($user, $post, $like, $dislike){
            $query = "UPDATE `Reazione` SET `Dislike`=?,`Like`=? WHERE PostId=? AND Username=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('ssss', $dislike, $like, $post, $user);
            return $stmt->execute();    
        }

        public function getFollower($user){
            $query = "SELECT Seguace AS Amico, Immagine FROM Follower f JOIN Utente u ON f.Seguace=u.Username WHERE Seguito=?";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("s", $user);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getFollow($user){
            $query = "SELECT Seguito AS Amico, Immagine FROM Follower f JOIN Utente u ON f.Seguito=u.Username WHERE Seguace=?";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("s", $user);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function checkFollow($friend, $me){
            $query = "SELECT * FROM Follower WHERE Seguace=? AND Seguito=?";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("ss", $me, $friend);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function removeFollow($me, $friend){
            $query = "DELETE FROM Follower WHERE Seguito=? AND Seguace=?";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("ss", $friend, $me);
            return $stmt->execute();
        }

        public function addFollow($me, $friend){
            $query = "INSERT INTO Follower(Seguito, Seguace) VALUES (?,?)";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("ss", $friend, $me);
            return $stmt->execute();
        }

        public function addNotification($utente, $tipologia, $testo, $visualizzata, $data, $creatore){
            $query = "INSERT INTO Notifica(Testo, Data, Visualizzata, Utente, Tipologia, Creatore) VALUES (?,?,?,?,?,?)";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("ssssss", $testo, $data, $visualizzata, $utente, $tipologia, $creatore);
            return $stmt->execute();
        }

        public function getNotifications($user){
            $query = "SELECT * FROM Notifica JOIN Utente ON Creatore=Username  WHERE Utente=? ORDER BY Data DESC";
            $stmt = $this->db->prepare($query);
            $stmt->bind_param("s", $user);
            $stmt->execute();
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function setOld($user){
            $query = 'UPDATE Notifica SET Visualizzata="1" WHERE Utente=?'; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $user);
            return $stmt->execute(); 
        }

        public function delateNotifications($user){
            $query = "DELETE FROM Notifica WHERE Utente=?"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $user);
            return $stmt->execute();     
        }

        public function checkNotificPresent($user){
            $query = "SELECT count(*) AS Numero FROM Notifica WHERE Utente=? AND Visualizzata=0"; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $user);
            $stmt->execute(); 
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }

        public function getResultSearch($date){
            $stt = "%$date%";
            $query = "SELECT Username, Immagine FROM Utente WHERE Username LIKE ? "; 
            $stmt = $this->db->prepare($query);
            $stmt->bind_param('s', $stt);
            $stmt->execute(); 
            return $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
        }
    }
?>
