package com.github.mateqh.config.resolver.implement;

import com.github.mateqh.config.resolver.ValueResolver;

public class BooleanResolver implements ValueResolver<Boolean> {

    @Override
    public Boolean resolve(Object value) {
        return Boolean.valueOf(value.toString());
    }
}
