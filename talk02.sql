-- MySQL dump 10.16  Distrib 10.1.23-MariaDB, for Win64 (AMD64)
--
-- Host: 127.0.0.1    Database: forum_db
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '评论者ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父评论ID，0表示顶级评论（预留）',
  `content` varchar(500) NOT NULL,
  `like_count` int DEFAULT '0',
  `status` tinyint DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follow`
--

DROP TABLE IF EXISTS `follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `follow` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '关注者ID',
  `follow_user_id` bigint NOT NULL COMMENT '被关注者ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_follow` (`user_id`,`follow_user_id`),
  KEY `idx_follow_user` (`follow_user_id`),
  CONSTRAINT `follow_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `follow_ibfk_2` FOREIGN KEY (`follow_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='关注关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follow`
--

LOCK TABLES `follow` WRITE;
/*!40000 ALTER TABLE `follow` DISABLE KEYS */;
/*!40000 ALTER TABLE `follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `like`
--

DROP TABLE IF EXISTS `like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `target_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`,`target_type`,`target_id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `like`
--

LOCK TABLES `like` WRITE;
/*!40000 ALTER TABLE `like` DISABLE KEYS */;
INSERT INTO `like` VALUES (1,'post',1,2,'2026-04-15 10:00:00'),(2,'post',1,3,'2026-04-15 10:30:00'),(3,'post',1,4,'2026-04-15 11:00:00'),(4,'post',1,5,'2026-04-15 11:30:00'),(5,'post',1,6,'2026-04-15 12:00:00'),(6,'post',1,7,'2026-04-15 12:30:00'),(7,'post',1,8,'2026-04-15 13:00:00'),(8,'post',2,1,'2026-04-10 15:00:00'),(9,'post',2,3,'2026-04-10 15:30:00'),(10,'post',2,4,'2026-04-10 16:00:00'),(11,'post',2,6,'2026-04-10 16:30:00'),(12,'post',2,7,'2026-04-10 17:00:00'),(13,'post',2,8,'2026-04-10 17:30:00'),(14,'post',2,5,'2026-04-11 09:00:00'),(15,'post',3,2,'2026-03-28 17:00:00'),(16,'post',3,3,'2026-03-28 17:30:00'),(17,'post',3,4,'2026-03-28 18:00:00'),(18,'post',3,5,'2026-03-28 18:30:00'),(19,'post',3,6,'2026-03-28 19:00:00'),(20,'post',3,7,'2026-03-28 19:30:00'),(21,'post',3,8,'2026-03-28 20:00:00'),(22,'post',4,1,'2026-04-12 11:00:00'),(23,'post',4,3,'2026-04-12 12:00:00'),(24,'post',4,5,'2026-04-12 12:30:00'),(25,'post',4,6,'2026-04-12 13:00:00'),(26,'post',4,7,'2026-04-12 14:00:00'),(27,'post',4,8,'2026-04-12 15:00:00'),(28,'post',5,3,'2026-04-08 16:00:00'),(29,'post',5,7,'2026-04-08 18:00:00'),(30,'post',5,1,'2026-04-09 10:00:00'),(31,'post',6,1,'2026-04-14 09:00:00'),(32,'post',6,2,'2026-04-14 10:00:00'),(33,'post',6,4,'2026-04-14 10:30:00'),(34,'post',6,5,'2026-04-14 11:00:00'),(35,'post',6,6,'2026-04-14 12:00:00'),(36,'post',6,7,'2026-04-14 14:00:00'),(37,'post',6,8,'2026-04-14 16:00:00'),(38,'post',7,1,'2026-04-06 08:00:00'),(39,'post',7,2,'2026-04-05 21:30:00'),(40,'post',7,4,'2026-04-06 10:00:00'),(41,'post',7,5,'2026-04-06 08:30:00'),(42,'post',7,6,'2026-04-06 11:00:00'),(43,'post',7,8,'2026-04-05 22:00:00'),(44,'post',8,1,'2026-03-20 12:00:00'),(45,'post',8,2,'2026-03-20 15:00:00'),(46,'post',8,5,'2026-03-21 09:00:00'),(47,'post',8,7,'2026-03-20 11:30:00'),(48,'post',9,1,'2026-04-13 13:00:00'),(49,'post',9,2,'2026-04-13 14:00:00'),(50,'post',9,3,'2026-04-13 12:30:00'),(51,'post',9,6,'2026-04-13 15:00:00'),(52,'post',10,6,'2026-04-01 19:00:00'),(53,'post',10,1,'2026-04-02 10:00:00'),(54,'post',10,7,'2026-04-02 14:00:00'),(55,'post',11,3,'2026-04-11 22:00:00'),(56,'post',11,4,'2026-04-12 08:00:00'),(57,'post',11,6,'2026-04-12 09:00:00'),(58,'post',11,8,'2026-04-11 23:00:00'),(59,'post',13,1,'2026-04-10 09:00:00'),(60,'post',13,2,'2026-04-10 10:00:00'),(61,'post',13,3,'2026-04-10 08:30:00'),(62,'post',13,4,'2026-04-10 09:30:00'),(63,'post',13,5,'2026-04-10 10:30:00'),(64,'post',13,7,'2026-04-10 11:00:00'),(65,'post',13,8,'2026-04-10 11:30:00'),(66,'post',14,1,'2026-04-03 07:00:00'),(67,'post',14,3,'2026-04-03 08:00:00'),(68,'post',14,4,'2026-04-03 09:00:00'),(69,'post',14,5,'2026-04-03 10:00:00'),(70,'post',14,7,'2026-04-03 11:00:00'),(71,'post',18,1,'2026-04-12 17:00:00'),(72,'post',18,3,'2026-04-12 18:00:00'),(73,'post',18,5,'2026-04-13 09:00:00'),(74,'post',19,2,'2026-04-03 10:00:00'),(75,'post',19,3,'2026-04-03 11:00:00'),(76,'post',19,5,'2026-04-04 09:00:00'),(77,'post',19,6,'2026-04-04 10:00:00'),(78,'post',19,7,'2026-04-05 11:00:00'),(79,'comment',1,3,'2026-04-15 10:30:00'),(80,'comment',1,5,'2026-04-15 11:00:00'),(81,'comment',1,6,'2026-04-15 12:00:00'),(82,'comment',5,1,'2026-04-15 14:30:00'),(83,'comment',5,2,'2026-04-15 15:00:00'),(84,'comment',5,3,'2026-04-15 15:30:00'),(85,'comment',5,4,'2026-04-15 16:00:00'),(86,'comment',27,1,'2026-04-14 10:00:00'),(87,'comment',27,6,'2026-04-14 11:00:00');
/*!40000 ALTER TABLE `like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `like_record`
--

DROP TABLE IF EXISTS `like_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `like_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `target_type` tinyint NOT NULL COMMENT '1-帖子 2-评论',
  `target_id` bigint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`,`target_type`,`target_id`),
  CONSTRAINT `like_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='点赞记录表（备份）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `like_record`
--

LOCK TABLES `like_record` WRITE;
/*!40000 ALTER TABLE `like_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `like_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '接收通知的用户ID',
  `from_user_id` bigint NOT NULL COMMENT '触发通知的用户ID',
  `type` tinyint NOT NULL COMMENT '1-点赞 2-评论 3-关注',
  `target_id` bigint NOT NULL COMMENT '目标ID（帖子ID或评论ID）',
  `content` varchar(200) DEFAULT NULL,
  `is_read` tinyint DEFAULT '0' COMMENT '0-未读 1-已读',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_read` (`user_id`,`is_read`),
  KEY `idx_create_time` (`create_time`),
  KEY `from_user_id` (`from_user_id`),
  CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `notification_ibfk_2` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '发布者ID',
  `title` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `like_count` int DEFAULT '0' COMMENT '点赞数（冗余，从Redis同步）',
  `comment_count` int DEFAULT '0' COMMENT '评论数',
  `view_count` int DEFAULT '0' COMMENT '浏览数',
  `status` tinyint DEFAULT '1' COMMENT '1-正常 2-删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码（加密）',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像URL',
  `bio` varchar(200) DEFAULT NULL COMMENT '个人简介',
  `follow_count` int DEFAULT '0' COMMENT '关注数',
  `fans_count` int DEFAULT '0' COMMENT '粉丝数',
  `status` tinyint DEFAULT '1' COMMENT '状态：1正常，0禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-06  9:13:51
