SET SQL_SAFE_UPDATES=0;

alter table medicos add ativo tinyint;

update medicos set ativo = 1;
