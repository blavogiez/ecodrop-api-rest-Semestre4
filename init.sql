--- Création de la base avec idempotence
DROP TABLE IF EXISTS Accepts CASCADE;
DROP TABLE IF EXISTS WasteType CASCADE;
DROP TABLE IF EXISTS CollectionPoint CASCADE;

create table WasteType (
    id serial primary key,
    nom varchar(30) not null,
    pointsPerKilo float
);

CREATE TABLE CollectionPoint (
    id SERIAL PRIMARY KEY,
    adresse VARCHAR(30) NOT NULL,
    capaciteMax int NOT NULL
);

CREATE TABLE Accepts (
    pointid INT REFERENCES CollectionPoint(id) ON UPDATE CASCADE ON DELETE CASCADE,
    wastetypeid INT REFERENCES WasteType(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT pk_ids PRIMARY KEY (pointid, wastetypeid)
);

\i fill.sql;