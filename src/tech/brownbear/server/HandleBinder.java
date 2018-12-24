package tech.brownbear.server;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import tech.brownbear.soy.SoyTemplateRenderer;

public class HandleBinder<Application, Session> {
    private final Application application;
    private final SoyTemplateRenderer renderer;

    public HandleBinder(Application application, SoyTemplateRenderer renderer) {
        this.application = application;
        this.renderer = renderer;
    }

    public HttpHandler bind(Handler handler) {
        return (HttpServerExchange e) -> handler.handle(application, new Exchange<Session>(e, renderer));
    }
}