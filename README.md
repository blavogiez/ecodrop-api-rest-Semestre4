# Ecodrop

## Notes pour Paul

- j'ai refactor la gestion de requêtes en mode chemin avec la classe RequestContext (c'est maintenant plus court de lire le chemin d'une requête (par exemple ecodrop/points/overloaded etc tu vois, ça rend les api rest plus claires))
https://gitlab.univ-lille.fr/baptiste.lavogiez.etu/ecodrop/-/commit/3cdcfe85c918570680ef6823333c7468ff4a3a04
- la même chose pour la lecture du body de la requête avec RequestUtils
- (si le nouveau code est pas clair tu peux faire comme avant)
- tu peux tester l'API entière avec :

```bash
npm install -g @usebruno/cli
docker compose up --build -d
cd bruno-clean && bru run --env local
```

- et à chaque fois que tu push les tests bruno vont se faire automatiquement dans gitlab. (Si environ 2 à 3 tests ratent c'est pas grave ça peut être un problème de configuration vu que c'est récent)
- tu n'es pas obligé d'inclure des tests/assertion aux nouvelles requêtes bruno que tu fais pour la semaine 3 je peux le faire après tqt

## Membres de l'équipe
Réalisé par :
- **Paul-Arnaud Delavictoire** : [paul-arnaud.delavictoire.etu@univ-lille.fr](mailto:paul-arnaud.delavictoire.etu@univ-lille.fr)  
- **Baptiste Lavogiez** : [baptiste.lavogiez.etu@univ-lille.fr](mailto:baptiste.lavogiez.etu@univ-lille.fr)  

## Liens utiles

Mme Everaere : 

| Code |
|------|
| [Répertoire principal](tomcat/webapps/ecodrop/WEB-INF/src) |
| [Répertoire de tests](bruno-clean) |
| CI/CD ; tests à la volée : voir l'historique des commits |

## Schéma de la base 

todo

## Explication des requêtes complexes

todo

## Retour de l'API

todo