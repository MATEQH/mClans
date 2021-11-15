package com.github.mateqh.config.resolver;

public interface ValueResolver<T> {

    T resolve(Object value);
}
