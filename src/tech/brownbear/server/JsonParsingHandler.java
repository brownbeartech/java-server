package tech.brownbear.server;

import com.google.common.io.ByteSource;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.util.AttachmentKey;
import io.undertow.util.Headers;

import java.io.InputStream;

public class JsonParsingHandler implements HttpHandler {
    public static final AttachmentKey<String> REQUEST_BODY_JSON = AttachmentKey.create(String.class);

    private volatile HttpHandler next = ResponseCodeHandler.HANDLE_404;

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String contentType = exchange.getRequestHeaders().getFirst(Headers.CONTENT_TYPE);
        if (contentType != null && contentType.startsWith("application/json")) {
            if (exchange.isInIoThread()) {
                exchange.dispatch(this);
                return;
            }
            exchange.startBlocking();
            ByteSource source = new ByteSource() {
                @Override
                public InputStream openStream() {
                    return exchange.getInputStream();
                }
            };
            exchange.putAttachment(REQUEST_BODY_JSON, new String(source.read(), "UTF-8"));
        }
        next.handleRequest(exchange);
    }

    public HttpHandler getNext() {
        return next;
    }

    public JsonParsingHandler setNext(final HttpHandler next) {
        Handlers.handlerNotNull(next);
        this.next = next;
        return this;
    }
}
