package dev.matthew.config.resolver;

public interface ValueResolver<T> {

    T resolve(Object value);
}
