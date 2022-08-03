package dev.matthew.config.resolver.implement;

import dev.matthew.config.resolver.ValueResolver;
import org.bukkit.inventory.ItemStack;

public class ItemStackResolver implements ValueResolver<ItemStack> {

    @Override
    public ItemStack resolve(Object value) {
        return (ItemStack) value;
    }
}
