# Utilities for Undertow HTTP Server

This library provides some help with boilerplate server setup using the Undtertow library. Included are
binding caught and uncaught exceptions, soy template setup and route groups. 

## Basic usage

```java
public class Main {
    public static void main(String[] args) {
        Server<Application, UserSession> server = new ServerBuilder<Application, UserSession>(Application::new)
            .setPort(8585)
            .setRootClass(Main.class)
            .setSoyTemplatesPath("/path/to/templates")
            .get("", Home::main)
            .get("/blog", Blog::main)
            .addExceptionHandler(NotFoundException.class, Errors::notFound)
            .setFallbackExceptionHandler(Errors::uncaught)
            .build();
        server.start();
    }
}
```

## Route Groups

Route groups allow for authentication by creating handler chains that start with an 
authentication check provided to `begin(...)`. Failure to authenticate will trigger a
`AuthenticationException` that can be handled by using the `addExceptionHandler` method.

```java
RouteGroup<Application, UserSession> internalRoutes = RouteGroup.<Application, UserSession>builder()
    .begin(Authenticate::doAuth)
    .get(AccountRoutes.ACCOUNT_INTERNAL_ROOT, Account::edit)
    .get(AccountRoutes.LOGOUT, Account::logout)
    ...
    .build();

Server<Application, UserSession> server = new ServerBuilder<Application, UserSession>(Application::new)
    .setPort(8484)
    ...
    .route(internalRoutes)
    .addExceptionHandler(AuthenticationException.class, Errors::auth)
    ...
    .build();
server.start();
```