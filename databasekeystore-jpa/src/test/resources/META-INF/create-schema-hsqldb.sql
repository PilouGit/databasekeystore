create schema databasekeystore;

create table databasekeystore.keyentry(id bigint not null  GENERATED BY DEFAULT AS IDENTITY,alias varchar(255) not null, entry LONGVARCHAR not null,  primary key (id));
