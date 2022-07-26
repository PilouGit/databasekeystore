create table keyentry(
id bigint   AUTO_INCREMENT PRIMARY KEY,
alias varchar(255) not null,
entry LONGTEXT not null
);

CREATE UNIQUE INDEX idx_alias ON keyentry(alias);