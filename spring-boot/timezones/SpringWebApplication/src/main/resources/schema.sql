CREATE DATABASE IF NOT EXISTS timezonedb;
USE timezonedb;

CREATE TABLE IF NOT EXISTS `timezonedb`.`date_time_tbl` (
    `id`INT NOT NULL AUTO_INCREMENT,
    `date_str` VARCHAR(500) NULL,
    `date_time` DATETIME NULL,
    `local_time` TIME NULL,
    `local_date`  DATE NULL,
    `local_datetime_dt` DATETIME NULL,
    `offset_datetime` TIMESTAMP NULL,
    `zoned_datetime` TIMESTAMP NULL,
    `created_at` TIMESTAMP NOT NULL,

    PRIMARY KEY (`id`));