CREATE TABLE `customer` (
  `id` mediumint(8) unsigned NOT NULL auto_increment,
  `firstName` varchar(255) default NULL,
  `surname` varchar(255) default NULL,
  `age` int default NULL,
  `salary` varchar(100) default NULL,
  `city` varchar(255),
  `email` varchar(100) default NULL,
  `birthDate` date,
  `description` varchar(255) default NULL,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT=1;