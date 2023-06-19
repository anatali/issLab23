## ----------------------------------------------------------
## Dockerfile for unibo.basicrobot22
## ----------------------------------------------------------

FROM openjdk:12.0.2
EXPOSE 8085
## ADD extracts the tar
ADD ./build/distributions/webrobot23-1.0.tar /
WORKDIR /webrobot23-1.0/bin
COPY ./*.pl ./
COPY ./*.json ./
COPY ./*.bin ./
COPY ./*.txt ./


CMD ["bash", "webrobot23"]

## 1) gradlew distTar
## 2) docker build -t webrobot23:1.0 .
## docker tag webrobot23:1.0 natbodocker/webrobot23:1.0
## docker run -it --rm --name webrobot23 -p8085:8085/tcp --privileged webrobot23:1.0  /bin/bash

#type docker_password.txt | docker login --username natbodocker --password-stdin
#docker login --username natbodocker --password xyz
#docker push natbodocker/webrobot23:1.0