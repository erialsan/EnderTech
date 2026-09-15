package io.endertech.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

import cofh.lib.gui.slot.SlotEnergy;
import io.endertech.tile.TileChargePad;

public class ContainerChargePad extends ContainerETBase {

    TileChargePad tileChargePad;

    public ContainerChargePad(InventoryPlayer inventoryPlayer, TileEntity tileEntity) {
        super(inventoryPlayer, tileEntity);

        this.tileChargePad = ((TileChargePad) tileEntity);
        this.addSlotToContainer(new SlotEnergy(this.tileChargePad, this.tileChargePad.getChargeSlot(), 8, 53));
    }
}
