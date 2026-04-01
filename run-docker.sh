#!/bin/bash

if [ "$1" == "build" ] 2>/dev/null
then
    docker compose --project-name ecodrop down -v && docker compose --project-name ecodrop up -d --build
    if [ "$2" == "test" ] 2>/dev/null
    then
        echo "Attente du démarrage de Tomcat..."
        until docker exec ecodrop-ecodrop-1 curl -sf http://localhost:8080/ecodrop > /dev/null 2>&1; do
            sleep 0.5
        done
        docker exec -w /bruno ecodrop-ecodrop-1 bru run --env local --tests-only
    fi
fi

if [ "$1" == "test" ] 2>/dev/null
then
    docker exec -w /bruno ecodrop-ecodrop-1 bru run --env local --tests-only
fi