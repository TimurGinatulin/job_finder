CREATE TABLE `area` (
  `area_id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`area_id`),
  KEY `FK2dmtr5518yrmeswf3hau5ksik` (`parent_id`)
);

CREATE TABLE `empolyment` (
  `empolyment_id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`empolyment_id`)
);

CREATE TABLE `industry` (
  `industry_id` double NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` double DEFAULT NULL,
  PRIMARY KEY (`industry_id`),
  KEY `FKq0jbh4n1a2m8k4k691vw24ir8` (`parent_id`)
);

CREATE TABLE `schedule` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `specialization` (
  `specialization_id` double NOT NULL,
  `laboring` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` double DEFAULT NULL,
  PRIMARY KEY (`specialization_id`),
  KEY `FKihl7myduwdf9w2k2a2ylmbhym` (`parent_id`)
);

CREATE TABLE `experience` (
  `experience_id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`experience_id`)
);
CREATE TABLE `currency`(
  `id` varchar(3) NOT NULL,
  `name` varchar(124),
  `rate` double,
  PRIMARY KEY (`id`)
);
CREATE TABLE `filter` (
  `filter_id` bigint NOT NULL AUTO_INCREMENT,
  `summary_id` varchar(127) NOT NULL,
  `cover_letter` varchar(255) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `salary` bigint DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `time_stamp` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `area_id` bigint DEFAULT NULL,
  `currency_id` varchar(3) DEFAULT NULL,
  `industry_id` double DEFAULT NULL,
  `specialization_id` double DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `total_sends` bigint DEFAULT 0,
  PRIMARY KEY (`filter_id`),
  KEY `FKberl5ycn3l12ryaa8pitflhd` (`area_id`),
  KEY `FK1x7343qk77f8nr4d39sjwmb50` (`currency_id`),
  KEY `FK4pf96udd4hok8v7lv98xrkqcv` (`specialization_id`),
  KEY `FK3ar7feex8aud4b0v6s084bt2t` (`user_id`)
);

CREATE TABLE `filter_employment`
(
  `filter_id`     bigint       NOT NULL,
  `employment_id` varchar(255) NOT NULL,
  KEY             `FKn4fq8h337b504a2e7nidhcaqw` (`employment_id`),
  KEY             `FKlp1f7bhwq3crnyfo6d1nk01os` (`filter_id`)
);

CREATE TABLE `filter_expirience`
(
  `filter_id`     bigint       NOT NULL,
  `expirience_id` varchar(255) NOT NULL,
  KEY             `FK3u5nui129gstbasu1brsxu5qp` (`expirience_id`),
  KEY             `FKn3epi7v6ble43phgs3af8anjo` (`filter_id`)
);

CREATE TABLE `filter_schedule` (
  `filter_id` bigint NOT NULL,
  `schedule_id` varchar(255) NOT NULL,
  KEY `FKfpdpdgfw5uhxsf3ykdvyskmle` (`schedule_id`),
  KEY `FK74mdn3w72g1thk43817ewhmbt` (`filter_id`)
);


