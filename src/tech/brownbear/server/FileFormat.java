package tech.brownbear.server;

public enum FileFormat {
    PNG("image/png"),
    JSON("application/json"),
    CSV("text/csv");

    private final String mimeType;

    FileFormat(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
