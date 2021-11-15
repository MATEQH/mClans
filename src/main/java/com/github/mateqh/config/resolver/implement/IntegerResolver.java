package com.github.mateqh.config.resolver.implement;

import com.github.mateqh.config.resolver.ValueResolver;

public class IntegerResolver implements ValueResolver<Integer> {

    @Override
    public Integer resolve(Object value) {
        return Integer.valueOf(value.toString());
    }
}
