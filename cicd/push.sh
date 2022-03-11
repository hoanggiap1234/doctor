#!/bin/sh
set -e

VERSION=$1

echo "Push image to registry server"
docker --config ~/.docker/.vinhtx3 push 10.60.156.72/telecare/doctor:$1
docker rmi -f 10.60.156.72/telecare/doctor:$1
echo "Finish push image to registry server"