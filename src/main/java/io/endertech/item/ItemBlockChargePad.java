package io.endertech.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import io.endertech.tile.TileChargePad;
import io.endertech.util.helper.LocalisationHelper;
import io.endertech.util.helper.StringHelper;

public class ItemBlockChargePad extends ItemBlockPad {

    public ItemBlockChargePad(Block block) {
        super(block);
    }

    @Override
    protected int getCapacity(int meta) {
        return TileChargePad.CAPACITY[meta];
    }

    @Override
    protected int getReceive(int meta) {
        return TileChargePad.RECEIVE[meta];
    }

    @Override
    protected void addExtraShiftInfo(ItemStack stack, List<String> list) {
        list.add(
            LocalisationHelper.localiseString(
                "info.charge.send",
                StringHelper.getEnergyString(TileChargePad.SEND[stack.getItemDamage()]) + " RF/t"));
    }
}
