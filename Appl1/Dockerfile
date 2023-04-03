## ----------------------------------------------------------
## Dockerfile for Appl1HTTPSprint3
## ----------------------------------------------------------

FROM openjdk:12.0.2
EXPOSE 8030
## ADD extracts the tar
ADD ./application1/build/distributions/application1-1.0.tar /
WORKDIR /application1-1.0/bin
#COPY ./*.pl ./
COPY ./*.json ./
## COPY ./*.py ./


CMD ["bash", "./application1"]
### CMD ["bash" ]

## gradlew distTar
##  docker build -t appl1httpsprint3:1.0 .
## OCCORRE APRIRE UDP PER PERMETTERE COAP
## docker run -it  --rm --name appl1httpsprint3 -p8030:8030/tcp  --privileged appl1httpsprint3:1.0  /bin/bash

## COPIARE FILE CONFIG IN CONTAINER!
## docker cp sprint3Config.json ca26a27eae1b:/application1-1.0/bin/sprint3Config.json

## ESPERIMENTO
## docker run --link itunibovirtualrobot2023_default:wenv -it --net appl1_default appl1httpsprint3:1.0  ???

## docker-compose -f appl1httpsprint3.yaml up

#type docker_password.txt | docker login --username natbodocker --password-stdin
#docker login --username natbodocker --password xyz
#docker push natbodocker/appl1httpsprint3:1.0