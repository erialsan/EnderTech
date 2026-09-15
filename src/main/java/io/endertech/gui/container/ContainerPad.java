package io.endertech.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

import cofh.lib.gui.slot.SlotEnergy;
import io.endertech.tile.TilePad;

public class ContainerPad extends ContainerETBase {

    public ContainerPad(InventoryPlayer inventoryPlayer, TileEntity tileEntity) {
        super(inventoryPlayer, tileEntity);

        TilePad pad = (TilePad) tileEntity;
        this.addSlotToContainer(new SlotEnergy(pad, pad.getChargeSlot(), 8, 53));
    }
}
