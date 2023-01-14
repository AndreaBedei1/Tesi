-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Gen 13, 2023 alle 22:44
-- Versione del server: 10.4.24-MariaDB
-- Versione PHP: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `logico`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `animali`
--

CREATE TABLE `animali` (
  `Nome` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `animali`
--

INSERT INTO `animali` (`Nome`) VALUES
('Ctenoforo'),
('Delfino'),
('Granchio'),
('Medusa'),
('Tartaruga');

-- --------------------------------------------------------

--
-- Struttura della tabella `avvistamenti`
--

CREATE TABLE `avvistamenti` (
  `ID` int(11) NOT NULL,
  `Data` datetime NOT NULL,
  `Numero_Esemplari` int(11) NOT NULL,
  `Vento` float DEFAULT NULL,
  `Mare` float DEFAULT NULL,
  `Note` text DEFAULT NULL,
  `Latid` varchar(50) NOT NULL,
  `Long` varchar(50) NOT NULL,
  `Utente_ID` int(11) NOT NULL,
  `Anima_Nome` varchar(50) DEFAULT NULL,
  `Specie_Anima_Nome` varchar(50) DEFAULT NULL,
  `Specie_Nome` varchar(50) DEFAULT NULL
) ;

--
-- Dump dei dati per la tabella `avvistamenti`
--

INSERT INTO `avvistamenti` (`ID`, `Data`, `Numero_Esemplari`, `Vento`, `Mare`, `Note`, `Latid`, `Long`, `Utente_ID`, `Anima_Nome`, `Specie_Anima_Nome`, `Specie_Nome`) VALUES
(1, '2022-11-28 10:41:45', 1, 20, 2, 'Questo è un avvistamento di prova!', '41.9109', '12.4818', 1, 'Delfino', 'Delfino', 'Tursiope');

-- --------------------------------------------------------

--
-- Struttura della tabella `coordinate_geografiche`
--

CREATE TABLE `coordinate_geografiche` (
  `Latitudine` varchar(50) NOT NULL,
  `Longitudine` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `coordinate_geografiche`
--

INSERT INTO `coordinate_geografiche` (`Latitudine`, `Longitudine`) VALUES
('41.9109', '12.4818');

-- --------------------------------------------------------

--
-- Struttura della tabella `descrizioni`
--

CREATE TABLE `descrizioni` (
  `Nomenclatura_Binomiale` varchar(50) NOT NULL,
  `Descrizione` text NOT NULL,
  `Dimensione` varchar(50) DEFAULT NULL,
  `Curiosita` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `descrizioni`
--

INSERT INTO `descrizioni` (`Nomenclatura_Binomiale`, `Descrizione`, `Dimensione`, `Curiosita`) VALUES
('Mnemiopsis leidyi', 'La noce di mare (Mnemiopsis leidyi A. Agassiz, 1865) è uno ctenoforo appartenente alla famiglia Bolinopsidae. Al genere Mnemiopsis Agassiz, 1860 appartengono altre due specie: la M. gardeni e la M. mccradyi, con un\'area di distribuzione diversa; l\'opinione più accreditata è però che le tre specie siano forme zoologiche diverse della M. leidyi anche se mostrano un certo polimorfismo dovuto agli adattamenti ambientali', 'Fino 10cm', 'Specie dannosa'),
('Tursiops truncatus', 'Il tursìope (Tursiops truncatus, Montagu, 1821) è un cetaceo odontoceto appartenente alla famiglia dei Delfinidi. È una delle rare specie di delfini che sopportano la cattività; anche a causa di ciò è il più studiato e il più comune nei delfinari. È diffuso in tutti i mari del mondo, ad eccezione delle zone artiche ed antartiche e ne esistono due popolazioni distinte, una costiera ed una di mare aperto. Utilizza per cacciare la tecnica dell\'ecolocalizzazione e si nutre principalmente di pesci. Raggiunge la maturità sessuale intorno ai 12 anni e le femmine partoriscono un solo piccolo. Vive generalmente in branchi formati dalle femmine ed i piccoli, mentre i maschi possono formare delle associazioni chiamate \"alleanze\". A causa dell\'influenza dei media (il famoso delfino della serie televisiva Flipper era un tursiope), è diventato il delfino per antonomasia.', 'Circa 3m', 'Essi comunicano tra loro modulando dei fischi.');

-- --------------------------------------------------------

--
-- Struttura della tabella `esemplari`
--

CREATE TABLE `esemplari` (
  `ID` int(11) NOT NULL,
  `Nome` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `ferite`
--

CREATE TABLE `ferite` (
  `ID` int(11) NOT NULL,
  `Descrizione_Ferita` varchar(100) NOT NULL,
  `Posizione` varchar(50) NOT NULL,
  `Sottoi_ID` int(11) NOT NULL,
  `Gravi_Nome` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `gravita`
--

CREATE TABLE `gravita` (
  `Nome` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `immagini`
--

CREATE TABLE `immagini` (
  `ID` int(11) NOT NULL,
  `Img` blob NOT NULL,
  `Avvis_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `immagini`
--

INSERT INTO `immagini` (`ID`, `Img`, `Avvis_ID`) VALUES
(2, 0x492074726520737472616e6461726420616c6c6120626173652064656c2077656220706f73736f6e6f2065737365726520636f6e7369646572617469206c2748544d4c2c206c277572692065206c27687474702e0d0a0d0a48544d4c3a204573736f20646566696e6973636520696c20666f726d61746f2064656c6c6520706167696e652e204c27487970657254657874204d61726b7570204c616e677561676520c3a820756e206c696e6775616767696f206469206d61726b7570206469207469706f20646573637269747469766f2070726f6765747461746f20706572206c6120666f726d617474617a696f6e65206520696d706167696e617a696f6e6520646920636f6e74656e75746920697065727465737475616c692e204c61207375612073696e746173736920c3a82073746162696c6974612064656c205733432e20492073756f69207072696e636970616c6920656c656d656e746920736f6e6f206d657461646174692c20656c656d656e74692064692073656374696f6e696e672c207461672064692068656164696e672c207068726173696e672c20696e2068746d6c3520616e636865206c612067657374696f6e6520646569206d656469612e0d0a0d0a5552492028556e69666f726d205265736f75726365204964656e746966696572293a205065726d65747465206c2764656e7469666963617a696f6e652064656c6c65207269736f7273652c20696e66617474692061206f676e69207269736f7273612064656c2077656220c3a8206173736f636961746f20756e206964656e746966696361746f726520676c6f62616c6520636869616d61746f207572692e204573736920706f73736f6e6f2065737365726520646566696e69746920636f6d652075726c2c20696c207175616c6520636f6e7469656e6520696e666f726d617a696f6e6920696d6d6564696174616d656e7465207574696c697a7a6162696c692064616c6c61207269736f7273612c206f707075726520636f6d652075726e2069207175616c69207065726d6574746f6e6f20756e27657469636865747461207065726d616e656e74652065206e6f6e207269707564696162696c652064616c6c61207269736f7273612e204c2775726920c3a82064697669736f20696e3a2070726f746f636f6c6c6f3a5b2f617574686f726974795d706174685b3f71756572795d5b23667261676d656e745d2e0d0a496c2070726f746f636f6c6c6f2073706563696669636120696c2070726f746f636f6c6c6f207574696c697a7a61746f28687474702c68747470732c6674702e2e2e292c206c27617574686f7269747920c3a8206c276f7267616e697a7a617a696f6e6520676572617263686963612064656c6c6f207370617a696f20646569206e6f6d692c20696c207061746820c3a8206c61207061727465206964656e746966696361746976612064656c6c61207269736f7273612c206c6120717565727920c3a820756e27756c746572696f7265207370656369666963617a696f6e652c20696c20667261676d656e7420696e6469766964756120756e61207269736f727361207365636f6e64617269612e0d0a0d0a485454502028487970657274657874205472616e736665722050726f746f636f6c293a20452720756e2070726f746f636f6c6c6f20646920636f6d756e6963617a696f6e652065207065726d65747465206469207363616d6269617265206d6573736167676920737520756e61207265746520696e666f726d617469636120636f6d65206164206573656d70696f205443502f49502e20496c2073756f206d6f64656c6c6f2064692066756e7a696f6e616d656e746f20636f6e7369737465206e656c20666174746f2063686520696c20636c69656e7420696e697a69206c27696e746572617a696f6e652e205175696e6469207369207075c3b220646972652063686520696c20636c69656e7420666120696c2070756c6c2064656920646174692e20547574746176696120636920706f73736f6e6f20657373657265206465692063617369207061727469636f6c61726920636f6d65206164206573656d70696f20706572206c27696e7374616e74206d6573736167696e6720646f766520696c2073657276657220696e76696120616c20636c69656e7420656420657365677565206c276f706572617a696f6e6520646920707573682e20556e27657374656e73696f6e652064656c204854545020c3a8206c27485454505320696c207175616c65207065726d65746520646920617665726520756e2063616e616c65206369667261746f20706572206c6f207363616d62696f20646920646174692e, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `sottoimmagini`
--

CREATE TABLE `sottoimmagini` (
  `ID` int(11) NOT NULL,
  `tl_x` decimal(20,3) NOT NULL,
  `tl_y` decimal(20,3) NOT NULL,
  `br_x` decimal(20,3) NOT NULL,
  `br_y` decimal(20,3) NOT NULL,
  `Immag_ID` int(11) NOT NULL,
  `Esemp_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `specie`
--

CREATE TABLE `specie` (
  `Anima_Nome` varchar(50) NOT NULL,
  `Nome` varchar(50) NOT NULL,
  `Nomenclatura_Binomiale` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `specie`
--

INSERT INTO `specie` (`Anima_Nome`, `Nome`, `Nomenclatura_Binomiale`) VALUES
('Ctenoforo', 'Noce di mare', NULL),
('Delfino', 'Comune', NULL),
('Delfino', 'Stenella striata', NULL),
('Delfino', 'Tursiope', NULL),
('Granchio', 'Blu', NULL),
('Granchio', 'Comune', NULL),
('Medusa', 'Medusa cristallo', NULL),
('Medusa', 'Medusa luminosa', NULL),
('Medusa', 'Medusa quadrifoglio', NULL),
('Medusa', 'Polmone di mare', NULL),
('Tartaruga', 'Comune', NULL),
('Tartaruga', 'Liuto', NULL),
('Tartaruga', 'Verde', NULL);

-- --------------------------------------------------------

--
-- Struttura della tabella `utenti`
--

CREATE TABLE `utenti` (
  `ID` int(11) NOT NULL,
  `Nome` varchar(50) DEFAULT NULL,
  `Cognome` varchar(50) DEFAULT NULL,
  `Email` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `utenti`
--

INSERT INTO `utenti` (`ID`, `Nome`, `Cognome`, `Email`, `Password`) VALUES
(1, 'Andrea', 'Bedei', 'andreabedei@libero.it', 'tesi');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `animali`
--
ALTER TABLE `animali`
  ADD PRIMARY KEY (`Nome`);

--
-- Indici per le tabelle `avvistamenti`
--
ALTER TABLE `avvistamenti`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKPosizionato` (`Latid`,`Long`),
  ADD KEY `FKAggiunge` (`Utente_ID`),
  ADD KEY `FKRiferisce` (`Anima_Nome`),
  ADD KEY `FKRiferimento_FK` (`Specie_Anima_Nome`,`Specie_Nome`);

--
-- Indici per le tabelle `coordinate_geografiche`
--
ALTER TABLE `coordinate_geografiche`
  ADD PRIMARY KEY (`Latitudine`,`Longitudine`);

--
-- Indici per le tabelle `descrizioni`
--
ALTER TABLE `descrizioni`
  ADD PRIMARY KEY (`Nomenclatura_Binomiale`);

--
-- Indici per le tabelle `esemplari`
--
ALTER TABLE `esemplari`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `ferite`
--
ALTER TABLE `ferite`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKIdentifica` (`Sottoi_ID`),
  ADD KEY `FKRilevata` (`Gravi_Nome`);

--
-- Indici per le tabelle `gravita`
--
ALTER TABLE `gravita`
  ADD PRIMARY KEY (`Nome`);

--
-- Indici per le tabelle `immagini`
--
ALTER TABLE `immagini`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKInclude` (`Avvis_ID`);

--
-- Indici per le tabelle `sottoimmagini`
--
ALTER TABLE `sottoimmagini`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKContiene` (`Immag_ID`),
  ADD KEY `FKRiconosciuto` (`Esemp_ID`);

--
-- Indici per le tabelle `specie`
--
ALTER TABLE `specie`
  ADD PRIMARY KEY (`Anima_Nome`,`Nome`),
  ADD UNIQUE KEY `FKAttribuita_ID` (`Nomenclatura_Binomiale`);

--
-- Indici per le tabelle `utenti`
--
ALTER TABLE `utenti`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `avvistamenti`
--
ALTER TABLE `avvistamenti`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `esemplari`
--
ALTER TABLE `esemplari`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `ferite`
--
ALTER TABLE `ferite`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `immagini`
--
ALTER TABLE `immagini`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT per la tabella `sottoimmagini`
--
ALTER TABLE `sottoimmagini`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `utenti`
--
ALTER TABLE `utenti`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `avvistamenti`
--
ALTER TABLE `avvistamenti`
  ADD CONSTRAINT `FKAggiunge` FOREIGN KEY (`Utente_ID`) REFERENCES `utenti` (`ID`),
  ADD CONSTRAINT `FKPosizionato` FOREIGN KEY (`Latid`,`Long`) REFERENCES `coordinate_geografiche` (`Latitudine`, `Longitudine`),
  ADD CONSTRAINT `FKRiferimento_FK` FOREIGN KEY (`Specie_Anima_Nome`,`Specie_Nome`) REFERENCES `specie` (`Anima_Nome`, `Nome`),
  ADD CONSTRAINT `FKRiferisce` FOREIGN KEY (`Anima_Nome`) REFERENCES `animali` (`Nome`);

--
-- Limiti per la tabella `ferite`
--
ALTER TABLE `ferite`
  ADD CONSTRAINT `FKIdentifica` FOREIGN KEY (`Sottoi_ID`) REFERENCES `sottoimmagini` (`ID`),
  ADD CONSTRAINT `FKRilevata` FOREIGN KEY (`Gravi_Nome`) REFERENCES `gravita` (`Nome`);

--
-- Limiti per la tabella `immagini`
--
ALTER TABLE `immagini`
  ADD CONSTRAINT `FKInclude` FOREIGN KEY (`Avvis_ID`) REFERENCES `avvistamenti` (`ID`);

--
-- Limiti per la tabella `sottoimmagini`
--
ALTER TABLE `sottoimmagini`
  ADD CONSTRAINT `FKContiene` FOREIGN KEY (`Immag_ID`) REFERENCES `immagini` (`ID`),
  ADD CONSTRAINT `FKRiconosciuto` FOREIGN KEY (`Esemp_ID`) REFERENCES `esemplari` (`ID`);

--
-- Limiti per la tabella `specie`
--
ALTER TABLE `specie`
  ADD CONSTRAINT `FKAttribuita_FK` FOREIGN KEY (`Nomenclatura_Binomiale`) REFERENCES `descrizioni` (`Nomenclatura_Binomiale`),
  ADD CONSTRAINT `FKIndividuata` FOREIGN KEY (`Anima_Nome`) REFERENCES `animali` (`Nome`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
