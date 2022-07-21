package dev.matthew.clans.config.resolver;

public interface ValueResolver<T> {

    T resolve(Object value);
}
