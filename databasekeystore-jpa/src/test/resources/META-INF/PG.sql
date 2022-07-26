create table keyentry(
id bigserial PRIMARY KEY,
alias varchar(255) not null,
entry TEXT not null
);

CREATE UNIQUE INDEX idx_alias ON keyentry(alias);