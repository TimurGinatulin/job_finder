CREATE TABLE `jober_memory` (
  `id_summary` VARCHAR(127) NOT NULL,
  `id_vacancy` VARCHAR(127) NOT NULL,
  `time_stamp` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id_summary`, `id_vacancy`),
  KEY `fk_jober_memory` (`id_summary`)
  );