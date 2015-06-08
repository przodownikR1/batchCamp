CREATE TABLE `batchUser` (
  `id` mediumint(8) unsigned NOT NULL auto_increment,
  `login` varchar(50) default NULL,
  `loginDate` timestamp default NULL,
  `salary` DECIMAL default NULL,
  `city` varchar(255),
  `phone` varchar(15) default NULL,
  `logged` bit,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT=1;