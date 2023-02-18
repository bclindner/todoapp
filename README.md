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

Additionally, you will want an Application and API set up in Auth0 - the
settings provided are for the developer's use, and for demonstration. Without a
valid client ID and secret, the API will return 401 or 403 on all requests. 

Set these two settings in `src/main/resources/application-dev.properties` once
you've done so:

```
auth0.audience=audience-id-for-your-app
spring.security.oauth2.resourceserver.jwt.issuer-uri=jwt-issuer-uri
```

From here, you should be able to run the app in dev directly via:

```
./mvnw spring-boot:run
```

`application-dev.properties`, in the same directory as the default properties.

```
./mvnw -Dspring-boot.run.profiles="dev,default" spring-boot:run
```


### Building & Running in Production Environment

To run this in production, make sure to either tweak the settings in
`application-production.properties`, or creating a separate file to use when
launching the app. For simplicity's sake, we are operating on the assumption
that you have tweaked the settings to be baked into the JAR.

You may also want to take this time to add dependencies and set up JDBC
connections for a database, as this persists all records to an internal
in-memory H2 database by default.

To build the project, run:

```
./mvnw clean package spring-boot:repackage
```

This will build a runnable fat JAR in `target/todo-0.0.1-SNAPSHOT.jar`.

From here, just run:

```
java -jar target/todo-0.0.1-SNAPSHOT.jar
```

This will start the app up with the settings you specify, without need for any
external dependencies.

## Potential Enhancements

* Allow users to own to-do list items and restrict viewing from others
* Group TodoItems into a new TodoList model for the above
* Set up OAuth scopes for reading/writing TodoItems
* Full-stack test with web frontend
* Offload Scheduled job to a clustered CronJob if running Kubernetes
  architecture for reliability/cluster-safety
