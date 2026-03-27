#!/bin/bash

# sudo docker compose down -v && sudo docker compose up -d --build 

if [ $1 == "build" ] 2>/dev/null
then 
    podman-compose down -v && podman-compose up -d --build 
    if [ $2 == "and-test" ] 2>/dev/null
    then 
        podman exec -it -w /bruno-clean ecodrop_ecodrop_1 bru run --env local
    fi
fi

if [ $1 == "only-test" ] 2>/dev/null
then 
    podman exec -it -w /bruno-clean ecodrop_ecodrop_1 bru run --env local
fi