### Spring CQRS and Event Sourcing Microservice Example
#### This project is a microservice example using Event Sourcing and CQRS architecture with Java Spring.

#### Tools and Technologies Used
* [Spring](https://spring.io/) - Java Spring
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - data access layer
* [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb) - Spring Data MongoDB
* [Mongo Express](https://github.com/mongo-express/mongo-express) - Web-based MongoDB admin interface
* [Kafka](https://spring.io/projects/spring-kafka) - Spring for Apache Kafka
* [Kafdrop](https://github.com/obsidiandynamics/kafdrop) - Kafka Web UI
* [PostgreSQL](https://www.postgresql.org/) - PostgreSQL database
* [Docker](https://www.docker.com/) - Docker
* [Liquibase](https://www.liquibase.org/) - Database migrations
* [Swagger OpenAPI 3](https://springdoc.org/) - java library helps to automate the generation of API documentation

#### UI Interfaces
* Swagger UI: http://localhost:8006/swagger-ui/index.html
* Kafdrop: http://localhost:9000/topic/bank-account-event-store
* Mongo Express: http://localhost:8081/db/microservices/bankAccounts

#### Local Run
```
docker-compose up
./mvnw spring-boot:run
```

### Troubleshooting
If you encounter a Liquibase error, try running CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; manually.

Thank you for using this microservice example!