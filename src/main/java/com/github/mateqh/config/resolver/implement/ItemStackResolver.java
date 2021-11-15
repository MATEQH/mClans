package com.github.mateqh.config.resolver.implement;

import com.github.mateqh.config.resolver.ValueResolver;
import org.bukkit.inventory.ItemStack;

public class ItemStackResolver implements ValueResolver<ItemStack> {

    @Override
    public ItemStack resolve(Object value) {
        return (ItemStack) value;
    }
}
