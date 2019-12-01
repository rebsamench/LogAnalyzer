-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createduser` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `name` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `password` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `isadmin` TINYINT NOT NULL,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  PRIMARY KEY (`name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Site`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Site` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createduser` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `name` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `street` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `city` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `zipcode` VARCHAR(5) NOT NULL,
  `timezone` VARCHAR(45) NOT NULL,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  INDEX `createduser_idx` (`createduser` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  CONSTRAINT `createduser_site_user`
    FOREIGN KEY (`createduser`)
    REFERENCES `mydb`.`User` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Busline`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Busline` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createduser` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `name` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `bustype` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `createduser_idx` (`createduser` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  CONSTRAINT `createduser_busline_user`
    FOREIGN KEY (`createduser`)
    REFERENCES `mydb`.`User` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`LogRecord`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`LogRecord` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createduser` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `timestamp` DATETIME NOT NULL,
  `site` INT NULL,
  `busline` INT NULL,
  `address` INT NOT NULL,
  `milliseconds` INT NOT NULL,
  `type` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  `message` LONGTEXT CHARACTER SET 'utf8' NOT NULL,
  `unique_identifier` VARCHAR(45) CHARACTER SET 'utf8' NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `createduser_idx` (`createduser` ASC) VISIBLE,
  INDEX `busline_idx` (`busline` ASC) VISIBLE,
  INDEX `site_idx` (`site` ASC) VISIBLE,
  UNIQUE INDEX `unique_identifier_UNIQUE` (`unique_identifier` ASC) VISIBLE,
  CONSTRAINT `site`
    FOREIGN KEY (`site`)
    REFERENCES `mydb`.`Site` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `createduser_logrecord_user`
    FOREIGN KEY (`createduser`)
    REFERENCES `mydb`.`User` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `busline`
    FOREIGN KEY (`busline`)
    REFERENCES `mydb`.`Busline` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ReportSettings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ReportSettings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NULL,
  `createduser` VARCHAR(45) CHARACTER SET 'utf8' NULL,
  `name` VARCHAR(45) CHARACTER SET 'utf8' NULL,
  `json` JSON NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `createduser_idx` (`createduser` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  CONSTRAINT `createduser_reportsettings_user`
    FOREIGN KEY (`createduser`)
    REFERENCES `mydb`.`User` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
