package tech.brownbear.server;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ExchangeFileRenderer implements FileRenderer {
    private final HttpServerExchange exchange;

    public ExchangeFileRenderer(HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void render(File file, FileFormat format) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, format.getMimeType());
        exchange.getResponseSender().send(fileToByteBuffer(file));
    }

    private final ByteBuffer fileToByteBuffer(File file) {
        try {
            ByteSource byteSource = Files.asByteSource(file);
            return ByteBuffer.wrap(byteSource.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
