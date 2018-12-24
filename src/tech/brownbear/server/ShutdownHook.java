package tech.brownbear.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ShutdownHook {
    protected static Logger logger = LoggerFactory.getLogger(ShutdownHook.class);
    protected List<AutoCloseable> connections = new ArrayList<>();

    public ShutdownHook() {
        addShutdownHook();
    }

    public <T extends AutoCloseable> T register(ConnectionProvider<T> c) {
        T connection = null;
        try {
            connection = c.get();
            connections.add(connection);
        } catch(Exception e) {
            logger.error("Error registering class '" + connection.getClass().getSimpleName() + "'");
        }
        logger.info("Registered class '" + connection.getClass().getSimpleName() + "'");
        return connection; 
    }

    @FunctionalInterface
    public interface ConnectionProvider<T extends AutoCloseable> {
        T get();
    }
 
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Running Shutdown Hook");
            for (AutoCloseable connection : connections) {
                try {
                    connection.close();
                } catch (Exception e) {
                    logger.error("Failed to close class '" + connection.getClass().getSimpleName() + "'");
                }
                logger.info("Closing class '" + connection.getClass().getSimpleName() + "'");
            }
        }));
    }
}
