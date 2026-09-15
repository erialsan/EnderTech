package io.endertech.fx;

import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityChargePadFX extends EntityPadFX {

    public EntityChargePadFX(World world, double x, double y, double z, int maxAge, double[] velocity, float[] colour,
        float sizeModifier) {
        super(world, x, y, z, maxAge, velocity, colour, sizeModifier);

        setParticleTextureIndex(0);

        this.particleScale = 2F;
        this.particleScale += (this.rand.nextFloat() * 0.2F);
    }
}
