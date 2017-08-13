/*
SQLyog Community Edition- MySQL GUI v8.05 
MySQL - 5.5.46-0ubuntu0.14.04.2 : Database - puzzlebobble
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`puzzlebobble` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `puzzlebobble`;

/*Table structure for table `bubblepowers` */

DROP TABLE IF EXISTS `bubblepowers`;

CREATE TABLE `bubblepowers` (
  `bubblePowerId` int(11) NOT NULL AUTO_INCREMENT,
  `isDroppable` tinyint(1) NOT NULL DEFAULT '0',
  `isProjectile` tinyint(1) NOT NULL DEFAULT '0',
  `activationType` varchar(30) DEFAULT NULL,
  `timeConstraint` int(11) DEFAULT NULL,
  `gameParameterId` int(11) DEFAULT NULL,
  PRIMARY KEY (`bubblePowerId`),
  KEY `FK_bubblepowersGameParameterId` (`gameParameterId`),
  CONSTRAINT `FK_bubblepowersGameParameterId` FOREIGN KEY (`gameParameterId`) REFERENCES `gameparameter` (`gameParameterId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `bubblepowers` */

insert  into `bubblepowers`(`bubblePowerId`,`isDroppable`,`isProjectile`,`activationType`,`timeConstraint`,`gameParameterId`) values (1,1,0,NULL,NULL,NULL),(2,0,0,NULL,NULL,NULL),(3,0,1,NULL,NULL,NULL);

/*Table structure for table `bubbles` */

DROP TABLE IF EXISTS `bubbles`;

CREATE TABLE `bubbles` (
  `bubbleGameId` int(11) NOT NULL,
  `bubbleName` varchar(20) NOT NULL,
  `bubbleScore` int(11) NOT NULL,
  `image` varchar(100) NOT NULL,
  `bubblePowerId` int(11) DEFAULT NULL,
  PRIMARY KEY (`bubbleGameId`),
  KEY `FK_bubblesBubblePower` (`bubblePowerId`),
  CONSTRAINT `FK_bubblesBubblePower` FOREIGN KEY (`bubblePowerId`) REFERENCES `bubblepowers` (`bubblePowerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `bubbles` */

insert  into `bubbles`(`bubbleGameId`,`bubbleName`,`bubbleScore`,`image`,`bubblePowerId`) values (1,'Orange',5,'file:../../assets/images/bubbles/spaceBall-2.png',NULL),(2,'Purple',5,'file:../../assets/images/bubbles/spaceBall-3.png',NULL),(3,'Blue',5,'file:../../assets/images/bubbles/spaceBall-4.png',NULL),(4,'Green',5,'file:../../assets/images/bubbles/spaceBall-5.png',NULL),(5,'Red',5,'file:../../assets/images/bubbles/spaceBall-6.png',NULL),(6,'Color Blind',10,'file:../../assets/images/bubbles/colorblind.png',1),(7,'More chances',10,'file:../../assets/images/bubbles/plusfive.png',1),(8,'Less Choice',10,'file:../../assets/images/bubbles/limited.png',1),(9,'Fixed',10,'file:../../assets/images/bubbles/sticky.png',2),(10,'Bomb',10,'file:../../assets/images/bubbles/bomb.png',3),(11,'Rainbow',10,'file:../../assets/images/bubbles/rainbow-01.png',3);

/*Table structure for table `difficulties` */

DROP TABLE IF EXISTS `difficulties`;

CREATE TABLE `difficulties` (
  `difficultyId` int(11) NOT NULL AUTO_INCREMENT,
  `difficultyName` varchar(20) NOT NULL,
  PRIMARY KEY (`difficultyId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `difficulties` */

insert  into `difficulties`(`difficultyId`,`difficultyName`) values (1,'Easy'),(2,'Medium'),(3,'Hard');

/*Table structure for table `gameparameter` */

DROP TABLE IF EXISTS `gameparameter`;

CREATE TABLE `gameparameter` (
  `gameParameterId` int(1) NOT NULL AUTO_INCREMENT,
  `parameterId` int(11) NOT NULL,
  `value` double NOT NULL,
  `difficultyId` int(11) DEFAULT NULL,
  PRIMARY KEY (`gameParameterId`),
  KEY `FK_gameparameterParameter` (`parameterId`),
  CONSTRAINT `FK_gameparameterParameter` FOREIGN KEY (`parameterId`) REFERENCES `parameters` (`parameterId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `gameparameter` */

insert  into `gameparameter`(`gameParameterId`,`parameterId`,`value`,`difficultyId`) values (1,1,20,1),(2,1,30,2),(3,1,50,3);

/*Table structure for table `levels` */

DROP TABLE IF EXISTS `levels`;

CREATE TABLE `levels` (
  `levelNumber` int(11) NOT NULL,
  `levelName` varchar(25) NOT NULL,
  `levelSeed` varchar(113) NOT NULL,
  `parameterId` int(11) DEFAULT NULL,
  PRIMARY KEY (`levelNumber`),
  KEY `FK_levels` (`parameterId`),
  CONSTRAINT `FK_levels` FOREIGN KEY (`parameterId`) REFERENCES `parameters` (`parameterId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `levels` */

insert  into `levels`(`levelNumber`,`levelName`,`levelSeed`,`parameterId`) values (1,'Level 1','63373293394060945832105228075356681',NULL),(2,'Level 2','3140596595619098589406629460281260458304759954956',NULL),(3,'Level 3','4668458870574544855726098720',NULL),(4,'Level 4','36262540028503690442512135784044812957921263533770989510',NULL),(5,'Level 5','1129426237700731126421071570',NULL);

/*Table structure for table `parameters` */

DROP TABLE IF EXISTS `parameters`;

CREATE TABLE `parameters` (
  `parameterId` int(11) NOT NULL AUTO_INCREMENT,
  `parameterName` varchar(30) NOT NULL,
  PRIMARY KEY (`parameterId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `parameters` */

insert  into `parameters`(`parameterId`,`parameterName`) values (1,'Bubbles remaining');

/*Table structure for table `playerlevelhighscores` */

DROP TABLE IF EXISTS `playerlevelhighscores`;

CREATE TABLE `playerlevelhighscores` (
  `playerId` int(11) NOT NULL,
  `levelId` int(11) NOT NULL,
  `highscore` int(11) DEFAULT NULL,
  PRIMARY KEY (`playerId`,`levelId`),
  KEY `FK_playerlevelhighscores` (`levelId`),
  CONSTRAINT `FK_playerlevelhighscoresLevels` FOREIGN KEY (`levelId`) REFERENCES `levels` (`levelNumber`),
  CONSTRAINT `FK_playerlevelhighscoresPlayer` FOREIGN KEY (`playerId`) REFERENCES `players` (`playerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `playerlevelhighscores` */

insert  into `playerlevelhighscores`(`playerId`,`levelId`,`highscore`) values (12,1,150),(12,2,180),(13,1,530),(13,2,1245),(15,1,1520),(15,2,1535),(19,1,125),(20,1,150),(24,1,135),(25,1,125),(33,1,125),(34,1,150),(35,1,150),(40,1,150),(41,1,125),(44,1,175);

/*Table structure for table `players` */

DROP TABLE IF EXISTS `players`;

CREATE TABLE `players` (
  `playerId` int(11) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(100) NOT NULL,
  `playerName` varchar(20) NOT NULL,
  `mpHighScoreCount` int(11) DEFAULT '0',
  PRIMARY KEY (`playerId`),
  KEY `FK_player` (`avatar`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

/*Data for the table `players` */

insert  into `players`(`playerId`,`avatar`,`playerName`,`mpHighScoreCount`) values (12,'2','Jel',10),(13,'3','Lucas',30),(14,'3','SHane',5),(15,'3','Yentl',5),(16,'3','test',5),(18,'2','brecyht',0),(19,'2','f',0),(20,'2','d',0),(21,'2','c',0),(22,'2','k',0),(23,'2','1',0),(24,'2','s',0),(25,'2','h',0),(26,'2','g',0),(27,'2','j',0),(28,'2','b',0),(29,'','sdfsdf',0),(30,'','sdf',0),(31,'','dfgdf',0),(32,'test','sdfsdf',0),(33,'test','sdfds',0),(34,'test','sdfsd',0),(35,'test','frgh',0),(36,'avatar4','Jelk',0),(37,'file:../../assets/images/avatars/1.png','lhk',0),(38,'file:../../assets/images/avatars/4.png','dfgfd',0),(39,'file:../../assets/images/avatars/4.png','dsf',0),(40,'file:../../assets/images/avatars/4.png','dfg',0),(41,'file:../../assets/images/avatars/4.png','sdfs',0),(42,'file:../../assets/images/avatars/4.png','sdfgsd',0),(43,'file:../../assets/images/avatars/4.png','dhdgfh',0),(44,'file:../../assets/images/avatars/4.png','dfgd',0),(45,'file:../../assets/images/avatars/4.png','ghj',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
