-- -----------------------------------------------------
-- Schema WaterWatchDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `WaterWatchDB` DEFAULT CHARACTER SET utf8 ;
USE `WaterWatchDB` ;

-- -----------------------------------------------------
-- Table `WaterWatchDB`.`Reservatorio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WaterWatchDB`.`Reservatorio` (
  `idReservatorio` INT UNSIGNED NOT NULL,
  `Nome` VARCHAR(40) NOT NULL,
  `Localizacao` VARCHAR(40) NOT NULL,
  `VolumeMax` DECIMAL(12,3) UNSIGNED NOT NULL,
  `Vazao` DECIMAL(12,3) UNSIGNED NULL,
  `AreaDrenagem` DECIMAL(12,3) UNSIGNED NULL,
  `EspelhoAgua` DECIMAL(12,3) UNSIGNED NULL,
  PRIMARY KEY (`idReservatorio`),
  UNIQUE INDEX `idReservatorio_UNIQUE` (`idReservatorio` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WaterWatchDB`.`PluviometriaDiaria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WaterWatchDB`.`PluviometriaDiaria` (
  `idPluviometriaDiaria` BIGINT UNSIGNED NOT NULL,
  `idReservatorio` INT UNSIGNED NOT NULL,
  `Data` DATE NOT NULL,
  `VolumeDiario` DECIMAL(5,2) UNSIGNED NOT NULL,
  `QuantidadeChuva` DECIMAL(9,3) UNSIGNED NOT NULL,
  PRIMARY KEY (`idPluviometriaDiaria`),
  UNIQUE INDEX `idPluviometriaDiaria1_UNIQUE` (`idPluviometriaDiaria` ASC),
  INDEX `fk_PluviometriaDiaria_Reservatorio_idx` (`idReservatorio` ASC),
  CONSTRAINT `fk_PluviometriaDiaria_Reservatorio`
    FOREIGN KEY (`idReservatorio`)
    REFERENCES `WaterWatchDB`.`Reservatorio` (`idReservatorio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WaterWatchDB`.`Municipio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WaterWatchDB`.`Municipio` (
  `idMunicipio` BIGINT UNSIGNED NOT NULL,
  `Nome` VARCHAR(40) NOT NULL,
  `QuantidadeHabitantes` BIGINT UNSIGNED NULL,
  `AreaKmQuad` DECIMAL(12,3) UNSIGNED NULL,
  PRIMARY KEY (`idMunicipio`),
  UNIQUE INDEX `idMunicipios_UNIQUE` (`idMunicipio` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `WaterWatchDB`.`Abastecimento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WaterWatchDB`.`Abastecimento` (
  `idReservatorio` INT UNSIGNED NOT NULL,
  `idMunicipio` BIGINT UNSIGNED NOT NULL,
  `ResidenciasAbastecidas` BIGINT UNSIGNED NULL,
  `AreaAbastecidaKmQuad` DECIMAL(12,3) UNSIGNED NULL,
  PRIMARY KEY (`idReservatorio`, `idMunicipio`),
  INDEX `fk_Reservatorio_has_Municipio_Municipio1_idx` (`idMunicipio` ASC),
  INDEX `fk_Reservatorio_has_Municipio_Reservatorio1_idx` (`idReservatorio` ASC),
  UNIQUE INDEX `idMunicipio_UNIQUE` (`idMunicipio` ASC),
  CONSTRAINT `fk_Reservatorio_has_Municipio_Reservatorio1`
    FOREIGN KEY (`idReservatorio`)
    REFERENCES `WaterWatchDB`.`Reservatorio` (`idReservatorio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reservatorio_has_Municipio_Municipio1`
    FOREIGN KEY (`idMunicipio`)
    REFERENCES `WaterWatchDB`.`Municipio` (`idMunicipio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;