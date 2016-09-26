#!bin/bash
./gradlew bootRepackage
docker build -t b4456609/easylearn-gateway:latest -t b4456609/easylearn-gateway:${1} .
