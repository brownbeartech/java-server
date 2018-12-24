package tech.brownbear.server;

import java.util.ArrayList;
import java.util.List;

public class HandlerChain<Application, Session> {
    private final List<Handler<Application, Session>> chain;

    private HandlerChain(List<Handler<Application, Session>> chain) {
        this.chain = chain;
    }

    public static <Application, Session> HandlerChain<Application, Session> begin(Handler<Application, Session> begin) {
        List<Handler<Application, Session>> chain = new ArrayList<>();
        chain.add(begin);
        return new HandlerChain<>(chain);
    }

    public HandlerChain<Application, Session> next(Handler<Application, Session> next) {
        chain.add(next);
        return this;
    }

    public Handler<Application, Session> compose(Handler<Application, Session> end) {
        return (e, a) -> {
            chain.forEach(c -> c.handle(e, a));
            end.handle(e, a);
        };
    }
}
