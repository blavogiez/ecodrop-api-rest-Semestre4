--- Création de la base avec idempotence

create table WasteType (
    id serial primary key 
    nom varchar(30) not null,
    pointsPerKilo integer
)