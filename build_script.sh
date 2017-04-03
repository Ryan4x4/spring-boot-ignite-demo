#!/usr/bin/env bash

projectNamespace=sageburner.com:5000
projectName=sbd-credit_card
projectQualifiedName=$projectNamespace/$projectName

# GRADLE
export spring_profiles_active=dev
sh ./gradlew build

# DOCKER
containerId=$(docker ps -q -a --filter="name=$projectName")

# If docker container is running, stop and remove it
if [[ ! -z $containerId ]]
then
    docker stop $containerId
    docker rm $containerId
fi

# Build Docker image
docker build -t $projectQualifiedName .

# Run Docker container with new image
docker run -d --name $projectName -p 8080:8080 $projectQualifiedName
