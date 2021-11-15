package com.github.mateqh.config.resolver.implement;

import com.github.mateqh.config.resolver.ValueResolver;

public class StringResolver implements ValueResolver<String> {

    @Override
    public String resolve(Object value) {
        return value.toString();
    }
}
