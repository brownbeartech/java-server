package tech.brownbear.server;

import com.google.common.collect.ImmutableMap;

import java.io.File;
import java.util.Map;

public interface ResponseRenderer {
    HtmlRenderer htmlRenderer();
    FileRenderer fileRenderer();

    void renderText(String text);

    void renderHtml(String templateName, Map<String, Object> args);

    void renderHtml(String templateName);

    default void renderHtml(Map<String, Object> ij, String templateName) {
        htmlRenderer().render(ij, templateName);
    }

    default void renderHtml(Map<String, Object> ij, String templateName, Map<String, Object> args) {
        htmlRenderer().render(ij, templateName, args);
    }

    default void renderHtml(Map<String, Object> ij, String templateName, String name, Object obj) {
        renderHtml(ij, templateName, ImmutableMap.of(name, obj));
    }

    void renderHtml(String templateName, String name, Object obj);

    default void renderFile(File file, FileFormat format) {
        fileRenderer().render(file, format);
    }
}
