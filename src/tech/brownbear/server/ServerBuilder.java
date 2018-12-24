package tech.brownbear.server;

import tech.brownbear.resources.FallbackResourceFetcher;
import tech.brownbear.soy.SoyTemplateRenderer;
import tech.brownbear.soy.SoyTemplates;

import java.util.*;

import static com.google.common.base.Preconditions.*;

public class  ServerBuilder<Application extends AutoCloseable, Session> {
    private final ApplicationProvider<Application> provider;
    private Class<?> rootClass;
    private Integer port;
    private final List<Route> routes = new ArrayList<>();
    private String soyTemplatesPath;
    private final Map<Class, Handler<Application, Session>> exceptionHandlers = new HashMap<>();
    private Handler<Application, Session> fallbackExceptionHandler;

    public ServerBuilder(ApplicationProvider<Application> provider) {
        this.provider = provider;
    }

    public ServerBuilder<Application, Session> setRootClass(Class<?> rootClass) {
        this.rootClass = checkNotNull(rootClass);
        return this;
    }

    public ServerBuilder<Application, Session> setPort(Integer port) {
        this.port = checkNotNull(port);
        return this;
    }

    public ServerBuilder<Application, Session> setSoyTemplatesPath(String soyTemplatesPath) {
        this.soyTemplatesPath = checkNotNull(soyTemplatesPath);
        return this;
    }

    public ServerBuilder<Application, Session> get(String path, Handler<Application, Session> handler) {
        routes.add(new Route(HttpMethod.GET, path, handler));
        return this;
    }

    public ServerBuilder<Application, Session> post(String path, Handler<Application, Session> handler) {
        routes.add(new Route(HttpMethod.POST, path, handler));
        return this;
    }

    public ServerBuilder<Application, Session> route(RouteGroup<Application, Session> group) {
        routes.addAll(group.complete());
        return this;
    }

    public ServerBuilder<Application, Session> addExceptionHandler(Class c, Handler<Application, Session> handler) {
        exceptionHandlers.put(c, handler);
        return this;
    }

    public ServerBuilder<Application, Session> setFallbackExceptionHandler(Handler<Application, Session> handler) {
        this.fallbackExceptionHandler = handler;
        return this;
    }

    private void checkState() {
        checkNotNull(port);
        checkNotNull(rootClass);
        checkArgument(!routes.isEmpty());
    }

    public Server<Application, Session> build() {
        checkState();
        return new Server(
            provider,
            rootClass,
            port,
            routes,
            exceptionHandlers,
            fallbackExceptionHandler,
            buildRenderer());
    }

    private SoyTemplateRenderer buildRenderer() {
        if (soyTemplatesPath != null) {
            FallbackResourceFetcher fetcher = new FallbackResourceFetcher(
                rootClass,
                Collections.singleton(soyTemplatesPath));
            return new SoyTemplates(() -> fetcher.findAll(p -> p.getFileName().toString().endsWith(".soy")));
        }
        return null;
    }
}
