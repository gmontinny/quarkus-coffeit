#!/bin/bash
./mvnw quarkus:add-extension -Dextensions="smallrye-fault-tolerance"

curl -v --location --request POST 'http://localhost:8082/v1/limite/' \
 --header 'Content-Type: application/json' \
 --data-raw '{"agencia": 209,   "conta": 78592,   "valor": 20000}'

curl -v --location --request DELETE 'http://localhost:8082/v1/limite/2' \
 --header 'Content-Type: application/json'
