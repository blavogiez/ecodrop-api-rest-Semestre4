# RANDOM=f2d1cb28-8477-4120-a638-d4438089cb86

# curl "http://localhost:8080/TP06/etudiants1?token=${RANDOM}"

# BASIC=amVhbjpqZWFu

# curl "http://localhost:8080/TP06/etudiants2" -H "Authorization: Basic ${BASIC}"
# curl -u paul:paul http://localhost:8080/TP06/etudiants2

JWT=eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIzYmIyMjZiMWJiNDE0YWRjYmU0Y2JlYWM3YTA3ZWIyMCIsImlhdCI6MTc3MTQwNjM3Mywic3ViIjoiQXV0aGVudGlmaWNhdGlvbiBwb3VyIHRwMzMzIiwiaXNzIjoicGhpbGlwcGUubWF0aGlldUB1bml2LWxpbGxlLmZyIiwiZXhwIjoxNzcxNDA3NTczfQ.Z3Hn46IPe-wq5uGRGyaxfx2dwcDyXGuMPNEdxWaVvz2kJOdXTDPar26N23kezEbi

curl "http://localhost:8080/TP06/etudiants3" -H "Authorization: Bearer ${JWT}"