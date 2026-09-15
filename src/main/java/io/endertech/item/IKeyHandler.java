package io.endertech.item;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import io.endertech.util.Key;

public interface IKeyHandler {

    public abstract void handleKey(EntityPlayer player, ItemStack itemStack, Key.KeyCode key);

    public abstract Set<Key.KeyCode> getHandledKeys();
}
