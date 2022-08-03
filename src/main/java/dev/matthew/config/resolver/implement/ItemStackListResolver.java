package dev.matthew.config.resolver.implement;

import dev.matthew.config.resolver.ValueResolver;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemStackListResolver implements ValueResolver<List<ItemStack>> {

    @Override
    public List<ItemStack> resolve(Object value) {
        return (List<ItemStack>) value;
    }
}
