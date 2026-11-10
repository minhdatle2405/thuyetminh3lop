-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: video_narration_db
-- ------------------------------------------------------
-- Server version	26.7.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `access_log`
--

DROP TABLE IF EXISTS `access_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `access_log` (
  `access_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `accessed_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `last_seen_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `ip_address` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_agent` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`access_id`),
  KEY `idx_accessed_at` (`accessed_at`),
  KEY `idx_last_seen_at` (`last_seen_at`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audio_output`
--

DROP TABLE IF EXISTS `audio_output`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audio_output` (
  `audio_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `job_id` bigint unsigned NOT NULL,
  `voice_id` bigint unsigned DEFAULT NULL,
  `file_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_path` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `format` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `duration_ms` bigint unsigned DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`audio_id`),
  KEY `idx_audio_job_id` (`job_id`),
  KEY `idx_audio_voice_id` (`voice_id`),
  CONSTRAINT `fk_audio_job` FOREIGN KEY (`job_id`) REFERENCES `translation_job` (`job_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_audio_voice` FOREIGN KEY (`voice_id`) REFERENCES `voice_profile` (`voice_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `processing_config`
--

DROP TABLE IF EXISTS `processing_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `processing_config` (
  `config_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `config_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_language` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_language` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `subtitle_format` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `audio_format` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subtitle_segment`
--

DROP TABLE IF EXISTS `subtitle_segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subtitle_segment` (
  `subtitle_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `translation_id` bigint unsigned NOT NULL,
  `transcript_seg_id` bigint unsigned DEFAULT NULL,
  `sequence_no` int unsigned NOT NULL,
  `start_ms` bigint unsigned NOT NULL,
  `end_ms` bigint unsigned NOT NULL,
  `original_text` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `translated_text` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`subtitle_id`),
  UNIQUE KEY `uq_subtitle_translation_sequence` (`translation_id`,`sequence_no`),
  KEY `idx_subtitle_translation_id` (`translation_id`),
  KEY `idx_subtitle_transcript_seg_id` (`transcript_seg_id`),
  CONSTRAINT `fk_subtitle_transcript_segment` FOREIGN KEY (`transcript_seg_id`) REFERENCES `transcript_segment` (`transcript_seg_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_subtitle_translation` FOREIGN KEY (`translation_id`) REFERENCES `translation` (`translation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_subtitle_segment_time` CHECK ((`end_ms` > `start_ms`))
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transcript`
--

DROP TABLE IF EXISTS `transcript`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transcript` (
  `transcript_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `job_id` bigint unsigned NOT NULL,
  `language` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_text` longtext COLLATE utf8mb4_unicode_ci,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`transcript_id`),
  KEY `idx_transcript_job_id` (`job_id`),
  CONSTRAINT `fk_transcript_job` FOREIGN KEY (`job_id`) REFERENCES `translation_job` (`job_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transcript_segment`
--

DROP TABLE IF EXISTS `transcript_segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transcript_segment` (
  `transcript_seg_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `transcript_id` bigint unsigned NOT NULL,
  `sequence_no` int unsigned NOT NULL,
  `start_ms` bigint unsigned NOT NULL,
  `end_ms` bigint unsigned NOT NULL,
  `original_text` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`transcript_seg_id`),
  UNIQUE KEY `uq_transcript_segment_sequence` (`transcript_id`,`sequence_no`),
  KEY `idx_segment_transcript_id` (`transcript_id`),
  CONSTRAINT `fk_segment_transcript` FOREIGN KEY (`transcript_id`) REFERENCES `transcript` (`transcript_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_transcript_segment_time` CHECK ((`end_ms` > `start_ms`))
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `translation`
--

DROP TABLE IF EXISTS `translation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `translation` (
  `translation_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `job_id` bigint unsigned NOT NULL,
  `source_language` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_language` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('PENDING','PROCESSING','COMPLETED','FAILED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`translation_id`),
  KEY `idx_translation_job_id` (`job_id`),
  KEY `idx_translation_status` (`status`),
  CONSTRAINT `fk_translation_job` FOREIGN KEY (`job_id`) REFERENCES `translation_job` (`job_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `translation_job`
--

DROP TABLE IF EXISTS `translation_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `translation_job` (
  `job_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `video_id` bigint unsigned NOT NULL,
  `config_id` bigint unsigned NOT NULL,
  `status` enum('PENDING','PROCESSING','COMPLETED','FAILED','CANCELLED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `progress_percent` tinyint unsigned NOT NULL DEFAULT '0',
  `error_message` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `started_at` datetime(3) DEFAULT NULL,
  `completed_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`job_id`),
  KEY `idx_job_video_id` (`video_id`),
  KEY `idx_job_config_id` (`config_id`),
  KEY `idx_job_status` (`status`),
  KEY `idx_job_created_at` (`created_at`),
  CONSTRAINT `fk_job_config` FOREIGN KEY (`config_id`) REFERENCES `processing_config` (`config_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_job_video` FOREIGN KEY (`video_id`) REFERENCES `video_source` (`video_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_job_progress` CHECK ((`progress_percent` between 0 and 100)),
  CONSTRAINT `chk_job_time_order` CHECK (((`completed_at` is null) or (`started_at` is null) or (`completed_at` >= `started_at`)))
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `video_source`
--

DROP TABLE IF EXISTS `video_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_source` (
  `video_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `source_type` enum('UPLOAD','YOUTUBE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `original_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_url` varchar(2048) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `source_file_path` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `duration_seconds` int unsigned DEFAULT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`video_id`),
  CONSTRAINT `chk_video_source_location` CHECK ((((`source_type` = _utf8mb4'UPLOAD') and (`source_file_path` is not null)) or ((`source_type` = _utf8mb4'YOUTUBE') and (`source_url` is not null))))
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `voice_profile`
--

DROP TABLE IF EXISTS `voice_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voice_profile` (
  `voice_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `voice_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `language_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `provider_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `provider_voice_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`voice_id`),
  UNIQUE KEY `uq_voice_provider_id` (`provider_name`,`provider_voice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'video_narration_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-06 22:39:04
