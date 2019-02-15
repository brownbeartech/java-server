package tech.brownbear.server;

import com.google.common.collect.ImmutableSet;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.form.*;
import io.undertow.server.handlers.resource.ResourceHandler;
import tech.brownbear.soy.SoyTemplateRenderer;

public class Server<Application extends AutoCloseable, Session> {
    private final ShutdownHook shutdownHook = new ShutdownHook();
    private final Application application;
    private final Class<?> rootClass;
    private final Integer port;
    private final List<Route> routes;
    private final Map<Class, Handler<Application, Session>> exceptionHandlers;
    private final Handler<Application, Session> fallbackExceptionHandler;
    private final SoyTemplateRenderer renderer;

    public Server(
        ApplicationProvider<Application> provider,
        Class<?> rootClass,
        Integer port,
        List<Route> routes,
        Map<Class, Handler<Application, Session>> exceptionHandlers,
        Handler<Application, Session> fallbackExceptionHandler,
        SoyTemplateRenderer renderer)
    {
        this.application = shutdownHook.register(() -> provider.provide(renderer));
        this.rootClass = rootClass;
        this.port = port;
        this.routes = routes;
        this.exceptionHandlers = exceptionHandlers;
        this.fallbackExceptionHandler = fallbackExceptionHandler;
        this.renderer = renderer;
    }

    public void start() {
        Undertow undertow = Undertow.builder()
            .addHttpListener(port, "0.0.0.0")
            .setHandler(buildHandlerList())
            .build();
        undertow.start();
    }

    private HttpHandler buildHandlerList() {
        return errors(
            buildFormParsing().setNext(
            new JsonParsingHandler().setNext(
            buildPathHandler())));
    }

    private ExceptionHandler errors(HttpHandler handler) {
        HandleBinder<Application, Session> binder = new HandleBinder(application, renderer);
        ExceptionHandler exceptionHandler = Handlers.exceptionHandler(handler);
        for (Class c : exceptionHandlers.keySet()) {
            exceptionHandler.addExceptionHandler(c, binder.bind(exceptionHandlers.get(c)));
        }
        if (fallbackExceptionHandler != null) {
            exceptionHandler.addExceptionHandler(Throwable.class, (h) -> {
                Throwable t = h.getAttachment(ExceptionHandler.THROWABLE);
                Optional<Class> matched = exceptionHandlers.keySet().stream()
                    .filter(c -> c.isInstance(t))
                    .findAny();
                // Only handle request with fallback handler if not already handled
                if (!matched.isPresent()) {
                    binder.bind(fallbackExceptionHandler).handleRequest(h);
                }
            });
        }
        return exceptionHandler;
    }

    private EagerFormParsingHandler buildFormParsing() {
        return new EagerFormParsingHandler(
            FormParserFactory.builder()
                .addParsers(new MultiPartParserDefinition())
                .build());
    }

    private PathHandler buildPathHandler() {
        PathHandler pathHandler = new PathHandler();
        pathHandler.addPrefixPath("/public", buildResourceHandler());
        pathHandler.addPrefixPath("/", buildRoutingHandler());
        return pathHandler;
    }

    private RoutingHandler buildRoutingHandler() {
        RoutingHandler routingHandler = new RoutingHandler();
        HandleBinder<Application, Session> binder = new HandleBinder(application, renderer);
        for (Route route : routes) {
            HttpHandler httpHandler = binder.bind(route.getHandler());
            routingHandler.add(route.getHttpString(), route.getPath(), httpHandler);
        }
        routingHandler.setFallbackHandler(h -> {
            throw new NotFoundException();
        });
        return routingHandler;
    }

    private ResourceHandler buildResourceHandler() {
        ClasspathResourceManager resourceManager = new ClasspathResourceManager(
            rootClass,
            ImmutableSet.of("/assets"));
        return new ResourceHandler(resourceManager);
    }
}
