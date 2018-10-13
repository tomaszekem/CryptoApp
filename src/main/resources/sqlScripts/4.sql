ALTER TABLE `cryptodb`.`encrypted_data`
ADD COLUMN `file_name` VARCHAR(50) NOT NULL AFTER `file_extension`,
ADD UNIQUE INDEX `file_name_UNIQUE` (`file_name` ASC);
