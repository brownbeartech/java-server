package tech.brownbear.server;

public enum FileFormat {
    PNG("image/png");

    private final String mimeType;

    FileFormat(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
