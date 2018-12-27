use baidugushi;

CREATE TABLE `baidu_hot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title_line1` varchar(255) DEFAULT NULL,
  `title_line2` int(11) DEFAULT NULL,
  `title_line3` varchar(255) DEFAULT NULL,
  `title_line4` varchar(255) DEFAULT NULL,
  `dirver_thing` text,
  `hot_stock_name_1` varchar(255) DEFAULT NULL,
  `hot_stock_code_1` varchar(11) DEFAULT NULL,
  `hot_stock_price_1` double DEFAULT NULL,
  `hot_stock_increment_1` varchar(20) DEFAULT NULL,
  `hot_stock_name_2` varchar(255) DEFAULT NULL,
  `hot_stock_code_2` varchar(11) DEFAULT NULL,
  `hot_stock_price_2` double DEFAULT NULL,
  `hot_stock_increment_2` varchar(20) DEFAULT NULL,
  `hot_stock_name_3` varchar(255) DEFAULT NULL,
  `hot_stock_code_3` varchar(11) NOT NULL,
  `hot_stock_price_3` double DEFAULT NULL,
  `hot_stock_increment_3` varchar(20) DEFAULT NULL,
  `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


CREATE TABLE `stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stock_id` varchar(30) DEFAULT NULL,
  `data` json DEFAULT NULL,
  `insert_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

