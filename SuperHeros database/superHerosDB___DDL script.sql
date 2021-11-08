-- -----------------------------------------------------
-- DATABASE superHeroSightings
-- -----------------------------------------------------
DROP DATABASE IF EXISTS superHeroSightings ;

-- -----------------------------------------------------
-- DATABASE superHeroSightings
-- -----------------------------------------------------
CREATE DATABASE  superHeroSightings ;
USE superHeroSightings;

-- -----------------------------------------------------
-- Table superPower
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS superPower ;

CREATE TABLE IF NOT EXISTS superPower (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(100) NOT NULL
);


-- -----------------------------------------------------
-- Table superCreature
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS superCreature ;

CREATE TABLE IF NOT EXISTS superCreature (
  id INT  PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `type` VARCHAR(50) NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  superPower_id INT NOT NULL,
FOREIGN KEY (superPower_id) REFERENCES superPower(id)
);


-- -----------------------------------------------------
-- Table address
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS address ;

CREATE TABLE IF NOT EXISTS address (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `street` VARCHAR(100) NOT NULL,
  `city` VARCHAR(50) NOT NULL,
  `state` VARCHAR(50) NOT NULL,
  `country` VARCHAR(50) NOT NULL,
  `postalCode` CHAR(6) NOT NULL
  );

-- -----------------------------------------------------
-- Table location
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS location ;

CREATE TABLE IF NOT EXISTS location (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  `latitude` DECIMAL(7,5) NOT NULL,
  `longitude` DECIMAL(7,5) NOT NULL,
  address_id INT NOT NULL,
 FOREIGN KEY (address_id) REFERENCES address (id)
  );


-- -----------------------------------------------------
-- Table sighting
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS sighting ;

CREATE TABLE IF NOT EXISTS sighting (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `date` Date NOT NULL,
  `description` VARCHAR(100) NULL,
  superCreature_id INT NOT NULL,
  location_id INT NOT NULL,
FOREIGN KEY (superCreature_id) REFERENCES superCreature(id),
FOREIGN KEY (location_id) REFERENCES location(id)
);


-- -----------------------------------------------------
-- Table organisation
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS organisation ;

CREATE TABLE IF NOT EXISTS organisation (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(20) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  address_id INT NOT NULL,
 FOREIGN KEY (address_id) REFERENCES address (id)
  );


-- -----------------------------------------------------
-- Table superCreature_organisation
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS superCreature_organisation ;

CREATE TABLE IF NOT EXISTS superCreature_organisation (
  superCreature_id INT NOT NULL,
  organisation_id INT NOT NULL,
PRIMARY KEY (superCreature_id, organisation_id),
FOREIGN KEY (superCreature_id) REFERENCES superCreature(id),
FOREIGN KEY (organisation_id) REFERENCES organisation(id)
);

