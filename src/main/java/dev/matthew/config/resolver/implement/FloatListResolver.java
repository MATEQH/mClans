package dev.matthew.config.resolver.implement;

import dev.matthew.config.resolver.ValueResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FloatListResolver implements ValueResolver<List<Float>> {

    @Override
    public List<Float> resolve(Object value) {
        return new ArrayList<>((Collection<Float>) value);
    }
}
