-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        8.0.42 - MySQL Community Server - GPL
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- lostark 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `lostark` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `lostark`;

-- 테이블 lostark.board 구조 내보내기
CREATE TABLE IF NOT EXISTS `board` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `view_count` int NOT NULL DEFAULT '0',
  `is_notice` tinyint(1) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `category` enum('FREE','SUGGEST','PARTY','GUILD','FRIEND') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'FREE',
  `comment_count` int NOT NULL DEFAULT '0',
  `like_count` int NOT NULL DEFAULT '0',
  `ip_address` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_board_user` (`user_id`),
  CONSTRAINT `fk_board_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 lostark.board:~0 rows (대략적) 내보내기
INSERT INTO `board` (`id`, `user_id`, `title`, `content`, `view_count`, `is_notice`, `is_deleted`, `created_at`, `updated_at`, `category`, `comment_count`, `like_count`, `ip_address`) VALUES
	(35, 5, 'dasasda', 'adsaadsa', 49, 0, 0, '2026-01-17 14:40:10', NULL, 'FREE', 7, 0, '0:0:0:0:0:0:0:1');

-- 테이블 lostark.character 구조 내보내기
CREATE TABLE IF NOT EXISTS `character` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `nickname` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `server_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `class_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `item_level` decimal(10,2) DEFAULT NULL,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `character_image_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `order_index` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `character_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 lostark.character:~6 rows (대략적) 내보내기
INSERT INTO `character` (`id`, `user_id`, `nickname`, `server_name`, `class_name`, `item_level`, `updated_at`, `character_image_url`, `order_index`) VALUES
	(17, 11, '냥냥펀치망구', '카제로스', '인파이터', 1702.16, '2026-01-17 15:40:41', 'https://img.lostark.co.kr/armory/1/E42500C5E892FD2B19CB6DF1128C2776F3105B7DDE011BC7ADD9AE9752C96FCB.jpg?v=20260117044551', NULL),
	(18, 11, '눈사람만드는망구', '카제로스', '도화가', 1548.33, '2026-01-17 15:40:41', 'https://img.lostark.co.kr/armory/9/087FB92A051622DB2F646193942A61F14599B25F7DA36B5C93E913BBA9A05C7B.jpg?v=20250821124230', NULL),
	(19, 11, '수호천사망구', '카제로스', '워로드', 1703.33, '2026-01-17 15:40:41', 'https://img.lostark.co.kr/armory/3/042C0212CA0A03EE63D68B029FDB03506833F7E04ADAE7D10B43BD66D0DF2FCD.jpg?v=20260115225457', NULL),
	(24, 5, '이다', '루페온', '브레이커', 1762.50, '2026-01-22 12:58:31', 'https://img.lostark.co.kr/armory/7/3A97CC19AFF9C1FFE724924AB9528A4364F2670C45353F826C3A1EEBF1FFBC35.jpg?v=20260116145146', 2),
	(26, 5, '로마러', '아만', '버서커', 1740.83, '2026-01-22 12:58:31', 'https://img.lostark.co.kr/armory/9/5DEE8C966F0DB290829C1C569A470DB7B9140EA5F8396A8D67FA600781892900.jpg?v=20260109061620', 0),
	(28, 5, '조이냥', '카단', '바드', 1771.66, '2026-01-22 12:58:31', 'https://img.lostark.co.kr/armory/6/F6326CD0EED43A9E25E2461BBA251711AF213D49FCEACC851E60C72CF3BFF95B.jpg?v=20260117075634', 1);

-- 테이블 lostark.comment 구조 내보내기
CREATE TABLE IF NOT EXISTS `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `board_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_comment_board` (`board_id`),
  KEY `fk_comment_user` (`user_id`),
  KEY `fk_comment_parent` (`parent_id`),
  CONSTRAINT `fk_comment_board` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_parent` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 lostark.comment:~6 rows (대략적) 내보내기
INSERT INTO `comment` (`id`, `board_id`, `user_id`, `content`, `parent_id`, `is_deleted`, `created_at`, `updated_at`) VALUES
	(37, 35, 11, 'sdadasdadas', NULL, 0, '2026-01-17 14:40:15', NULL),
	(38, 35, 5, 'asdaadsa', 37, 0, '2026-01-17 14:40:19', NULL),
	(39, 35, 11, 'ddd', 38, 0, '2026-01-17 14:40:25', NULL),
	(40, 35, 11, 'dsadasdadas', 38, 0, '2026-01-17 14:40:31', NULL),
	(41, 35, 11, 'adssad', 38, 0, '2026-01-17 14:44:17', NULL),
	(42, 35, 11, 'ㅅㅅㅅㅅ', 38, 0, '2026-01-17 14:44:25', NULL),
	(43, 35, 11, 'ㅅㅅㅅ', 40, 0, '2026-01-17 14:44:29', NULL);

