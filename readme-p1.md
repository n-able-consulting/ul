# For Trainer Developing with Virtualization - dev with virt2
----------------------------------------------------------------
Start Project (minimal form from gitlab, Before first dockerfile build, commit: 26bcf1a2)

## First we will explain an container, and show that it is a server...

Explain that we run containers with Docker. Docker is the most populair or know way to run containers.
But they will see later that it is just one way of running containers

### Stopping the MySql server on the demo laptop

- [ ] Stop the MySql via MAMP

**If not done earlier: show local docker install and no running containers, two ways:**
- [ ] via cli
```
docker ps
```
- [ ] visual studio docker plugin

### Running an MySql server as a container

#### Show docker hub 

As a Image Registry one of the registry sources of Containers, but the one docker default looks for containers.
Show that their a MySql container can be found, by just searching on mysql.

- [ ] Take a moment to show the docker cli.

- [ ] Start the MySql container
```
docker run -d -p 3306:3306 --name=docker-mysql --env="MYSQL_ROOT_PASSWORD=root" --env="MYSQL_PASSWORD=root" --env="MYSQL_DATABASE=users" mysql
```

- [ ] When running jump into the server to show this container is a computer/server (but mini) just as your own laptop...
```
docker exec -it docker-mysql bash
```
# Running the UL (springboot) Rest Application in Docker

## Building and storing application container image(s)

**Explain Java - JIT Language and precompiling...**
- [ ] Build Jar (in demo dir of with ./demo/...):
```
./mvn install
```

