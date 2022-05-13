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