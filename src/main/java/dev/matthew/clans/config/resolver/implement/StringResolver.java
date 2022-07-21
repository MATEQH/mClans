package dev.matthew.clans.config.resolver.implement;

import dev.matthew.clans.config.resolver.ValueResolver;

public class StringResolver implements ValueResolver<String> {

    @Override
    public String resolve(Object value) {
        return value.toString();
    }
}
