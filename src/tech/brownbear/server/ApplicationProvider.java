package tech.brownbear.server;

import tech.brownbear.soy.SoyTemplateRenderer;

@FunctionalInterface
public interface ApplicationProvider<Application> {
    Application provide(SoyTemplateRenderer renderer);
}