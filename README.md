# Spring Boot To-Do App

## Features

* Full REST API for managing To-Do list items
* Full JUnit test suite runnable by Maven to test TodoItemController and
  TodoItemService
* API documentation via Swagger (visit `http://localhost:8080/swagger-ui.html`
  when the app is launched locally)
* Scoped request authentication via JWT using Spring Security and Auth0
* Spring @Scheduled job for sending email

## Setup

### Dev Environment

This project is using Spring 6, Spring Boot 3, and Java JDK 17. Ensure you have
those installed and set up before attempting to run this.

To begin, clone down the Git repository:

```
git clone https://github.com/bclindner/todoapp
cd todoapp
```

The project's dependencies are managed through Maven - install them here, or
through your IDE if it has support:

```
./mvnw dependency:resolve
```

From here, you should be able to run the app with dev properties via this
command.

```
./mvnw -Dspring-boot.run.profiles="dev" spring-boot:run
```

This is an ease-of-use config - OAuth authentication and the email job are
disabled. You can visit the docs at site at
http://localhost:8080/swagger-ui.html, using the username "user" and the
password in the app logs. (API requests can be made using similar HTTP Basic
auth settings.)

To test with authentication, you will want an Application and API set up in
Auth0 - the settings provided are for the developer's use, and for
demonstration. Without a valid client ID and secret, the API will return 4xx
errors on all requests.

Change these two settings in `src/main/resources/application.properties` (or
any profile you'd like to activate) once you've gotten an Auth0 environment set
up:

```
auth0.audience=audience-id-for-your-app
spring.security.oauth2.resourceserver.jwt.issuer-uri=jwt-issuer-uri
```

To test with the email job, you'll need an SMTP server. As an example,
SMTPBucket is set up in the default profile - use it as a reference for the
settings to change.

To test both the email job and authentication, simply run the app without the
dev profile:

```
./mvnw spring-boot:run
```

### Building & Running in Production Environment

To run this in production, I would recommend creating a separate
`application.properties` file to use when launching the app from your prod
server, as the properties would include some sensitive info like the SMTP login
credentials.

You may also want to take some time to add dependencies to pom.xml to set up
JDBC connections for a database, as this persists all records to an internal
in-memory H2 database by default. This app is built on Spring Data JPA, so most
major database engines can be configured. Configuration for a specific
prod-ready database of your choosing is left as an exercise to the reader.

With all that said, let's build the project. Run:

```
./mvnw clean package spring-boot:repackage
```

This will build a runnable fat JAR in `target/todo-0.0.1-SNAPSHOT.jar`.

You can run this JAR file with the aforementioned external properties via:

```
java -jar todo-0.0.1-SNAPSHOT.jar
```

This should start the app up with the default settings, without need for any
external dependencies or a Tomcat install.

You can include your application.properties file in the same directory when
launching to apply your settings.

## Potential Enhancements

* Allow users to own to-do list items and restrict viewing from others
* Group TodoItems into a new TodoList model for the above
* Set up OAuth scopes for reading/writing TodoItems
* Full-stack test with web frontend
* Offload Scheduled job to a clustered CronJob if running Kubernetes
  architecture for reliability/cluster-safety
* Hardening of existing SecurityConfig filter chain and settings
* Improvement of Swagger UI security setting documentation