-- 테이블 lostark.file 구조 내보내기
CREATE TABLE IF NOT EXISTS `file` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `board_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `original_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `stored_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_path` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_size` bigint DEFAULT NULL,
  `is_thumbnail` tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `uploaded_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_file_board` (`board_id`),
  KEY `fk_file_user` (`user_id`),
  CONSTRAINT `fk_file_board` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_file_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 lostark.file:~0 rows (대략적) 내보내기

-- 테이블 lostark.item 구조 내보내기
CREATE TABLE IF NOT EXISTS `item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `api_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `grade` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 lostark.item:~0 rows (대략적) 내보내기

-- 테이블 lostark.raid_detail 구조 내보내기
CREATE TABLE IF NOT EXISTS `raid_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `task_id` bigint NOT NULL,
  `phase_number` int NOT NULL,
  `gold_reward` int DEFAULT '0',
  `difficulty` enum('NORMAL','HARD') COLLATE utf8mb4_unicode_ci DEFAULT 'NORMAL',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_biweekly` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `raid_detail_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 lostark.raid_detail:~72 rows (대략적) 내보내기
INSERT INTO `raid_detail` (`id`, `task_id`, `phase_number`, `gold_reward`, `difficulty`, `created_at`, `is_biweekly`) VALUES
	(1, 4, 1, 500, 'NORMAL', '2025-11-17 15:01:32', 0),
	(2, 4, 1, 700, 'HARD', '2025-11-17 15:01:32', 0),
	(3, 4, 2, 700, 'NORMAL', '2025-11-17 15:01:32', 0),
	(4, 4, 2, 1100, 'HARD', '2025-11-17 15:01:32', 0),
	(5, 5, 1, 600, 'NORMAL', '2025-11-17 15:01:32', 0),
	(6, 5, 1, 900, 'HARD', '2025-11-17 15:01:32', 0),
	(7, 5, 2, 1000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(8, 5, 2, 1500, 'HARD', '2025-11-17 15:01:32', 0),
	(9, 6, 1, 600, 'NORMAL', '2025-11-17 15:01:32', 0),
	(10, 6, 2, 900, 'NORMAL', '2025-11-17 15:01:32', 0),
	(11, 6, 3, 1500, 'NORMAL', '2025-11-17 15:01:32', 0),
	(12, 7, 1, 1000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(13, 7, 1, 1200, 'HARD', '2025-11-17 15:01:32', 0),
	(14, 7, 2, 1000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(15, 7, 2, 1200, 'HARD', '2025-11-17 15:01:32', 0),
	(16, 7, 3, 1000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(17, 7, 3, 1200, 'HARD', '2025-11-17 15:01:32', 0),
	(18, 7, 4, 1600, 'NORMAL', '2025-11-17 15:01:32', 1),
	(19, 7, 4, 2000, 'HARD', '2025-11-17 15:01:32', 1),
	(20, 8, 1, 750, 'NORMAL', '2025-11-17 15:01:32', 0),
	(21, 8, 1, 900, 'HARD', '2025-11-17 15:01:32', 0),
	(22, 8, 2, 1100, 'NORMAL', '2025-11-17 15:01:32', 0),
	(23, 8, 2, 1400, 'HARD', '2025-11-17 15:01:32', 0),
	(24, 8, 3, 1450, 'NORMAL', '2025-11-17 15:01:32', 0),
	(25, 8, 3, 2000, 'HARD', '2025-11-17 15:01:32', 0),
	(26, 9, 1, 1000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(27, 9, 1, 1500, 'HARD', '2025-11-17 15:01:32', 0),
	(28, 9, 2, 1800, 'NORMAL', '2025-11-17 15:01:32', 0),
	(29, 9, 2, 2500, 'HARD', '2025-11-17 15:01:32', 0),
	(30, 9, 3, 2600, 'NORMAL', '2025-11-17 15:01:32', 0),
	(31, 9, 3, 3500, 'HARD', '2025-11-17 15:01:32', 0),
	(32, 10, 1, 1600, 'NORMAL', '2025-11-17 15:01:32', 0),
	(33, 10, 1, 2000, 'HARD', '2025-11-17 15:01:32', 0),
	(34, 10, 2, 2000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(35, 10, 2, 2400, 'HARD', '2025-11-17 15:01:32', 0),
	(36, 10, 3, 2800, 'NORMAL', '2025-11-17 15:01:32', 0),
	(37, 10, 3, 3600, 'HARD', '2025-11-17 15:01:32', 0),
	(38, 10, 4, 5000, 'HARD', '2025-11-17 15:01:32', 1),
	(39, 11, 1, 1200, 'NORMAL', '2025-11-17 15:01:32', 0),
	(40, 11, 1, 1400, 'HARD', '2025-11-17 15:01:32', 0),
	(41, 11, 2, 1600, 'NORMAL', '2025-11-17 15:01:32', 0),
	(42, 11, 2, 2000, 'HARD', '2025-11-17 15:01:32', 0),
	(43, 11, 3, 2400, 'NORMAL', '2025-11-17 15:01:32', 0),
	(44, 11, 3, 3800, 'HARD', '2025-11-17 15:01:32', 0),
	(45, 12, 1, 2800, 'NORMAL', '2025-11-17 15:01:32', 0),
	(46, 12, 2, 6000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(47, 13, 1, 2300, 'NORMAL', '2025-11-17 15:01:32', 0),
	(48, 13, 1, 2800, 'HARD', '2025-11-17 15:01:32', 0),
	(49, 13, 2, 5000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(50, 13, 2, 6000, 'HARD', '2025-11-17 15:01:32', 0),
	(51, 14, 1, 4750, 'NORMAL', '2025-11-17 15:01:32', 0),
	(52, 14, 1, 8000, 'HARD', '2025-11-17 15:01:32', 0),
	(53, 14, 2, 10750, 'NORMAL', '2025-11-17 15:01:32', 0),
	(54, 14, 2, 16500, 'HARD', '2025-11-17 15:01:32', 0),
	(55, 15, 1, 7250, 'NORMAL', '2025-11-17 15:01:32', 0),
	(56, 15, 1, 10000, 'HARD', '2025-11-17 15:01:32', 0),
	(57, 15, 2, 14250, 'NORMAL', '2025-11-17 15:01:32', 0),
	(58, 15, 2, 20500, 'HARD', '2025-11-17 15:01:32', 0),
	(59, 16, 1, 6000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(60, 16, 1, 7000, 'HARD', '2025-11-17 15:01:32', 0),
	(61, 16, 2, 9500, 'NORMAL', '2025-11-17 15:01:32', 0),
	(62, 16, 2, 11000, 'HARD', '2025-11-17 15:01:32', 0),
	(63, 16, 3, 12500, 'NORMAL', '2025-11-17 15:01:32', 0),
	(64, 16, 3, 20000, 'HARD', '2025-11-17 15:01:32', 0),
	(65, 17, 1, 12500, 'NORMAL', '2025-11-17 15:01:32', 0),
	(66, 17, 1, 15000, 'HARD', '2025-11-17 15:01:32', 0),
	(67, 17, 2, 20500, 'NORMAL', '2025-11-17 15:01:32', 0),
	(68, 17, 2, 27000, 'HARD', '2025-11-17 15:01:32', 0),
	(69, 18, 1, 14000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(70, 18, 1, 17000, 'HARD', '2025-11-17 15:01:32', 0),
	(71, 18, 2, 26000, 'NORMAL', '2025-11-17 15:01:32', 0),
	(72, 18, 2, 35000, 'HARD', '2025-11-17 15:01:32', 0);

-- 테이블 lostark.raid_detail_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `raid_detail_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `raid_detail_id` bigint NOT NULL,
  `item_id` bigint NOT NULL,
  `quantity` int DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `raid_detail_id` (`raid_detail_id`),
  KEY `item_id` (`item_id`),
  CONSTRAINT `raid_detail_item_ibfk_1` FOREIGN KEY (`raid_detail_id`) REFERENCES `raid_detail` (`id`) ON DELETE CASCADE,
  CONSTRAINT `raid_detail_item_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 lostark.raid_detail_item:~0 rows (대략적) 내보내기

-- 테이블 lostark.task 구조 내보내기
CREATE TABLE IF NOT EXISTS `task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('DAILY','WEEKLY','RAID','EVENT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `official_days` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `max_phase` int DEFAULT '1',
  `is_official` tinyint(1) DEFAULT '1',
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 lostark.task:~21 rows (대략적) 내보내기
INSERT INTO `task` (`id`, `name`, `type`, `official_days`, `max_phase`, `is_official`, `is_active`, `created_at`) VALUES
	(1, '쿠르잔 전선', 'DAILY', NULL, 1, 1, 1, '2025-11-17 13:36:43'),
	(2, '가디언 토벌', 'DAILY', NULL, 1, 1, 1, '2025-11-17 13:36:43'),
	(3, '낙원', 'WEEKLY', NULL, 1, 1, 1, '2025-11-17 13:36:43'),
	(4, '발탄', 'RAID', NULL, 2, 1, 1, '2025-11-17 13:36:43'),
	(5, '비아키스', 'RAID', NULL, 2, 1, 1, '2025-11-17 13:36:43'),
	(6, '쿠크세이튼', 'RAID', NULL, 3, 1, 1, '2025-11-17 13:36:43'),
	(7, '아브렐슈드', 'RAID', NULL, 4, 1, 1, '2025-11-17 13:36:43'),
	(8, '카양겔', 'RAID', NULL, 3, 1, 1, '2025-11-17 13:36:43'),
	(9, '일리아칸', 'RAID', NULL, 3, 1, 1, '2025-11-17 13:36:43'),
	(10, '카멘', 'RAID', NULL, 4, 1, 1, '2025-11-17 13:36:43'),
	(11, '상아탑', 'RAID', NULL, 3, 1, 1, '2025-11-17 13:36:43'),
	(12, '베히모스', 'RAID', NULL, 2, 1, 1, '2025-11-17 13:36:43'),
	(13, '서막', 'RAID', NULL, 2, 1, 1, '2025-11-17 13:36:43'),
	(14, '1막', 'RAID', NULL, 2, 1, 1, '2025-11-17 13:36:43'),
	(15, '2막', 'RAID', NULL, 3, 1, 1, '2025-11-17 13:36:43'),
	(16, '3막', 'RAID', NULL, 3, 1, 1, '2025-11-17 13:36:43'),
	(17, '4막', 'RAID', NULL, 3, 1, 1, '2025-11-17 13:36:43'),
	(18, '종막', 'RAID', NULL, 3, 1, 1, '2025-11-17 13:36:43'),
	(19, '카오스 게이트', 'EVENT', 'MON,THU,SAT,SUN', 1, 1, 1, '2025-11-17 13:36:43'),
	(20, '필드 보스', 'EVENT', 'TUE,FRI,SUN', 1, 1, 1, '2025-11-17 13:36:43'),
	(21, '모험의 섬', 'EVENT', 'MON,TUE,WED,THU,FRI,SAT,SUN', 1, 1, 1, '2025-11-17 13:36:43');

-- 테이블 lostark.task_progress 구조 내보내기
CREATE TABLE IF NOT EXISTS `task_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `character_id` bigint NOT NULL,
  `task_id` bigint DEFAULT NULL,
  `custom_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reset_type` enum('DAILY','WEEKLY') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `progress_phase` int DEFAULT '0',
  `is_completed` tinyint(1) DEFAULT '0',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `difficulty_data` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `character_id` (`character_id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `task_progress_ibfk_1` FOREIGN KEY (`character_id`) REFERENCES `character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `task_progress_ibfk_2` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 lostark.task_progress:~5 rows (대략적) 내보내기
INSERT INTO `task_progress` (`id`, `character_id`, `task_id`, `custom_name`, `reset_type`, `progress_phase`, `is_completed`, `updated_at`, `difficulty_data`) VALUES
	(135, 24, 1, NULL, NULL, 0, 0, '2026-01-19 13:20:43', NULL),
	(136, 24, 2, NULL, NULL, 1, 1, '2026-01-19 13:20:50', '[]'),
	(137, 24, 3, NULL, NULL, 1, 1, '2026-01-19 13:20:49', '[]'),
	(140, 26, 1, NULL, NULL, 0, 0, '2026-01-19 13:21:08', NULL),
	(141, 26, 3, NULL, NULL, 0, 0, '2026-01-19 13:21:09', NULL);

-- 테이블 lostark.user 구조 내보내기
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('USER','ADMIN') COLLATE utf8mb4_unicode_ci DEFAULT 'USER',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 lostark.user:~3 rows (대략적) 내보내기
INSERT INTO `user` (`id`, `password`, `email`, `nickname`, `role`, `created_at`) VALUES
	(5, '$2a$10$KnJWaGcSNKDgEWiqj96yc.UoKNgOE0nelnqc7iTMFxG.PJQuxPZ7e', 'abc@naver.com', '앙버터망구', 'USER', '2025-11-12 05:31:47'),
	(9, '$2a$10$tq2ELXLkNp2EDN4LDNUKmektFGs/vfaDW1pPB1TMRSPHLnfjyGHLO', 'abcd@naver.com', '비가싫은망구', 'USER', '2025-11-25 21:08:39'),
	(10, '$2a$10$W0frMFKrWh5//lzb5mm/cOaZw1V5QKphQUAZjvJYOIhyn7N66KrZq', 'ahs@naver.com', '빡녕', 'USER', '2025-11-28 05:18:28'),
	(11, '$2a$10$U5LSfLTBMFlmASN6cb6AiusHKuT5yBtQDdiTpMZdXNLUY/YucJqXa', 'abb@naver.com', '냥냥펀치망구', 'USER', '2025-11-28 09:53:07');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
