#!/bin/bash

# sudo docker compose down -v && sudo docker compose up -d --build 
podman-compose down -v && podman-compose up -d --build 

if [ $1 == "--test" ] 2>/dev/null
then 
    podman exec -it -w /bruno-clean ecodrop_ecodrop_1 bru run --env local
fi