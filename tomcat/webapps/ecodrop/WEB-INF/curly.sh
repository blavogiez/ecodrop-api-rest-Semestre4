WT_URL="http://localhost:8080/ecodrop/waste-types"
CP_URL="http://localhost:8080/ecodrop/points"

# WasteType

# JSON

# curl -i -H 'Content-Type: application/json' \
# -d '{"id":5,"nom":"Je suis un nom modifié JSON","pointsPerKilo":2}' \
# -X PUT $WT_URL/2

# curl -i -H 'Content-Type: application/json' \
# -d '{"id":5,"nom":"Je suis un nouveau nom JSON","pointsPerKilo":2}' \
# -X POST $WT_URL

# curl -i -H 'Content-Type: application/json' \
# -X DELETE $WT_URL/2

# # XML                                                                                                                                                       

# curl -i -H 'Content-Type: application/xml' \                                                                                                                
# -d '<wasteType><id>5</id><nom>Je suis un nom modifié XML</nom><pointsPerKilo>2</pointsPerKilo></wasteType>' \
# -X PUT $WT_URL/2

# curl -i -H 'Content-Type: application/xml' \
# -d '<wasteType><id>5</id><nom>Je suis un nouveau nom XML</nom><pointsPerKilo>2</pointsPerKilo></wasteType>' \
# -X POST $WT_URL

# curl -i -H 'Content-Type: application/xml' \
# -X DELETE $WT_URL/2

# CollectionPoint

# curl -i -H 'Content-Type: application/json' \
# -d '{"adresse":"Je suis une nouvelle adresse"}' \
# -X PATCH $CP_URL/3

curl -i -H 'Content-Type: application/json' \
-d '{"capaciteMax":700}' \
-X PATCH $CP_URL/3