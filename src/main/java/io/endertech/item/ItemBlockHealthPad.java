package io.endertech.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import io.endertech.tile.TileHealthPad;

public class ItemBlockHealthPad extends ItemBlockPad {

    public ItemBlockHealthPad(Block block) {
        super(block);
    }

    @Override
    protected int getCapacity(int meta) {
        return TileHealthPad.CAPACITY[meta];
    }

    @Override
    protected int getReceive(int meta) {
        return TileHealthPad.RECEIVE[meta];
    }

    @Override
    protected void addExtraShiftInfo(ItemStack stack, List<String> list) {
        list.add("Ticks per heal: " + TileHealthPad.TICKS_PER_HEAL[stack.getItemDamage()]);
    }
}
