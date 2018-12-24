package tech.brownbear.server;

@FunctionalInterface
public interface Handler<Application, Session> {
    void handle(Application a, Exchange<Session> e);
}
