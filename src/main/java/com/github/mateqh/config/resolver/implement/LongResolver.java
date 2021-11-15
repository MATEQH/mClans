package com.github.mateqh.config.resolver.implement;

import com.github.mateqh.config.resolver.ValueResolver;

public class LongResolver implements ValueResolver<Long> {

    @Override
    public Long resolve(Object value) {
        return Long.valueOf(value.toString());
    }
}
