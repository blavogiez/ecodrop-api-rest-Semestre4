#!/bin/bash

# sudo docker compose down -v && sudo docker compose up -d --build 

if [ $1 == "build" ] 2>/dev/null
then 
    podman-compose down -v && podman-compose up -d --build 
    if [ $2 == "test" ] 2>/dev/null
    then
        echo "Attente du démarrage de Tomcat..."
        until podman exec ecodrop_ecodrop_1 curl -sf http://localhost:8080/ > /dev/null 2>&1; do
            sleep 0.5
        done
        podman exec -e NODE_OPTIONS="--experimental-global-webcrypto" -it -w /bruno ecodrop_ecodrop_1 bru run --env local --tests-only
    fi
fi

if [ $1 == "test" ] 2>/dev/null
then
    podman exec -e NODE_OPTIONS="--experimental-global-webcrypto" -it -w /bruno ecodrop_ecodrop_1 bru run --env local --tests-only
fi