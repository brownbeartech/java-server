# Utilities for Undertow HTTP Server

This library provides some help with boilerplate server setup using the Undtertow library. Included are
binding caught and uncaught exceptions, soy templates and route groups. The `Application` and `Session` 
are user provided parameterizations allowing for application setup and session passing to endpoints.
This library is dependant upon [java-resources](https://github.com/brownbeartech/java-resources) and 
[java-soy](https://github.com/brownbeartech/java-soy).

## Basic usage

```java
public class Main {
    public static void main(String[] args) {
        Server<Application, Session> server = new ServerBuilder<Application, Session>(Application::new)
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

```java
public class Application implements AutoCloseable {
    private final SoyTemplateRenderer renderer;

    public Application(SoyTemplateRenderer renderer) {
        this.renderer = renderer;
        ...
    }

    ...

    public SoyTemplateRenderer getRenderer() {
        return renderer;
    }

    @Override
    public void close() {
        Closeables.closeQuietly(db);
    }
}
```

```java
public class Errors {
    private static Logger logger = LogManager.getRootLogger();

    public static void uncaught(Application app, Exchange<UserSession> e) {
        // NOTE Toggle for no stack traces in prod
        logger.error("Uncaught error", e.getException());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.getException().printStackTrace(pw);
        e.setStatusError();
        e.renderText(sw.toString());
    }

    public static void auth(Application app, Exchange<UserSession> e) {
        e.redirect("/");
    }

    public static void notFound(Application app, Exchange<UserSession> e) {
        e.renderText("404");
    }
}
```

## Route Groups

Route groups allow for authentication by creating handler chains that start with an 
authentication check provided to `begin(...)`. Failure to authenticate will trigger a
`AuthenticationException` that can be handled by using the `addExceptionHandler` method.

```java
RouteGroup<Application, Session> internalRoutes = RouteGroup.<Application, Session>builder()
    .begin(Authenticate::doAuth)
    .get(AccountRoutes.ACCOUNT_INTERNAL_ROOT, Account::edit)
    .get(AccountRoutes.LOGOUT, Account::logout)
    ...
    .build();

Server<Application, Session> server = new ServerBuilder<Application, Session>(Application::new)
    .setPort(8484)
    ...
    .route(internalRoutes)
    .addExceptionHandler(AuthenticationException.class, Errors::auth)
    ...
    .build();
server.start();
```

```java
public class Authenticate extends Controller {
    public static void doAuth(Application app, Exchange<Session> e) {
        Optional<Session> session = ...;
        if (!session.isPresent()) {
            throw new AuthenticationException();
        }
        e.setSession(session.get());
    }
}
```