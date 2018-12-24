package tech.brownbear.server;

import io.undertow.server.handlers.Cookie;

import java.util.Date;

public abstract class ImmutableCookie implements Cookie {
    protected final String name;
    protected final String value;
    protected final Date expires;

    public ImmutableCookie(String name, String value, Date expires) {
        this.name = name;
        this.value = value;
        this.expires = expires;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Date getExpires() {
        return expires;
    }

    @Override
    public Cookie setValue(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie setPath(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie setDomain(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie setMaxAge(Integer integer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie setDiscard(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie setSecure(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie setVersion(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie setHttpOnly(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie setExpires(Date date) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cookie setComment(String s) {
        throw new UnsupportedOperationException();
    }
}
