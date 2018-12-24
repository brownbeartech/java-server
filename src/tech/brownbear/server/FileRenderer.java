package tech.brownbear.server;

import java.io.File;

public interface FileRenderer {
    void render(File file, FileFormat format);
}
