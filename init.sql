--- Création de la base avec idempotence
DROP TABLE IF EXISTS Accepts CASCADE;
DROP TABLE IF EXISTS WasteType CASCADE;
DROP TABLE IF EXISTS CollectionPoint CASCADE;
DROP TABLE IF EXISTS Users CASCADE ;
DROP TABLE IF EXISTS Deposit CASCADE ;

create table WasteType (
    id serial primary key,
    nom varchar(30) not null,
    pointsPerKilo float
);

CREATE TABLE CollectionPoint (
    id SERIAL PRIMARY KEY,
    adresse VARCHAR(30) NOT NULL,
    capaciteMax FLOAT NOT NULL
);

CREATE TABLE Accepts (
    pointid INT REFERENCES CollectionPoint(id) ON UPDATE CASCADE ON DELETE CASCADE,
    wastetypeid INT REFERENCES WasteType(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT pk_ids PRIMARY KEY (pointid, wastetypeid)
);

CREATE TABLE Users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL CHECK (char_length(password) > 10),
    role VARCHAR(10) NOT NULL CHECK (role IN ('USER','ADMIN'))
);


create table Deposit (
    id serial primary key,
    userId int references Users(id) on update cascade on delete cascade,
    pointId int references CollectionPoint(id) on update cascade on delete cascade,
    wasteTypeId int references WasteType(id) on update cascade on delete cascade,
    poids FLOAT not null,
    datedepot DATE default now(),
    collecte BOOLEAN default false
);




\i fill.sql;
