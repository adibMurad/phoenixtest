# Technology outline

This application provides a microservice API to consume the Stack Exchange API.
It was built on Java 12 and Spring Boot.

On startup the application saves Stack Overflow questions data in an in-memory
H2 database, since this assessment requires a volatile persistence. H2 is
ubiquitous, lightweight and easy to handle through JPA. H2 administration
console is enabled in the application (see *Building and running*)

Stack Exchange services are consumed using Feign, a declarative REST client
that is lightweight and easy to use. A Feign Client is an annotated interface.
Feign supports JAX-RS annotations and pluggable encoders and decoders.

Stack Overflow users data is cached using Redis, another light, market standard
and easy to use solution.

Beside the reasons stated above, these solutions were adopted because of
their integration with Spring Boot.

Project Lombok annotations were used to avoid massive amounts of boilerplate
getters, setters, constructors, builders and more.

JUnit Jupiter and Mockito asserted 100% test coverage for controllers,
services and utils.

A Swagger page is available (see *Building and running*) as API documentation
and testing. Also a Postman collection (*postman_collection.json*) can be
found in the project root directory.

# Dependencies

This application requires:

- JDK 12 or later
- Redis, that can be downloaded and installed
  from https://github.com/microsoftarchive/redis/releases/download/win-3.0.504/Redis-x64-3.0.504.msi

# Building and running

Open a command prompt and start Redis by running:

- redis-server

Test the Redis server by running:

- redis-cli ping

The server should answer `PONG`

Change directory to the application folder and run it:

- .\mvnw.cmd clean install spring-boot:run

Use `curl` to check if the application is running:

- curl http://localhost:8080/phoenix/question

The H2 database can be managed at:

http://localhost:8080/phoenix/h2

A swagger API documentation page is available at:

http://localhost:8080/phoenix/swagger-ui/

The Postman collection *postman_collection.json* is available in the
project root directory. Import it into Postman, run the collection and
check the test results.