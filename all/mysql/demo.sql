-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.3.0 - MySQL Community Server - GPL
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 demo 的数据库结构
CREATE DATABASE IF NOT EXISTS `demo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `demo`;

-- 导出  表 demo.permmision_index 结构
CREATE TABLE IF NOT EXISTS `permmision_index` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  demo.permmision_index 的数据：~4 rows (大约)
/*!40000 ALTER TABLE `permmision_index` DISABLE KEYS */;
INSERT INTO `permmision_index` (`id`, `name`, `create_time`) VALUES
	(1, 'create', '2024-03-19 19:21:43'),
	(2, 'read', '2024-03-19 19:21:48'),
	(3, 'update', '2024-03-19 19:22:57'),
	(4, 'delete', '2024-03-19 19:22:59');
/*!40000 ALTER TABLE `permmision_index` ENABLE KEYS */;

-- 导出  表 demo.product_index 结构
CREATE TABLE IF NOT EXISTS `product_index` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` text,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  demo.product_index 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `product_index` DISABLE KEYS */;
INSERT INTO `product_index` (`id`, `name`, `create_time`) VALUES
	(1, '产品1', '2024-03-19 21:43:05');
/*!40000 ALTER TABLE `product_index` ENABLE KEYS */;

-- 导出  表 demo.role_index 结构
CREATE TABLE IF NOT EXISTS `role_index` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  demo.role_index 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `role_index` DISABLE KEYS */;
INSERT INTO `role_index` (`id`, `name`, `create_time`) VALUES
	(1, 'PRODUCT_ADMIN ', '2024-03-19 19:23:49'),
	(2, 'EDITOR', '2024-03-19 19:23:57'),
	(3, 'USER', '2024-03-19 19:24:05');
/*!40000 ALTER TABLE `role_index` ENABLE KEYS */;

-- 导出  表 demo.role_permmission_index 结构
CREATE TABLE IF NOT EXISTS `role_permmission_index` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `permmission_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_role_permmission_index_role_index` (`role_id`),
  KEY `FK_role_permmission_index_permmision_index` (`permmission_id`),
  CONSTRAINT `FK_role_permmission_index_permmision_index` FOREIGN KEY (`permmission_id`) REFERENCES `permmision_index` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_role_permmission_index_role_index` FOREIGN KEY (`role_id`) REFERENCES `role_index` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  demo.role_permmission_index 的数据：~9 rows (大约)
/*!40000 ALTER TABLE `role_permmission_index` DISABLE KEYS */;
INSERT INTO `role_permmission_index` (`id`, `role_id`, `permmission_id`, `create_time`) VALUES
	(1, 1, 1, '2024-03-19 19:24:35'),
	(2, 1, 2, '2024-03-19 19:24:42'),
	(3, 1, 3, '2024-03-19 19:24:50'),
	(4, 1, 4, '2024-03-19 19:24:57'),
	(5, 2, 1, '2024-03-19 19:25:07'),
	(6, 2, 3, '2024-03-19 19:25:18'),
	(7, 2, 4, '2024-03-19 19:25:28'),
	(8, 2, 2, '2024-03-19 19:26:20'),
	(9, 3, 2, '2024-03-19 19:26:27');
/*!40000 ALTER TABLE `role_permmission_index` ENABLE KEYS */;

-- 导出  表 demo.user_index 结构
CREATE TABLE IF NOT EXISTS `user_index` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL DEFAULT '',
  `password` varchar(64) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  demo.user_index 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `user_index` DISABLE KEYS */;
INSERT INTO `user_index` (`id`, `username`, `password`, `create_time`) VALUES
	(1, 'adm_1', '$2a$10$b9M355CSbj0Ru4NBvPT/ie7j1flyZ1drW92JAS/Os/BtZl1ZTb78i', '2024-03-19 19:26:43'),
	(2, 'editor_1', '$2a$10$iIwRRBlBVpM86yWCeeHayeVVW5TAOGeSGKHrwFRJvVf7qo.b3sLoa', '2024-03-19 19:26:43'),
	(3, 'user_1', '$2a$10$JRND7NFXesBhivFclLgGWeOwwbaX2wcHBqOHMgYsxrV/rmg2lr6ba', '2024-03-19 19:26:43');
/*!40000 ALTER TABLE `user_index` ENABLE KEYS */;

-- 导出  表 demo.user_role_index 结构
CREATE TABLE IF NOT EXISTS `user_role_index` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_user_role_index_user_index` (`user_id`),
  KEY `FK_user_role_index_role_index` (`role_id`),
  CONSTRAINT `FK_user_role_index_role_index` FOREIGN KEY (`role_id`) REFERENCES `role_index` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_user_role_index_user_index` FOREIGN KEY (`user_id`) REFERENCES `user_index` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  demo.user_role_index 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `user_role_index` DISABLE KEYS */;
INSERT INTO `user_role_index` (`id`, `user_id`, `role_id`, `create_time`) VALUES
	(1, 1, 1, '2024-03-19 19:27:22'),
	(2, 2, 2, '2024-03-19 19:27:28'),
	(3, 3, 3, '2024-03-19 19:27:34');
/*!40000 ALTER TABLE `user_role_index` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
