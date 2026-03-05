URL="http://localhost:8080/ecodrop/waste-types"

curl -i -H 'Content-Type: application/json' \
-d '{"id":5,"nom":"Je suis un nom modifié","pointsPerKilo":2}' \
-X PUT $URL/2

curl -i -H 'Content-Type: application/json' \
-d '{"id":5,"nom":"Je suis un nouveau nom","pointsPerKilo":2}' \
-X POST $URL

curl -i -H 'Content-Type: application/json' \
-X DELETE $URL/2