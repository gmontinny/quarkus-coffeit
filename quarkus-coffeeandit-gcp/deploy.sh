#!/bin/bash
mvn clean package
gcloud functions deploy httpFunction \
  --entry-point=br.com.coffeeandit.ValidarCPF \
  --runtime=java11 --trigger-http --source=target/deployment