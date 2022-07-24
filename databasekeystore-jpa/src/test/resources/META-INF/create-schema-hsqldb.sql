create schema databasekeystore;
create table databasekeystore.keyentry (id bigint not null, alias varchar(255) not null, entry varchar(255) not null, primary key (id));
