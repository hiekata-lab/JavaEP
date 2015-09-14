-- release_20141017のDBにカラムを追加するコマンド
-- alter table users add exam_score smallint unsigned default 0 after score;
-- create table students_status_exam (
--	  username varchar(50) not null,
--	  question_id smallint(3) unsigned not null,
--	  source text,
--	  passed tinyint(1) not null,
--	  primary key (username, question_id),
--	  foreign key (username) references users (username),
--	  foreign key (question_id) references questions (id)
-- );
-- alter table qandas add like_count smallint(3) unsigned not null default 0 after solved;

-- release_20141107のDBにカラムを追加するコマンド
-- alter table questions add object_oriented tinyint(1) default 0;
-- alter table questions add class_name varchar(64) default null;

-- release_20141118のDBにカラムを追加するコマンド
-- alter table questions add shown_in_exam tinyint(1) default 1;

drop database javaep;

create database javaep;
use javaep;

grant all on javaep.* to javaep@localhost identified by 'javaep';

create table users (
	username varchar(50) not null,
	password varchar(64) not null,
	score smallint unsigned default 0,
	exam_score smallint unsigned default 0,
	enabled tinyint(1) not null,
	primary key (username)
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	unique key ix_auth_username (username, authority),
	constraint fk_authorities_users foreign key (username) references users (username)
);

create table questions (
	id smallint(3) unsigned not null auto_increment,
	difficulty tinyint unsigned not null,
	content text not null,
	args text not null,
	answers text not null,
	source text not null,
	object_oriented tinyint(1) default 0,
	class_name varchar(64) default null,
	shown_in_exam tinyint(1) default 1,
	primary key (id)
);

create table students_status (
	username varchar(50) not null,
	question_id smallint(3) unsigned not null,
	source text,
	passed tinyint(1) not null,
	primary key (username, question_id),
	foreign key (username) references users (username),
	foreign key (question_id) references questions (id)
);

create table students_status_exam (
	username varchar(50) not null,
	question_id smallint(3) unsigned not null,
	source text,
	passed tinyint(1) not null,
	primary key (username, question_id),
	foreign key (username) references users (username),
	foreign key (question_id) references questions (id)
);

create table qandas (
    id int unsigned not null auto_increment,
    question_id smallint(3) unsigned not null,
    username varchar(50) not null,
    source text,
    console text,
    question_comment text,
    response_comment text,
    responsed_flag tinyint(1),
    solved_flag tinyint(1),
    respondant varchar(50),
    created TIMESTAMP,
    responsed TIMESTAMP,
    solved TIMESTAMP,
    like_count smallint(3) unsigned not null default 0,
    primary key (id),
    foreign key (username) references users (username),
    foreign key (question_id) references questions (id)
);

insert into users (username, password, enabled) values (
	'admin',
	'8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
	1
);

insert into authorities (username, authority) values (
	'admin',
	'ROLE_ADMIN'
);