# Ecodrop : Le service de recyclage RESTful

## Membres de l'équipe

**Groupe G1**

Réalisé par :
- **Paul-Arnaud Delavictoire** : [paul-arnaud.delavictoire.etu@univ-lille.fr](mailto:paul-arnaud.delavictoire.etu@univ-lille.fr)  
- **Baptiste Lavogiez** : [baptiste.lavogiez.etu@univ-lille.fr](mailto:baptiste.lavogiez.etu@univ-lille.fr)  
- ***Enseignant : Mme Everaere***

## Présentation

Ecodrop est le terrain d'apprentissage parfait pour concevoir une API REST sécurisée, résiliente et testable. Ce service de collecte de déchets permet d'obtenir des points de recyclage où sont déposés des déchets par des utilisateurs. Par l'API REST, différents services externes pourront alors obtenir, injecter ou modifier un [ensemble de données](#schéma-de-la-base). 

### Stack tecknique

- Java EE, Tomcat
- PostgreSQL
- GitLab CI
- Docker Compose

## Liens utiles

Mme Everaere : 

| Code |
|------|
| [Répertoire principal](tomcat/webapps/ecodrop/WEB-INF/src) |
| [Répertoire de tests](bruno-clean) |
| CI ; tests à la volée -> voir l'historique des commits |

## Schéma de la base 

todo

## Explication des requêtes complexes

todo

## Retours de l'API

| Endpoint | Headers fournis par l'utilisateur | Code de retour |
| DELETE | Token user | 401 |

## Les petits plus

- Tests `bruno` disponibles avec `Bruno CLI` -> `cd bruno && bru run --env local --tests-only` ;
- Conteneurisation de l'application pour faciliter le développement / tests, notamment vis-à-vis de l'idempotence des tests ;
- CI GitLab de tests automatiques à chaque push : construction de l'image, lancement et test sur runner hébergé sur Dattier (pas de runner privilégié dispo sur le GitLab universitaire). Voir les commits et la petite croix à côté pour détails ! ;
- Chiffrement SHA256 des mots de passe avec l'extension `pgcrypto`.