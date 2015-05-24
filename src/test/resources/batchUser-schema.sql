CREATE TABLE `batchUser` (
  `id` mediumint(8) unsigned NOT NULL auto_increment,
  `login` varchar(255) default NULL,
  `loginDate` date,
  `salary` DECIMAL default NULL,
  `phone` varchar(12),
  
  `logged` bit(1) default NULL,
  
  PRIMARY KEY (`id`)
) AUTO_INCREMENT=1;