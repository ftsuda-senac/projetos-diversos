DROP TABLE IF EXISTS PESSOA_INTERESSE;
DROP TABLE IF EXISTS FOTO_PESSOA;
DROP TABLE IF EXISTS INTERESSE;
DROP TABLE IF EXISTS PESSOA;

create table pessoa (
    id integer generated by default as identity,
    nome varchar(100) not null,
    apelido varchar(100) not null,
    descricao varchar(1000),
    data_nascimento date not null,
    email varchar(100),
    telefone varchar(20),
    senha varchar(255),
    numero integer not null check (numero>=1 AND numero<=99),
    altura decimal(3,2) check (altura>=0 AND altura<=3),
    peso decimal(4,1) check (peso>=0 AND peso<=500),
    genero integer not null,
    cadastro_ativo boolean not null default true,
    data_cadastro timestamp not null,
    constraint pk_pessoa primary key (id),
    constraint unq_pessoa_apelido unique (apelido)
);

create table foto_pessoa (
    id integer generated by default as identity,
    nome_arquivo varchar(500) not null,
    legenda varchar(1000),
    pessoa_id integer,
    constraint pk_foto_pessoa primary key (id),
    constraint unq_foto_pessoa_nome_arquivo unique (nome_arquivo),
    constraint fk_foto_pessoa__pessoa foreign key (pessoa_id) references pessoa(id)
);

create table interesse (
    id integer generated by default as identity,
    nome varchar(100) not null,
    constraint pk_interesse primary key (id),
    constraint unq_interesse_nome unique (nome)
);

create table pessoa_interesse (
    pessoa_id integer not null,
    interesse_id integer not null,
    constraint pk_pessoa_interesse primary key (pessoa_id, interesse_id),
    constraint fk_pessoa_interesse__pessoa foreign key (pessoa_id) references pessoa(id),
    constraint fk_pessoa_interesse__interesse foreign key (interesse_id) references interesse(id)
);
