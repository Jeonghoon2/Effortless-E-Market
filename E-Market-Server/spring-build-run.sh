#!/bin/bash

IMAGE_NAME="spring-server"
TAG="latest"

PROJECT_NAME="server_app_1"

# DockerFile
BUILD_PATH="Springimage.dockerfile"

# Docker 이미지를 재빌드합니다.
echo "Building Docker image..."
docker build -t ${IMAGE_NAME}:${TAG} -f ${BUILD_PATH}

# 기존에 실행 중인 컨테이너를 멈추고 삭제합니다.
echo "Stopping and removing existing container..."
docker-compose -p ${PROJECT_NAME} down

# docker-compose를 사용하여 서비스를 다시 시작합니다.
echo "Starting new container with docker-compose..."
docker-compose -p ${PROJECT_NAME} up -d

echo "Deployment completed."
