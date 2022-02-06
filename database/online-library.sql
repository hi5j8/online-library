CREATE DATABASE  IF NOT EXISTS `online-library` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `online-library`;
-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: online-library
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account_states`
--

DROP TABLE IF EXISTS `account_states`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_states` (
  `state_id` int NOT NULL,
  `state_name` varchar(45) NOT NULL,
  PRIMARY KEY (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_states`
--

LOCK TABLES `account_states` WRITE;
/*!40000 ALTER TABLE `account_states` DISABLE KEYS */;
INSERT INTO `account_states` VALUES (5,'active'),(6,'marked for deletion'),(7,'frozen');
/*!40000 ALTER TABLE `account_states` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `account_id` int NOT NULL AUTO_INCREMENT,
  `password_hash` varchar(256) NOT NULL,
  `state_id` int NOT NULL,
  `registered_since` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `permission_level` int NOT NULL,
  PRIMARY KEY (`account_id`),
  KEY `permission_level_idx` (`permission_level`),
  KEY `fk_ACCOUNTS_ACCOUNT_STATE_idx` (`state_id`),
  CONSTRAINT `fk_ACCOUNTS_ACCOUNT_STATES` FOREIGN KEY (`state_id`) REFERENCES `account_states` (`state_id`),
  CONSTRAINT `fk_ACCOUNTS_ROLES` FOREIGN KEY (`permission_level`) REFERENCES `roles` (`permission_level`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (19,'$2a$10$2yLOsp.6kRnRkGbUVynts.Kiio40bLsAvKqqH25sfxuN/nePlQ.vW',5,'2020-06-17 13:34:25',3),(20,'$2a$10$RrRtQjidAACxChGkS58DK.FaU3Ieny3YT7vupy7QkUX6Dgi76.6QC',5,'2020-06-17 13:34:58',1),(21,'$2a$10$4d4UDeD8KCAwL3FEom9NmOXWYHHhCcyt4tmk1omlBfI4yIqIZ/YCe',5,'2020-07-01 09:46:31',1),(22,'$2a$10$5c3.v8XVVCGXaTZ9SDc..eimWYvZO36wdt7E5ON3aDC8oGt97sjyC',5,'2020-07-25 21:08:10',1);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_authors`
--

DROP TABLE IF EXISTS `book_authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_authors` (
  `author` varchar(128) NOT NULL,
  `books_book_id` int NOT NULL,
  KEY `fk_authors_books1_idx` (`books_book_id`),
  CONSTRAINT `fk_authors_books1` FOREIGN KEY (`books_book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_authors`
--

LOCK TABLES `book_authors` WRITE;
/*!40000 ALTER TABLE `book_authors` DISABLE KEYS */;
INSERT INTO `book_authors` VALUES ('J.K. Rowling',17),('Ninja',18),('Markiplier',18),('author',19),('J.K. Rowling',20);
/*!40000 ALTER TABLE `book_authors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_genres`
--

DROP TABLE IF EXISTS `book_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_genres` (
  `book_genres_list_genre_id` int NOT NULL,
  `books_book_id` int NOT NULL,
  KEY `fk_book_genres_book_genres_list1_idx` (`book_genres_list_genre_id`),
  KEY `fk_book_genres_books1_idx` (`books_book_id`),
  CONSTRAINT `fk_book_genres_book_genres_list1` FOREIGN KEY (`book_genres_list_genre_id`) REFERENCES `book_genres_list` (`genre_id`),
  CONSTRAINT `fk_book_genres_books1` FOREIGN KEY (`books_book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_genres`
--

LOCK TABLES `book_genres` WRITE;
/*!40000 ALTER TABLE `book_genres` DISABLE KEYS */;
INSERT INTO `book_genres` VALUES (10,17),(13,17),(6,18),(10,18),(3,19),(6,19),(9,19);
/*!40000 ALTER TABLE `book_genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_genres_list`
--

DROP TABLE IF EXISTS `book_genres_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_genres_list` (
  `genre_id` int NOT NULL AUTO_INCREMENT,
  `genre_name` varchar(45) NOT NULL,
  PRIMARY KEY (`genre_id`),
  UNIQUE KEY `genre_id_UNIQUE` (`genre_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_genres_list`
--

LOCK TABLES `book_genres_list` WRITE;
/*!40000 ALTER TABLE `book_genres_list` DISABLE KEYS */;
INSERT INTO `book_genres_list` VALUES (1,'Action and Adventure'),(2,'Anthology'),(3,'Classic'),(4,'Comic'),(5,'Crime and Detective'),(6,'Drama'),(7,'Fable'),(8,'Fairy Tale'),(9,'Fan Fiction'),(10,'Fantasy'),(11,'Horror'),(12,'Humor'),(13,'Legend'),(14,'Mystery'),(15,'Romance'),(16,'Satire'),(17,'Science Fiction'),(18,'Short Story'),(19,'Thriller'),(20,'Biography'),(21,'Essay'),(22,'Memoir'),(23,'Periodicals'),(24,'Self Help'),(25,'Textbook'),(26,'Poetry');
/*!40000 ALTER TABLE `book_genres_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_publishers`
--

DROP TABLE IF EXISTS `book_publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_publishers` (
  `publisher` varchar(128) NOT NULL,
  `books_book_id` int NOT NULL,
  KEY `fk_book_publishers_books1_idx` (`books_book_id`),
  CONSTRAINT `fk_book_publishers_books1` FOREIGN KEY (`books_book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_publishers`
--

LOCK TABLES `book_publishers` WRITE;
/*!40000 ALTER TABLE `book_publishers` DISABLE KEYS */;
INSERT INTO `book_publishers` VALUES ('Wowsers',17),('Youtube',18),('Twitch',18),('Mixeer',18),('author 123',19),('xd',19),('Bloomsbury',20);
/*!40000 ALTER TABLE `book_publishers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `media_media_id` int NOT NULL,
  PRIMARY KEY (`book_id`),
  UNIQUE KEY `book_id_UNIQUE` (`book_id`),
  KEY `fk_books_media1_idx` (`media_media_id`),
  CONSTRAINT `fk_books_media1` FOREIGN KEY (`media_media_id`) REFERENCES `media` (`media_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (17,25),(18,26),(19,27),(20,28);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media`
--

DROP TABLE IF EXISTS `media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media` (
  `media_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(128) NOT NULL,
  `release_date` date NOT NULL,
  `is_available` tinyint NOT NULL,
  `is_borrowed` tinyint NOT NULL,
  PRIMARY KEY (`media_id`),
  UNIQUE KEY `media_id_UNIQUE` (`media_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media`
--

LOCK TABLES `media` WRITE;
/*!40000 ALTER TABLE `media` DISABLE KEYS */;
INSERT INTO `media` VALUES (25,'Harry Potter','2016-03-23',1,0),(26,'Gaming Book','2016-06-08',1,0),(27,'Test Book','2020-06-30',0,0),(28,'Harry Potter','1999-08-25',1,0);
/*!40000 ALTER TABLE `media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `permission_level` int NOT NULL,
  `role_name` varchar(45) NOT NULL,
  PRIMARY KEY (`permission_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (0,'guest'),(1,'user'),(2,'moderator'),(3,'administrator');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) NOT NULL,
  `email_address` varchar(64) NOT NULL,
  `account_id` int NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `fk_USERS_ACCOUNTS_idx` (`account_id`),
  CONSTRAINT `fk_USERS_ACCOUNTS` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (28,'admin','John','Doe','john-doe@example.com',19),(29,'JohnDoe','John','Doe','john-doe@example.com',20),(30,'TestAccount','John','Doe','john-doe@example.com',21),(31,'JohnDoe13','John','Doe','johndoe@example-com',22);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watchlists`
--

DROP TABLE IF EXISTS `watchlists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `watchlists` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `media_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_userid_watchlist_idx` (`media_id`),
  KEY `fk_userid_watchlist_idx1` (`user_id`),
  CONSTRAINT `fk_mediaid_watchlist` FOREIGN KEY (`media_id`) REFERENCES `media` (`media_id`),
  CONSTRAINT `fk_userid_watchlist` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watchlists`
--

LOCK TABLES `watchlists` WRITE;
/*!40000 ALTER TABLE `watchlists` DISABLE KEYS */;
/*!40000 ALTER TABLE `watchlists` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-25 23:00:52
