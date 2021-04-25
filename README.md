# Message Board Server

## Project technologies
* Apache Maven 3.6.0
* Java 1.8.0_202, vendor: AdoptOpenJdk
* Docker engine version 20.10.5
* IntelliJ Idea 2021.1
* Spring boot 2.4.5
* Liquibase 3.8.0
* H2 database 1.4.200

---

## Clone project from GitHub
```git clone https://github.com/nemethsamusandor/storytel.git```

## Maven Build
Run ```mvn clean install``` to build the complete project with tests

or

```mvn clean install -DskipTests``` without tests

```./mvnw``` command also can be used instead of mvn from the project root

## Docker build and run
* Run Docker build ```docker build -t se.storytel.messageboard:0.0.1 .``` to create docker image
* Run Docker image ```docker run --publish 8239:8080 --detach --name storytel.messageboard se.storytel.messageboard:0.0.1```

## Database connection
Database is accessible on localhost:8329/h2-console
````
Driver class:   org.h2.Driver
JDBC URL:       jdbc:h2:mem:storytel
User Name:      storytel
Password:       <leave it empty>
````

## Initialization
RestAPI endpoint is accessible by default on port 8329.
Clients need to login in order to access services methods.
Initiated clients (what can be extended in H2 in memory database):

Username|Password
--------|---------
Emily   | password
John    | password
Sandor  | password
Lili    | password

Service uses Basic authentication

**Insert new client to database** 
````
insert into client (username, password) values ('New client', 'bcrypt encoded password');

insert into authorities (username, authority) values ('New client', 'ROLE_USER');
````

Encrypted password can be generetad online e.g. on this site: ``https://bcrypt-generator.com/``.

## Available endpoints

* ```POST http://localhost:8329/api/login``` - Authenticate client
  ``Request body:
  {
  "username" : "string",
  "password": "bcrypt encoded password"
  }
  ``
* ```GET http://localhost:8329/api/messages``` - Get all messages in the service 
* ```GET http://localhost:8329/api/messages/client``` - Get all messages of the authenticated client
* ```POST http://localhost:8329/api/messages``` - Add a client message
  
  ``Request body: 
    {
    "text": "string"
    }
  ``
* ```PUT http://localhost:8329/api/messages``` - Update a client message

  ``Request body:
  {
  "id" : int,
  "text": "string"
  }
  ``
* ```DELETE http://localhost:8329/api/messages/{id}``` - Delete a client message

## Service test
Service can be tested with e.g. Postman, Terminal (curl) or other tools

Configure request header map with
```
Content-Type: application/json
Authorization: Basic <Base64 encoded credentials of username:password>
```
