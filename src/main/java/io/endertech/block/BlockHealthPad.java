package io.endertech.block;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import io.endertech.reference.Strings;
import io.endertech.reference.Textures;
import io.endertech.tile.TileHealthPad;

public class BlockHealthPad extends BlockPad {

    public static ItemStack itemHealthPadCreative;
    public static ItemStack itemHealthPadResonant;
    public static ItemStack itemHealthPadRedstone;

    public BlockHealthPad() {
        super();

        this.setBlockName(Strings.Blocks.HEALTH_PAD);
    }

    public void init() {
        TileHealthPad.init();

        itemHealthPadCreative = new ItemStack(this, 1, 0);
        itemHealthPadResonant = new ItemStack(this, 1, 2);
        itemHealthPadRedstone = new ItemStack(this, 1, 1);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileHealthPad();
    }

    @Override
    protected String getIconTypePrefix() {
        return "HealthPad";
    }

    @Override
    protected String getTextureBase() {
        return Textures.HEALTH_PAD_BASE;
    }
}
