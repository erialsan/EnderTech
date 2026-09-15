package io.endertech.tile;

import java.util.List;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cofh.lib.util.helpers.ServerHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.endertech.config.GeneralConfig;
import io.endertech.fx.EntityHealthPadFX;
import io.endertech.gui.client.GuiPad;
import io.endertech.gui.container.ContainerPad;
import io.endertech.network.PacketETBase;
import io.endertech.reference.Strings;

public class TileHealthPad extends TilePad {

    public static final int[] RECEIVE = { 0, 1 * 2000, 10 * 2000 };
    public static final int[] CAPACITY = { -1, 1 * 2000000, 10 * 1000000 };
    public static final int[] TICKS_PER_HEAL = { 10, 40, 20 };
    public static byte particleSkip = 0;
    public int ticksSinceLastHealthSent = 0;

    public float sentHealth = 0;

    public TileHealthPad() {
        super();

        this.tileName = "Health Pad";
    }

    public static void init() {
        GameRegistry.registerTileEntity(TileHealthPad.class, "tile." + Strings.Blocks.HEALTH_PAD);
    }

    public int getMaxEnergyStored(int meta) {
        return CAPACITY[meta];
    }

    public int getMaxReceiveRate(int meta) {
        return RECEIVE[meta];
    }

    public int getMaxSendRate(int meta) {
        return GeneralConfig.healthPadChargeCostPerHalfHeart * 2;
    }

    @Override
    public String toString() {
        return "Health Pad: position " + this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
    }

    @Override
    public void updateEntity() {
        int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        this.isCreative = (meta == 0);

        if (ServerHelper.isServerWorld(this.worldObj)) {
            boolean oldActive = this.isActive;
            this.isActive = this.sentHealth > 0;
            this.sentHealth = 0;

            int totalChargeUseable = this.extractEnergy(getMaxSendRate(meta), meta, true);
            float totalHealthSendable = MathHelper
                .floor_float((float) totalChargeUseable / GeneralConfig.healthPadChargeCostPerHalfHeart);
            if (this.isCreative) totalHealthSendable = 8.0F;

            if (totalHealthSendable >= 1) {
                AxisAlignedBB front = this.getAABBInFront(2);
                List<EntityLivingBase> healableEntitiesInRange = this.worldObj
                    .getEntitiesWithinAABB(EntityLivingBase.class, front);
                int healableEntityCount = healableEntitiesInRange.size();
                if (healableEntityCount > 0) {
                    float healthPerEntity = MathHelper.floor_float(1.0F / healableEntityCount);
                    for (EntityLivingBase entity : healableEntitiesInRange) {
                        if ((entity.getHealth() < entity.getMaxHealth() && entity.isEntityAlive())
                            || GeneralConfig.healthPadForceAlwaysSendHealth) {
                            float efficiency = (float) this.calculateEfficiencyForEntity(entity);
                            if (this.ticksSinceLastHealthSent > TICKS_PER_HEAL[meta]
                                && worldObj.rand.nextInt(TICKS_PER_HEAL[meta] / 4) == 0) {
                                int costForHalfHeart = MathHelper
                                    .floor_float(GeneralConfig.healthPadChargeCostPerHalfHeart * healthPerEntity);
                                costForHalfHeart += (1 - efficiency) * costForHalfHeart;
                                int couldExtract = this.extractEnergy(costForHalfHeart, meta, true);
                                if (this.isCreative || couldExtract == costForHalfHeart) {
                                    this.extractEnergy(costForHalfHeart, meta, false);

                                    // LogHelper.info("Health per entity: " + healthPerEntity);
                                    // LogHelper.info("Efficiency: " + efficiency);
                                    // LogHelper.info("Healing half a heart cost: " + couldExtract);

                                    entity.heal(1);
                                    this.ticksSinceLastHealthSent = 0;
                                } else {
                                    // LogHelper.info("Tried to extract " + costForHalfHeart + " managed " +
                                    // couldExtract + ", not healing");
                                }
                            } else {
                                if (this.ticksSinceLastHealthSent < 100) this.ticksSinceLastHealthSent++;
                            }

                            sentHealth += (healthPerEntity * efficiency);
                        }
                    }
                } else {
                    this.ticksSinceLastHealthSent = 0;
                }
            }

            this.chargeFromGUISlot();

            this.tickAndSync(this.isActive != oldActive);
        }

        if (this.sentHealth > 0 && ServerHelper.isClientWorld(this.worldObj)) {
            particleSkip++;

            if (particleSkip == 3) {
                this.spawnParticles(meta);
                particleSkip = 0;
            }
        }
    }

    @Override
    public PacketETBase getPacket() {
        PacketETBase packet = super.getPacket();
        packet.addFloat(this.sentHealth);
        packet.addInt(this.ticksSinceLastHealthSent);

        return packet;
    }

    @Override
    public void handleTilePacket(PacketETBase tilePacket, boolean isServer) {
        super.handleTilePacket(tilePacket, isServer);

        float sentHealth = tilePacket.getFloat();
        int ticksSinceLastHealthSent = tilePacket.getInt();

        if (!isServer) {
            this.sentHealth = sentHealth;
            this.ticksSinceLastHealthSent = ticksSinceLastHealthSent;
        }
    }

    @SideOnly(Side.CLIENT)
    protected EntityFX createParticle(World world, double x, double y, double z, int maxAge, double[] velocity,
        float[] colour, float sizeModifier) {
        return new EntityHealthPadFX(world, x, y, z, maxAge, velocity, colour, sizeModifier);
    }

    @SideOnly(Side.CLIENT)
    public int getParticleCount(int meta) {
        if (meta == 0) return 2;
        else if (meta == 2) return 2;
        else return 1;
    }

    @SideOnly(Side.CLIENT)
    public float getParticleSizeModifier(int meta) {
        if (meta == 0) return 0.9F;
        else if (meta == 2) return 0.75F;
        else return 0.6F;
    }

    @Override
    public Object getGuiClient(InventoryPlayer inventory) {
        return new GuiPad(inventory, this);
    }

    @Override
    public Object getGuiServer(InventoryPlayer inventory) {
        return new ContainerPad(inventory, this);
    }
}
