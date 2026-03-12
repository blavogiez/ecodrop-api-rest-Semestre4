INSERT INTO CollectionPoint(adresse, capaciteMax) VALUES ('21 Rue DuChemin', 500);
INSERT INTO CollectionPoint(adresse, capaciteMax) VALUES ('18 Avenue De L impasse', 300);
INSERT INTO CollectionPoint(adresse, capaciteMax) VALUES ('47 Route De La Ruelle', 600);
INSERT INTO CollectionPoint(adresse, capaciteMax) VALUES ('32 Chemin Avenue', 200);
INSERT INTO CollectionPoint(adresse, capaciteMax) VALUES ('2 Canal Concombre', 50);

INSERT INTO WasteType(nom, pointsPerKilo) VALUES ('batteries', 5.0);
INSERT INTO WasteType(nom, pointsPerKilo) VALUES ('textile', 3.0);
INSERT INTO WasteType(nom, pointsPerKilo) VALUES ('électronique', 6.0);
INSERT INTO WasteType(nom, pointsPerKilo) VALUES ('ampoules', 4.0);
INSERT INTO WasteType(nom, pointsPerKilo) VALUES ('bois', 2.0);
INSERT INTO WasteType(nom, pointsPerKilo) VALUES ('métaux', 1.0);
INSERT INTO WasteType(nom, pointsPerKilo) VALUES ('emballages toxiques', 7.0);
INSERT INTO WasteType(nom, pointsPerKilo) VALUES ('végétaux', 8.0);
INSERT INTO WasteType(nom, pointsPerKilo) VALUES ('radioactifs', 4.0);

INSERT INTO Accepts VALUES(1,1);
INSERT INTO Accepts VALUES(1,4);
INSERT INTO Accepts VALUES(1,5);
INSERT INTO Accepts VALUES(1,3);

INSERT INTO Accepts VALUES(2,7);
INSERT INTO Accepts VALUES(2,2);
INSERT INTO Accepts VALUES(2,5);
INSERT INTO Accepts VALUES(2,6);

INSERT INTO Accepts VALUES(3,1);
INSERT INTO Accepts VALUES(3,4);
INSERT INTO Accepts VALUES(3,5);
INSERT INTO Accepts VALUES(3,2);

INSERT INTO Accepts VALUES(4,9);
INSERT INTO Accepts VALUES(4,8);
INSERT INTO Accepts VALUES(4,7);
INSERT INTO Accepts VALUES(4,3);

INSERT INTO Accepts VALUES(5,4);
INSERT INTO Accepts VALUES(5,5);
INSERT INTO Accepts VALUES(5,7);
INSERT INTO Accepts VALUES(5,1);

INSERT INTO Users(login, password, role) VALUES('paulpaulpaul', 'paulpaulpaul', 'ADMIN');
INSERT INTO Users(login, password, role) VALUES('podmanpodman', 'podmanpodman', 'USER');
INSERT INTO Users(login, password, role) VALUES('podman-compose', 'podman-compose', 'USER');

INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(1, 1, 1, 2.5);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(2, 1, 4, 1.8);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(3, 1, 5, 3.2);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(1, 1, 3, 0.9);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(2, 2, 7, 5.1);

INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(3, 2, 2, 2.3);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(1, 2, 5, 4.7);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(2, 2, 6, 1.2);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(3, 3, 1, 3.5);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(1, 3, 4, 2.1);

INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(2, 3, 5, 1.6);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(3, 3, 2, 4.3);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(1, 4, 9, 0.7);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(2, 4, 8, 6.2);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(3, 4, 7, 2.9);

INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(1, 4, 3, 1.4);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(2, 5, 4, 3.8);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(3, 5, 5, 2.2);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(1, 5, 7, 4.5);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(2, 5, 1, 1.9);

INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(3, 1, 1, 5.3);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(1, 2, 2, 0.8);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(2, 3, 1, 2.4);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(3, 4, 8, 3.6);
INSERT INTO Deposit(userId, pointId, wasteTypeId, poids) VALUES(1, 5, 4, 1.1);