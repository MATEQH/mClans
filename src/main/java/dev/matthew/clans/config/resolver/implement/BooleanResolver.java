package dev.matthew.clans.config.resolver.implement;

import dev.matthew.clans.config.resolver.ValueResolver;

public class BooleanResolver implements ValueResolver<Boolean> {

    @Override
    public Boolean resolve(Object value) {
        return Boolean.valueOf(value.toString());
    }
}
