package io.endertech.block;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import io.endertech.reference.Strings;
import io.endertech.reference.Textures;
import io.endertech.tile.TileChargePad;

public class BlockChargePad extends BlockPad {

    public static ItemStack itemChargePadCreative;
    public static ItemStack itemChargePadResonant;
    public static ItemStack itemChargePadRedstone;

    public BlockChargePad() {
        super();

        this.setBlockName(Strings.Blocks.CHARGE_PAD);
    }

    public void init() {
        TileChargePad.init();

        itemChargePadCreative = new ItemStack(this, 1, 0);
        itemChargePadResonant = new ItemStack(this, 1, 2);
        itemChargePadRedstone = new ItemStack(this, 1, 1);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileChargePad();
    }

    @Override
    protected String getIconTypePrefix() {
        return "ChargePad";
    }

    @Override
    protected String getTextureBase() {
        return Textures.CHARGE_PAD_BASE;
    }
}
