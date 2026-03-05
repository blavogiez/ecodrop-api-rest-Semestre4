--- Création de la base avec idempotence
DROP TABLE IF EXISTS Accepts CASCADE;
DROP TABLE IF EXISTS WasteType CASCADE;
DROP TABLE IF EXISTS CollectionPoint CASCADE;

create table WasteType (
    id serial primary key 
);

CREATE TABLE CollectionPoint (

)