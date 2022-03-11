#!/bin/sh
set -e
echo "Start build maven"
mvn clean install
cp target/serviceTelecare-0.0.1-SNAPSHOT.jar cicd/
cp SmsTool.jar cicd/
echo "Start Build Docker images"
cd cicd/
docker build -t 10.60.156.72/telecare/doctor:$1 .


