package dev.matthew.config.resolver.implement;

import dev.matthew.config.resolver.ValueResolver;

public class BooleanResolver implements ValueResolver<Boolean> {

    @Override
    public Boolean resolve(Object value) {
        return Boolean.valueOf(value.toString());
    }
}
