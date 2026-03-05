URL="http://localhost:8080/ecodrop/waste-types"

# JSON

curl -i -H 'Content-Type: application/json' \
-d '{"id":5,"nom":"Je suis un nom modifié JSON","pointsPerKilo":2}' \
-X PUT $URL/2

curl -i -H 'Content-Type: application/json' \
-d '{"id":5,"nom":"Je suis un nouveau nom JSON","pointsPerKilo":2}' \
-X POST $URL

curl -i -H 'Content-Type: application/json' \
-X DELETE $URL/2

# XML                                                                                                                                                       

curl -i -H 'Content-Type: application/xml' \                                                                                                                
-d '<wasteType><id>5</id><nom>Je suis un nom modifié XML</nom><pointsPerKilo>2</pointsPerKilo></wasteType>' \
-X PUT $URL/2

curl -i -H 'Content-Type: application/xml' \
-d '<wasteType><id>5</id><nom>Je suis un nouveau nom XML</nom><pointsPerKilo>2</pointsPerKilo></wasteType>' \
-X POST $URL

curl -i -H 'Content-Type: application/xml' \
-X DELETE $URL/2
