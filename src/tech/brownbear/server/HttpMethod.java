package tech.brownbear.server;

import io.undertow.util.HttpString;
import io.undertow.util.Methods;

public enum HttpMethod {
    GET(Methods.GET),
    POST(Methods.POST);

    private final HttpString method;

    HttpMethod(HttpString method) {
        this.method = method;
    }

    public HttpString getMethod() {
        return method;
    }
}
