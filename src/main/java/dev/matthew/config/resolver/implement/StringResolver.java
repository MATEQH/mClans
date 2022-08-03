package dev.matthew.config.resolver.implement;

import dev.matthew.config.resolver.ValueResolver;

public class StringResolver implements ValueResolver<String> {

    @Override
    public String resolve(Object value) {
        return value.toString();
    }
}
