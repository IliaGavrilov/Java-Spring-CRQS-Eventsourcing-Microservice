### Spring CQRS and Event Sourcing microservice example

#### Full list what has been used:
* [Spring](https://spring.io/) - Java Spring
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - data access layer
* [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb) - Spring Data MongoDB
* [Mongo Express](https://github.com/mongo-express/mongo-express) - Web-based MongoDB admin interface
* [Kafka](https://spring.io/projects/spring-kafka) - Spring for Apache Kafka
* [Kafdrop](https://github.com/obsidiandynamics/kafdrop) - Kafka Web UI
* [PostgreSQL](https://www.postgresql.org/) - PostgreSQL database.
* [Docker](https://www.docker.com/) - Docker
* [Liquibase](https://www.liquibase.org/) - Database migrations
* [Swagger OpenAPI 3](https://springdoc.org/) - java library helps to automate the generation of API documentation

### Swagger UI:
http://localhost:8006/swagger-ui/index.html



For local development:
```
make local // runs docker-compose.yaml with all required containers
run spring application
```