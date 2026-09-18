package io.endertech.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import cofh.api.energy.IEnergyContainerItem;
import io.endertech.block.ItemBlockBasic;
import io.endertech.reference.MachineTier;
import io.endertech.tile.TilePad;
import io.endertech.util.helper.KeyHelper;
import io.endertech.util.helper.LocalisationHelper;
import io.endertech.util.helper.StringHelper;

public abstract class ItemBlockPad extends ItemBlockBasic implements IEnergyContainerItem {

    public ItemBlockPad(Block block) {
        super(block);

        this.setMaxStackSize(1);
    }

    protected abstract int getCapacity(int meta);

    protected abstract int getReceive(int meta);

    protected abstract void addExtraShiftInfo(ItemStack stack, List<String> list);

    @Override
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return MachineTier.rarityForMeta(par1ItemStack.getItemDamage());
    }

    public void checkAndSetDefaultTag(ItemStack stack) {
        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
            TilePad.writeDefaultTag(stack.stackTagCompound);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean check) {
        super.addInformation(stack, player, list, check);

        if (KeyHelper.isShiftDown()) {
            this.checkAndSetDefaultTag(stack);

            if (isCreative(stack)) {
                list.add(LocalisationHelper.localiseString("info.charge", "Infinite"));
            } else {
                list.add(
                    LocalisationHelper.localiseString(
                        "info.charge",
                        StringHelper.getEnergyString(this.getEnergyStored(stack)) + " / "
                            + StringHelper.getEnergyString(this.getMaxEnergyStored(stack))
                            + " RF"));
            }

            if (!isCreative(stack)) {
                list.add(
                    LocalisationHelper.localiseString(
                        "info.charge.receive",
                        StringHelper.getEnergyString(getReceive(stack.getItemDamage())) + " RF/t"));
            }

            addExtraShiftInfo(stack, list);
        } else {
            list.add(StringHelper.holdShiftForDetails);
        }
    }

    private boolean isCreative(ItemStack stack) {
        return stack.getItemDamage() == 0;
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        this.checkAndSetDefaultTag(container);

        int energy = container.stackTagCompound.getInteger("Energy");
        int energyReceived = Math.min(
            this.getMaxEnergyStored(container) - energy,
            Math.min(getReceive(container.getItemDamage()), maxReceive));

        if (!simulate) {
            energy += energyReceived;
            container.stackTagCompound.setInteger("Energy", energy);
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
            return 0;
        }
        return container.stackTagCompound.getInteger("Energy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return getCapacity(container.getItemDamage());
    }
}
