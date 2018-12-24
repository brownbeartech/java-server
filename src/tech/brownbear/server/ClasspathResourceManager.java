package tech.brownbear.server;

import io.undertow.UndertowMessages;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.server.handlers.resource.URLResource;

import tech.brownbear.resources.ClasspathResourceFetcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

public class ClasspathResourceManager implements ResourceManager {
    private final ClasspathResourceFetcher sourcer;

    public ClasspathResourceManager(
        Class<?> clazz,
        Set<String> directories) {
        this.sourcer = new ClasspathResourceFetcher(
            clazz,
            prefixDirectories(clazz, directories));
    }

    private Set<String> prefixDirectories(Class<?> clazz, Set<String> directories) {
        String dir = clazz.getPackage().getName().replace(".", File.separator);
        return directories.stream()
            .map(d -> Paths.get(File.separator, dir, d).toString())
            .collect(Collectors.toSet());
    }

    @Override
    public Resource getResource(String s) throws IOException {

        return sourcer.find(s).map(u -> new URLResource(u, s)).orElse(null);
    }

    @Override
    public boolean isResourceChangeListenerSupported() {
        return false;
    }

    @Override
    public void registerResourceChangeListener(ResourceChangeListener resourceChangeListener) {
        throw UndertowMessages.MESSAGES.resourceChangeListenerNotSupported();
    }

    @Override
    public void removeResourceChangeListener(ResourceChangeListener resourceChangeListener) {
        throw UndertowMessages.MESSAGES.resourceChangeListenerNotSupported();
    }

    @Override
    public void close() throws IOException { }
}
