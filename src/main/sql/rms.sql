-- --------------------------------------------------------
-- Host:                         192.168.99.101
-- Server version:               10.2.9-MariaDB-10.2.9+maria~jessie - mariadb.org binary distribution
-- Server OS:                    debian-linux-gnu
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for rmsdb
CREATE DATABASE IF NOT EXISTS `rmsdb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `rmsdb`;

-- Dumping structure for table rmsdb.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT '0',
  `password` varchar(100) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Dumping data for table rmsdb.user: ~2 rows (approximately)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `user_name`, `password`) VALUES
	(1, 'Alan', '12345'),
	(2, 'John', '12345');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

CREATE TABLE `book` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) NOT NULL,
  `author` varchar(45) NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`book_id`),
  UNIQUE KEY `book_id_UNIQUE` (`book_id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
