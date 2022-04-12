CREATE TABLE `users_tokens` (
  `id_user` BIGINT NOT NULL,
  `access_token` VARCHAR(1024) NOT NULL,
  `refresh_token` VARCHAR(1024) NULL,
  PRIMARY KEY (`id_user`)
  );

