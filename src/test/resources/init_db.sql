
create table if not exists msg_sites (
id MEDIUMINT(8) NOT NULL AUTO_INCREMENT,
domain_name  varchar(40) not null,
site_name    varchar(40),
reg_url      varchar(255),
post_url     varchar(255),
primary key (id),
unique key (domain_name)
);

create table if not exists msg_requested (
id MEDIUMINT(8) NOT NULL AUTO_INCREMENT,
com_info_id  int not null,
created_time DATETIME NOT NULL,
modified_time DATETIME NOT NULL,
primary key (id),
unique key (com_info_id)
);

create table if not exists app_tasks (
id MEDIUMINT(8) NOT NULL AUTO_INCREMENT,
root_url  varchar(255) not null,
group_name  varchar(40),
order_num   int,
jclass      varchar(60),
status      varchar(20),
curr_url    varchar(255),
last_url    varchar(255),
created_time DATETIME NOT NULL,
modified_time DATETIME NOT NULL,
primary key (id)
);

create table if not exists com_info (
id MEDIUMINT(8) NOT NULL AUTO_INCREMENT,
region       varchar(10) not null,
category     varchar(20) not null,
sub_category varchar(30),
name         varchar(60) not null,
description  varchar(255),
web_url      varchar(255),
visit_cnt    int default 0,
from_url     varchar(255),
created_time DATETIME NOT NULL,
modified_time DATETIME NOT NULL,
primary key (id),
unique key (category,web_url)
);