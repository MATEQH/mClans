package com.github.mateqh.config.resolver.implement;

import com.github.mateqh.config.resolver.ValueResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LongListResolver implements ValueResolver<List<Long>> {

    @Override
    public List<Long> resolve(Object value) {
        return new ArrayList<>((Collection<Long>) value);
    }
}
