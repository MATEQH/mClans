package dev.matthew.config.resolver.implement;

import dev.matthew.config.resolver.ValueResolver;

public class IntegerResolver implements ValueResolver<Integer> {

    @Override
    public Integer resolve(Object value) {
        return Integer.valueOf(value.toString());
    }
}
