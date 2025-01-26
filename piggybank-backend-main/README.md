# Example app for testing
Java application built with [Spring Boot](https://spring.io/projects/spring-boot).

## Run/debug
To run the application you can do the following:
- Run the PiggyBankApplication.java, and it will start the application. 
- *Or* run `mvn spring-boot:run`.

## Database
### Connect to in-memory h2 database

If you start the application, it will also start an instance of the in-memory h2 database. To access this database use the following information:

- **Url:** http://localhost:8080/h2-console
- **Driver Class:** org.h2.Driver
- **JDBC URL:** jdbc:h2:mem:testdb
- **User Name:** sa
- **Password:** password

## Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Spring JPA](https://spring.io/projects/spring-data-jpa)

## `!` Disclaimer
This app should never be used in production. This app is intended as a test and it contains some bugs to be fixed. The software is used as part of a workshop. The security of the software is also not safe for production purposes.
