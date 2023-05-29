## ELIMINARE node-modules dalle dir 
FROM openjdk:12.0.2
EXPOSE 8038

ARG distrFile=./build/distributions/unibo.radargui23-1.0.tar
## ADD extracts the tar
ADD ${distrFile} ./
## ADD ./build/distributions/QakRestFacade-1.0.tar /
WORKDIR /unibo.radargui23-1.0/bin
COPY ./*.pl ./
COPY ./*.json ./
COPY ./*.txt ./
## COPY ./*.py ./

CMD ["bash", "./unibo.radargui23"]

## gradlew distTar
##  docker build -t radargui23:1.0 .
## PRIMA ATTIVAZIONE: non si apre l'immagine ... OCCORRE WEB?
## docker run -it --rm --name radargui23 -p8038:8038  --privileged radargui23:1.0  /bin/bash


## Tag the image
## type docker_password.txt | docker login --username natbodocker --password-stdin
## docker tag radargui23:1.0 natbodocker/radargui23:1.0
## Register the image on DockerHub
## docker push natbodocker/virtualrobotdisi23:1.0



#### docker run -p 8090:8090 -p 8091:8091 --rm virtualrobotdisi23:1.0
#### docker run -d -p 8090:8090 -p 8091:8091 --rm virtualrobotdisi23:1.0   (Detach mode)
#### docker cp sceneConfig.js  		( )
#### docker cp sceneConfig.js 23df308fae4e:/home/node/WEnv/WebGLScene/sceneConfig.js

### docker inspect --format='{{.Id}} {{.Parent}}' $(docker images --filter since=1cc99e24ab2d -q)
### It will kill every children

## docker-compose -f virtualrobotdisi23:1.0.yaml up