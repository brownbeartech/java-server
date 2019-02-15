package tech.brownbear.server;

import io.undertow.attribute.RemoteIPAttribute;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.brownbear.soy.SoyTemplateRenderer;

import java.util.*;
import java.util.stream.Stream;

public class Exchange<Session> implements ResponseRenderer {
    protected static final Logger logger = LoggerFactory.getLogger(Exchange.class);
    private static final String UA_HEADER = "User-Agent";

    private final HttpServerExchange exchange;
    private final SoyTemplateRenderer renderer;
    private final ExchangeHtmlRenderer htmlRenderer;
    private final ExchangeFileRenderer fileRenderer;

    private Optional<Session> session = Optional.empty();
    private final Map<String, Object> templateInjectedData = new HashMap<>();

    public Exchange(
        HttpServerExchange exchange,
        SoyTemplateRenderer renderer) {
        this.exchange = exchange;
        this.renderer = renderer;
        this.htmlRenderer = new ExchangeHtmlRenderer(
            exchange,
            renderer);
        this.fileRenderer = new ExchangeFileRenderer(exchange);
    }

    @Override
    public void renderText(String text) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(text);
    }

    public void setStatusError() {
        exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void renderHtml(String templateName, Map<String, Object> args) {
        renderHtml(templateInjectedData, templateName, args);
    }

    @Override
    public void renderHtml(String templateName) {
        renderHtml(templateInjectedData, templateName);
    }

    @Override
    public void renderHtml(String templateName, String name, Object obj) {
        renderHtml(templateInjectedData, templateName, name, obj);
    }

    @Override
    public HtmlRenderer htmlRenderer() {
        return htmlRenderer;
    }

    @Override
    public FileRenderer fileRenderer() {
        return fileRenderer;
    }

    public String getRequestBodyJson() {
        return exchange.getAttachment(JsonParsingHandler.REQUEST_BODY_JSON);
    }

    public String getRequiredParameter(String param) {
        Optional<String> p = getOptionalParameter(param);
        if (!p.isPresent()) {
            notFound();
        }
        return p.get();
    }

    public Optional<String> getOptionalParameter(String param) {
        return getParam(param).findFirst();
    }

    private Stream<String> getParam(String param) {
        Stream<String> path = getOptionalParameter(param, exchange.getPathParameters());
        Stream<String> query = getOptionalParameter(param, exchange.getQueryParameters());
        Stream<String> form = getFormParameter(param);
        return Stream.concat(Stream.concat(path, query), form);
    }

    private Stream<String> getOptionalParameter(String param, Map<String, Deque<String>> parameters) {
        for (Map.Entry<String, Deque<String>> e : parameters.entrySet()) {
            if (e.getKey().equals(param)) {
                return e.getValue().stream();
            }
        }
        return Stream.empty();
    }

    private Stream<String> getFormParameter(String param) {
        FormData data = exchange.getAttachment(FormDataParser.FORM_DATA);
        if (data != null && data.contains(param)) {
            return data.get(param).stream().map(FormData.FormValue::getValue);
        }
        return Stream.empty();
    }

    public void notFound() {
        throw new NotFoundException();
    }

    public void redirect(String path) {
        exchange.setStatusCode(StatusCodes.FOUND);
        exchange.getResponseHeaders().put(Headers.LOCATION, path);
    }

    public void setCookie(Cookie cookie) {
        exchange.setResponseCookie(cookie);
    }

    public Optional<String> getCookie(String name) {
        if (exchange.getRequestCookies().containsKey(name)) {
            return Optional.of(exchange.getRequestCookies().get(name).getValue());
        }
        return Optional.empty();
    }

    public String getUserAgent() {
        if (exchange.getRequestHeaders().contains(UA_HEADER)) {
            return exchange.getRequestHeaders().get(UA_HEADER, 0);
        }
        return "UNK";
    }

    public String getIpAddress() {
        String ip = RemoteIPAttribute.INSTANCE.readAttribute(exchange);
        return ip != null ? ip : "UNK";
    }

    public Optional<Session> getSession() {
        return session;
    }

    public Session getRequiredSession() {
        if (!session.isPresent()) {
            throw new AuthenticationException();
        }
        return session.get();
    }

    public void setSession(Session session) {
        this.session = Optional.of(session);
    }

    public boolean hasSession() {
        return session.isPresent();
    }

    public void putTemplateInjectedData(Map<String, Object> templateInjectedData) {
        this.templateInjectedData.putAll(templateInjectedData);
    }

    public Throwable getException() {
        return exchange.getAttachment(ExceptionHandler.THROWABLE);
    }
}