- [ ] add first dockerfile and explain it:
```
FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

- [ ] Build the UL container for the first time with alpine tag and version number: 
```
docker build --build-arg JAR_FILE=target/*.jar -t enabledocker/ul:0.1-17-jdk-alpine .
docker build --build-arg JAR_FILE=target/*.jar -t enabledocker/ul .
```

- [ ] show docker images localy available:
 * Via Visual Studio Code
 * docker cli:
 ```
 docker images
 ```

- [ ] log into dockerhub: 
```
docker login --username enabledocker
```

- [ ] push current images to docker hub: 
```
docker push enabledocker/ul:latest
docker push enabledocker/ul:0.1-17-jdk-alpine
```
## Running the container app

- [ ] Run container 
```
docker run enabledocker/ul:latest
```
Crashes: **ask WHY?**
This is virtualization: your have seen how a container virualises an server. But remember the definition of Virualization: CPU, Networking & Storage. Their is a network virtualization thing going on.

### Lets create our own docker network

We will create a docker network and bind both the mysql container and our app-container to it.

- [ ] show the available docker networks
```
docker network ls
```
- [ ] lets create our own network
```
docker network create training
docker network ls
```
- [ ] bind the MySql container to the network
```
docker network connect training docker-mysql
```
**How would it work for the UL container if its needs to talk to the MySQl container?**

### I show you by experimenting with a generic Ubuntu(which is a Linux) server
- [ ] Start an ubuntu container
```
docker run --net training ubuntu
#aks whats happening and explain that a container always must have
#an active process, otherwise it will shutdown
#(run to completion) immediatly

#So explain that you will run the ubuntu container 
#by jumping into the server, what 
#is like logging on, so it will stay alive
docker run -it --net training -e HALLO="hello training: i am an environment variable" ubuntu /bin/bash

```
- [ ] explain the addition of commandline arguments: we added the container to the network & we added an environment variable from the commandline (as argument).
- [ ] in the container:
-install ping and the mysql-client
-show that the HELLO string is an environment variable
-ping the docker-mysql server to show he can be reach over the network
```
#add software
apt update & apt install mysql-client iputils-ping -y
#show env variables
env
#print the hello variable to the commandline
echo $HELLO
#ping docker-mysql
ping docker-mysql
#leave
exit
```

Summerize the issue above, and go back to the original question. Then explain the real answer (just demonstrated), real answer:

*Containers are running in the docker context, that means we can reach open ports on docker with localhost form the outside. But for container running in docker the have to run within the same (virtual docker) network to see each other. Also localhost is than no longer applicable because that has become the localhost context of the container (as a server) itself. So for applications within container to be able to talk they must refer to the other application on the open port of that application on the server, either by Container-IP or Container-Name*
**_!Show them the picture in the presentation_**

answers:
- [ ] we have to open up a port on the container and map that to the default set port of the application (show the application.settings file to make them understand). Explain how ports work: a.k.a. their are application ports and server ports, serverports first then colon (:) app-port.
- [ ] when values differ we have to set environment variables, to change the default settings in the application.settings file (dynamic configuration management)

- [ ] startup the UL app container with open ports and bind it to the network
```
docker run -p 8080:8080 --name docker-ul --net training enabledocker/ul
```
- [ ] still does not work anyone has a clue?
* answer: our app is not configured to work talk on the training network to docker-container: we have to change the localhost reference!
* Take a moment to show them the application-properties file to show them the environment variable to set.
- [ ] run the container on the commandline to show it will work now:
```
docker run -p 8080:8080 --name docker-ul --net training -e MYSQL_HOST=docker-mysql enabledocker/ul
```
- [ ] then run the container for real as background container (daemon):
```
docker rm docker-ul
docker run -d -p 8080:8080 --name docker-ul --net training -e MYSQL_HOST=docker-mysql enabledocker/ul
```
- [ ] Reafirm the Rest service interface working using the browser
- [ ] Summerize the current situation: we have two working containers in docker, our rest service with the mysql database server behind it

# Intermezzo
We just used an Ubuntu container for testing, which when we left was gone from docker. Now in preparation of what is to come we will spin up an permanent Ubuntu container, so we have a 'semi' permanent server in our network that we can use for testing.
- [ ] start the ubuntu container under the name Ubuntu and make it run permanent:

```
 docker run -d --name ubuntu --net training ubuntu sleep infinity
```
- [ ] jump into the container and install curl, mysql-client iputils-ping:
```
docker exec -it ubuntu bash
apt-get update && apt-get install curl host mysql-client iputils-ping -y
```
**Now we have an Ubuntu container/server standing by in our network, ready, for testing**

# Completing our demo setup by adding the frontend-application

- [ ] Also now we need a Dockerfile in our project:
```
FROM node:lts-alpine3.16
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
EXPOSE 3000
CMD ["npm", "start"]
```

- [ ] Build the UL frontoffice container for the first time with alpine tag and version number: 
```
docker build -t enabledocker/ul-frontend:0.1-node-alpine3.16 .
docker build -t enabledocker/ul-frontend .
```

- [ ] push current images to docker hub: 
```
docker push enabledocker/ul-frontend:latest
docker push enabledocker/ul-frontend:0.1-node-alpine3.16
```

- [ ] run the container on the commandline to show it will work now:
```
docker run -d -p 3000:3000 --name docker-ul-frontend --net training -e REACT_APP_BACKOFFICE_SERVICE=docker-ul enabledocker/ul-frontend
```
All runs without error, but the client is empty.
- What is going, on?
- How to find out?
- Tell me what are you thinking!!!!

# Making CORS work
- [ ] Start by explaining the new situation from the pictures in the presentation

- [ ] stop the UL-Backoffice container and reinitiate it with the url that is being used by the UL-Frontoffice:
```
docker rm docker-ul -f
docker run -d -p 8080:8080 --name docker-ul \
    --net training -e MYSQL_HOST=docker-mysql \
    -e CORSCLIENT_HOSTNAME=docker-ul-frontend \
    enabledocker/ul
```

```
docker rm docker-ul -f
docker run -d -p 8080:8080 --name docker-ul \
    --net training -e MYSQL_HOST=docker-mysql \
    -e CORSCLIENT_HOSTNAME=* \
    enabledocker/ul
```
# Reinstalling the demo with docker-compose
- [ ] Add docker compose .env file to your spring project root:
```
MYSQL_CONTAINER_IMAGE=mysql
MYSQL_HOSTNAME=docker-mysql
MYSQLDB_USER=root
MYSQLDB_ROOT_PASSWORD=root
MYSQLDB_DATABASE=users
MYSQLDB_LOCAL_PORT=3306
MYSQLDB_CONTAINER_PORT=3306
UL_CONTAINER_IMAGE=enabledocker/ul
UL_HOSTNAME=docker-ul
UL_LOCAL_PORT=8081
UL_CONTAINER_PORT=8081
UL_HTTP_PROTOCOL=http
UL_QUERY_PATH=api/users
UL_CORS_ENABLED=true
UL_CORS_ORIGINS=http://
UL_LOGLEVEL=DEBUG
UL_LOGSECURITYLEVEL=DEBUG
UL_FE_HOSTNAME=docker-ul-frontend
UL_FE_CONTAINER_IMAGE=enabledocker/ul-frontend
UL_FE_LOCAL_PORT=3001
UL_FE_CONTAINER_PORT=3001
UL_FE_PROTOCOL=http
```
- [ ] Then add a docker compose yaml to the (spring) project root:
```
version: "3.8"
networks:
    training:
        external: true

services:
  mysqldb:
    image: $MYSQL_CONTAINER_IMAGE
    container_name: $MYSQL_HOSTNAME
    restart: unless-stopped
    env_file: ./.env
    environment:
      - MYSQL_ROOT_PASSWORD=$MYSQLDB_ROOT_PASSWORD
      - MYSQL_DATABASE=$MYSQLDB_DATABASE
    ports:
      - $MYSQLDB_LOCAL_PORT:$MYSQLDB_CONTAINER_PORT
    networks:
      - training

  restapp:
    depends_on:
      - mysqldb
    image: $UL_CONTAINER_IMAGE
    container_name: $UL_HOSTNAME
    restart: on-failure
    env_file: ./.env
    ports:
      - $UL_LOCAL_PORT:$UL_CONTAINER_PORT
    networks:
      - training
    environment:
      - MYSQL_HOST=$MYSQL_HOSTNAME
      - MYSQL_USER=$MYSQLDB_USER
      - MYSQL_PASSWORD=$MYSQLDB_ROOT_PASSWORD
      - MYSQL_NAME=$MYSQLDB_DATABASE
      - MYSQL_PORT=$MYSQLDB_CONTAINER_PORT
      - SERVER_PORT=$UL_LOCAL_PORT
      - CORS_ENABLED=true
      - CORS_HOSTNAME=$UL_FE_HOSTNAME
      - CORS_PORT=$UL_FE_CONTAINER_PORT
      - CORS_PROTOCOL=$UL_FE_PROTOCOL
      - CORS_ORIGINS="$UL_FE_PROTOCOL://$UL_FE_HOSTNAME:$UL_FE_CONTAINER_PORT, $UL_FE_PROTOCOL://localhost:$UL_FE_CONTAINER_PORT"
#     - CORS_ORIGINS=""
#     - CORS_ORIGINS="*"
      - LOGLEVEL=$UL_LOGLEVEL
      - LOGSECURITYLEVEL=$UL_LOGSECURITYLEVEL

  frontendapp:
    depends_on:
      - restapp
    image: $UL_FE_CONTAINER_IMAGE
    container_name: $UL_FE_HOSTNAME
    restart: on-failure
    env_file: ./.env
    ports:
      - $UL_FE_LOCAL_PORT:$UL_FE_CONTAINER_PORT
    networks:
      - training
    environment:
      - REACT_APP_BACKOFFICE_PROTOCOL=$UL_HTTP_PROTOCOL
      - REACT_APP_BACKOFFICE_SERVICE=$UL_HOSTNAME
      - REACT_APP_BACKOFFICE_PORT=$UL_CONTAINER_PORT
      - REACT_APP_BACKOFFICE_API_ENDPOINT=$UL_QUERY_PATH
      - PORT=$UL_FE_LOCAL_PORT
```
- [ ] Walk the training to its content

- [ ] Start docker compose
```
docker-compose up
```
- [ ] Show them how to start docker compose as daemon:
```
docker-compose up -d
```