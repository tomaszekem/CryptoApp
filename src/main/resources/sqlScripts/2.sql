CREATE TABLE `cryptodb`.`encrypted_data` (
  `id` INT NOT NULL,
  `data` TEXT NOT NULL,
  `file_extension` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`));

SET GLOBAL time_zone = '+3:00';

ALTER TABLE `cryptodb`.`encrypted_data`
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;
