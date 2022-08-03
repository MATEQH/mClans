package dev.matthew.config.resolver.implement;

import dev.matthew.config.resolver.ValueResolver;

public class LongResolver implements ValueResolver<Long> {

    @Override
    public Long resolve(Object value) {
        return Long.valueOf(value.toString());
    }
}
