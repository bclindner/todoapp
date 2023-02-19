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

This project is using Java JDK 17. Ensure you have those installed and set up
in your environment before attempting setup.

To begin, clone down the Git repository:

```
git clone https://github.com/bclindner/todoapp
cd todoapp
```

The project is managed through Maven. You can run this command to download
Maven and the project's dependencies (this will make first compilation take
less time later):

```
./mvnw dependency:resolve
```

You will also need an Auth0 tenant to run this as-intended. You will want an
API set up in Auth0 to configure this app - the settings provided are for the
developer's use, and for demonstration. Additionally, without a valid client ID
and secret, the API will return 401/403 errors on all requests - see
[API Client Authentication](#API_Client_Authentication) for details.

Change these two settings in `src/main/resources/application.properties` (or
any profile you'd like to activate) once you've gotten an Auth0 environment set
up:

```
auth0.audience=audience-id-for-your-app
spring.security.oauth2.resourceserver.jwt.issuer-uri=jwt-issuer-uri
```

Important to note that if you're pulling the /authorize endpoint directly out
of Auth0 you'll have to include some extra settings. The example is in
`application.properties`. Note the `?audience=<your-audience-uri>` appended to
the authorizeUrl - you need to have that for it to direct properly from OpenAPI
consumers.

To test with the email job, you'll need an SMTP server. As an example,
SMTPBucket is set up in the default profile - use it as a reference for the
settings to change.

To test both the email job and authentication, simply run the app without the
dev profile:

```
./mvnw spring-boot:run
```

### API Client Authentication

The easiest way to test the API yourself would be to go to
`http://localhost:8080/swagger-ui.html` - that will open up the Swagger
documentation. If you prefer a client like Postman or Insomnia, you can
download the OpenAPI spec from `http://localhost:8080/v3/api-docs.yaml` - the
API spec includes auth and the instructions will be mostly the same from there.

You will need to do some more Auth0 dashboard legwork for this part. Create a
new "single page" Application in Auth0, and add
`http://localhost:8080/swagger-ui/oauth2-redirect.html` as an allowed callback
URL. Paste in the client ID and secret (and the redirect URL on a REST client)
to start the authorization flow. If all goes well, you should be logged in and
able to test the requests from your client - congratulations!

This is just a test of the authorization code flow - this is set up as a
generic resource server, so while the other pertinent OAuth flows are untested,
they should work fine.

### Building & Running in Production Environment

To run this in production, I would recommend creating a separate
`application.properties` file to use when launching the app from your prod
server, as the properties would include some sensitive info like the SMTP login
credentials and any production tenant info (it's probably fine, but you can
never be too sure, right?).

You may also want to take some time to add dependencies to pom.xml to set up
JDBC connections for a database, as this persists all records to an internal
in-memory H2 database by default. This app is built on Spring Data JPA, so most
major database engines can be configured. Configuration for a specific
prod-ready database of your choosing is left as an exercise to the reader.

With all that said, let's build the project. Run:

```
./mvnw clean package spring-boot:repackage
```

This will build a runnable "fat JAR" in `target/todo-0.0.1-SNAPSHOT.jar`,
containing all dependencies.

You can run this JAR file via the `java` runtime:

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
* `Dockerfile` config for containerized deployment
* `docker-compose` config or Helm chart for containerized deployment w/
  database