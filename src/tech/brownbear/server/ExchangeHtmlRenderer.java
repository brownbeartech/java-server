package tech.brownbear.server;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import tech.brownbear.soy.SoyTemplateRenderer;

import java.util.Map;

public class ExchangeHtmlRenderer implements HtmlRenderer {
    private final HttpServerExchange exchange;
    private final SoyTemplateRenderer renderer;

    public ExchangeHtmlRenderer(
        HttpServerExchange exchange,
        SoyTemplateRenderer renderer) {
        this.exchange = exchange;
        this.renderer = renderer;
    }

    @Override
    public void render(String templateName) {
        String content = renderer.render(templateName);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        exchange.getResponseSender().send(content);
    }

    @Override
    public void render(String templateName, Map<String, Object> args) {
        String content = renderer.render(templateName, args);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        exchange.getResponseSender().send(content);
    }

    @Override
    public void render(Map<String, Object> ij, String templateName, Map<String, Object> args) {
        String content = renderer.render(ij, templateName, args);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        exchange.getResponseSender().send(content);
    }
}
