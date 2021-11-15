package com.github.mateqh.config.resolver.implement;

import com.github.mateqh.config.resolver.ValueResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringListResolver implements ValueResolver<List<String>> {

    @Override
    public List<String> resolve(Object value) {
        return new ArrayList<>((Collection<String>) value);
    }
}
