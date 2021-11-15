package com.github.mateqh.config.resolver.implement;

import com.github.mateqh.config.resolver.ValueResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BooleanListResolver implements ValueResolver<List<Boolean>> {

    @Override
    public List<Boolean> resolve(Object value) {
        return new ArrayList<>((Collection<Boolean>) value);
    }
}
