-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema belimo
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema belimo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `belimo` DEFAULT CHARACTER SET utf8 ;
USE `belimo` ;

-- -----------------------------------------------------
-- Table `belimo`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `belimo`.`user` (
                                               `id` INT(11) NOT NULL AUTO_INCREMENT,
                                               `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               `lastchanged` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                               `createduser` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                               `name` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                               `password` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                               `isadmin` TINYINT(4) NOT NULL,
                                               PRIMARY KEY (`name`),
                                               UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                               UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
                                               INDEX `createduser` (`createduser` ASC) VISIBLE,
                                               CONSTRAINT `createduser`
                                                   FOREIGN KEY (`createduser`)
                                                       REFERENCES `belimo`.`user` (`name`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 17
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `belimo`.`busline`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `belimo`.`busline` (
                                                  `id` INT(11) NOT NULL AUTO_INCREMENT,
                                                  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                  `lastchanged` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                  `createduser` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                                  `name` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                                  `bustype` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                                  PRIMARY KEY (`id`),
                                                  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                                  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
                                                  INDEX `createduser_idx` (`createduser` ASC) VISIBLE,
                                                  CONSTRAINT `createduser_busline_user`
                                                      FOREIGN KEY (`createduser`)
                                                          REFERENCES `belimo`.`user` (`name`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 11
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `belimo`.`site`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `belimo`.`site` (
                                               `id` INT(11) NOT NULL AUTO_INCREMENT,
                                               `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               `lastchanged` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                               `createduser` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                               `name` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                               `street` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                               `zipcode` VARCHAR(5) NOT NULL,
                                               `city` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                               `timezone` VARCHAR(45) NOT NULL,
                                               PRIMARY KEY (`id`),
                                               UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                               UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
                                               INDEX `createduser_idx` (`createduser` ASC) VISIBLE,
                                               CONSTRAINT `createduser_site_user`
                                                   FOREIGN KEY (`createduser`)
                                                       REFERENCES `belimo`.`user` (`name`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 11
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `belimo`.`logrecord`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `belimo`.`logrecord` (
                                                    `id` INT(11) NOT NULL AUTO_INCREMENT,
                                                    `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                    `lastchanged` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                    `createduser` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                                    `unique_identifier` VARCHAR(45) CHARACTER SET 'utf8' NULL DEFAULT NULL,
                                                    `timestamp` DATETIME NOT NULL,
                                                    `site` INT(11) NULL DEFAULT NULL,
                                                    `busline` INT(11) NULL DEFAULT NULL,
                                                    `address` INT(11) NOT NULL,
                                                    `milliseconds` INT(11) NOT NULL,
                                                    `type` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
                                                    `source` VARCHAR(45) NOT NULL,
                                                    `message` LONGTEXT CHARACTER SET 'utf8' NOT NULL,
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                                    UNIQUE INDEX `unique_identifier_UNIQUE` (`unique_identifier` ASC) VISIBLE,
                                                    INDEX `createduser_idx` (`createduser` ASC) VISIBLE,
                                                    INDEX `busline_idx` (`busline` ASC) VISIBLE,
                                                    INDEX `site_idx` (`site` ASC) VISIBLE,
                                                    CONSTRAINT `busline`
                                                        FOREIGN KEY (`busline`)
                                                            REFERENCES `belimo`.`busline` (`id`),
                                                    CONSTRAINT `createduser_logrecord_user`
                                                        FOREIGN KEY (`createduser`)
                                                            REFERENCES `belimo`.`user` (`name`),
                                                    CONSTRAINT `site`
                                                        FOREIGN KEY (`site`)
                                                            REFERENCES `belimo`.`site` (`id`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 335
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `belimo`.`reportsettings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `belimo`.`reportsettings` (
                                                         `id` INT(11) NOT NULL AUTO_INCREMENT,
                                                         `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                         `lastchanged` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                         `createduser` VARCHAR(45) CHARACTER SET 'utf8' NULL DEFAULT NULL,
                                                         `name` VARCHAR(45) CHARACTER SET 'utf8' NULL DEFAULT NULL,
                                                         `json` JSON NOT NULL,
                                                         PRIMARY KEY (`id`),
                                                         UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                                         UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
                                                         INDEX `createduser_idx` (`createduser` ASC) VISIBLE,
                                                         CONSTRAINT `createduser_reportsettings_user`
                                                             FOREIGN KEY (`createduser`)
                                                                 REFERENCES `belimo`.`user` (`name`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;