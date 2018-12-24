package tech.brownbear.server;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class RouteGroup<Application, Session> {
    private final HandlerChain<Application, Session> before;
    private final List<Route> routes;

    public static class Builder<A, S> {
        private HandlerChain<A, S> before;
        private List<Route> routes = new ArrayList<>();

        public Builder<A, S> begin(Handler<A, S> before) {
            this.before = HandlerChain.begin(before);
            return this;
        }

        public Builder<A, S> next(Handler<A, S> before) {
            this.before.next(before);
            return this;
        }

        public Builder<A, S> get(String path, Handler<A, S> handler) {
            return route(HttpMethod.GET, path, handler);
        }

        public Builder<A, S> post(String path, Handler<A, S> handler) {
            return route(HttpMethod.POST, path, handler);
        }

        private Builder<A, S> route(HttpMethod method, String path, Handler handler) {
            Preconditions.checkNotNull(path);
            Preconditions.checkNotNull(handler);
            routes.add(new Route(method, path, handler));
            return this;
        }

        public Builder<A, S> route(Route route) {
            Preconditions.checkNotNull(route);
            routes.add(route);
            return this;
        }

        public RouteGroup<A, S> build() {
            Preconditions.checkNotNull(before);
            Preconditions.checkArgument(!routes.isEmpty());
            return new RouteGroup(before, routes);
        }
    }

    public RouteGroup(HandlerChain<Application, Session> before, List<Route> routes) {
        this.before = before;
        this.routes = routes;
    }

    public static <A, S> Builder<A, S> builder() {
        return new Builder<>();
    }

    public List<Route> complete() {
        List<Route> bound = new ArrayList<>();
        for (Route route : routes) {
            bound.add(new Route(route.getMethod(), route.getPath(), before.compose(route.getHandler())));
        }
        return bound;
    }
}
