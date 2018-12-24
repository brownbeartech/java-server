package tech.brownbear.server;

import java.util.Collections;
import java.util.Map;

/**
 * Render some html
 */
public interface HtmlRenderer {
    default void render(String templateName, Map<String, Object> args) {
        render(Collections.emptyMap(), templateName, args);
    }

    default void render(String templateName) {
        render(templateName, Collections.emptyMap());
    }

    default void render(Map<String, Object> ij, String templateName) {
        render(ij, templateName, Collections.emptyMap());
    }

    void render(Map<String, Object> ij, String templateName, Map<String, Object> args);
}