package io.endertech.tile;

import java.util.*;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.ServerHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.endertech.fx.EntityChargePadFX;
import io.endertech.gui.client.GuiPad;
import io.endertech.gui.container.ContainerPad;
import io.endertech.network.PacketETBase;
import io.endertech.reference.Strings;
import io.endertech.util.helper.LocalisationHelper;
import io.endertech.util.helper.StringHelper;

public class TileChargePad extends TilePad {

    public static final int[] RECEIVE = { 0, 1 * 2000, 10 * 2000 };
    public static final int[] SEND = { 10 * 1000000, 1 * 2000, 10 * 2000 };
    public static final int[] CAPACITY = { -1, 1 * 2000000, 10 * 1000000 };

    public int sentPower = 0;

    public TileChargePad() {
        super();

        this.tileName = "Charge Pad";
    }

    public static void init() {
        GameRegistry.registerTileEntity(TileChargePad.class, "tile." + Strings.Blocks.CHARGE_PAD);
    }

    public int getMaxEnergyStored(int meta) {
        return CAPACITY[meta];
    }

    public int getMaxReceiveRate(int meta) {
        return RECEIVE[meta];
    }

    public int getMaxSendRate(int meta) {
        return SEND[meta];
    }

    @Override
    public String toString() {
        return "Charge Pad: position " + this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
    }

    public Set<ItemStack> chargeableItemsInInventory(ItemStack[] itemStacks) {
        Set<ItemStack> itemsToCharge = new HashSet<ItemStack>();

        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) continue;

            Item item = itemStack.getItem();
            if (item instanceof IEnergyContainerItem) {
                IEnergyContainerItem chargeableItem = (IEnergyContainerItem) item;
                if (chargeableItem.receiveEnergy(itemStack, 1, true) == 1) itemsToCharge.add(itemStack);
            }
        }

        return itemsToCharge;
    }

    public List<Entity> getChargeableEntitiesInAABB(AxisAlignedBB aabb) {
        List<Entity> chargeableEntitiesInRange = new ArrayList<Entity>(
            this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb));
        List<EntityItem> chargeableItemsInRange = this.worldObj.getEntitiesWithinAABB(EntityItem.class, aabb);
        for (EntityItem entityItem : chargeableItemsInRange) {
            ItemStack itemStack = entityItem.getEntityItem();
            if (itemStack != null) {
                Item item = itemStack.getItem();
                if (item != null) {
                    if (item instanceof IEnergyContainerItem) chargeableEntitiesInRange.add(entityItem);
                }
            }
        }

        return chargeableEntitiesInRange;
    }

    public List<ItemStack> getItemsToChargeFromEntity(Entity entity) {
        LinkedList<ItemStack> itemsToCharge = new LinkedList<ItemStack>();

        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            itemsToCharge.addAll(this.chargeableItemsInInventory(player.inventory.mainInventory));
            itemsToCharge.addAll(this.chargeableItemsInInventory(player.inventory.armorInventory));
        } else if (entity instanceof EntityItem) {
            EntityItem entityItem = (EntityItem) entity;
            ItemStack item = entityItem.getEntityItem();

            if (item.stackSize == 1) itemsToCharge.add(item);
        }

        return itemsToCharge;
    }

    public int chargeItemsGivenEntity(Entity entity, int maxCharge, int meta) {
        List<ItemStack> itemsToCharge = this.getItemsToChargeFromEntity(entity);
        double efficiency = this.calculateEfficiencyForEntity(entity);

        int totalSent = 0;
        int itemCount = itemsToCharge.size();
        if (itemCount > 0) {
            int chargePerItem = (int) Math.floor(maxCharge / itemCount);
            if (chargePerItem == 0 && maxCharge > 0) chargePerItem = 1;

            for (ItemStack itemStack : itemsToCharge) {
                IEnergyContainerItem chargeableItem = (IEnergyContainerItem) itemStack.getItem();
                int couldReceive = chargeableItem.receiveEnergy(itemStack, chargePerItem, true);
                int toSend = this.extractEnergy(couldReceive, meta, false);
                if (this.isCreative) toSend = couldReceive;

                int sent = chargeableItem.receiveEnergy(itemStack, (int) (toSend * efficiency), false);
                if (sent > 0 && entity instanceof EntityItem) {
                    EntityItem entityItem = (EntityItem) entity;
                    if (entityItem.lifespan < Integer.MAX_VALUE) entityItem.lifespan = Integer.MAX_VALUE;
                }

                totalSent += sent;
            }

            if (totalSent >= maxCharge) return maxCharge;
        }

        return totalSent;
    }

    @Override
    public void updateEntity() {
        int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        this.isCreative = (meta == 0);

        if (ServerHelper.isServerWorld(this.worldObj)) {
            boolean oldActive = this.isActive;
            this.isActive = this.sentPower > 0;

            sentPower = 0;
            int totalChargeSendable = this.extractEnergy(getMaxSendRate(meta), meta, true);
            if (this.isCreative) totalChargeSendable = SEND[0];

            if (totalChargeSendable > 0) {
                AxisAlignedBB front = this.getAABBInFront(2);
                List<Entity> ownersInRange = this.getChargeableEntitiesInAABB(front);

                int totalChargeForEntity = (int) (((double) totalChargeSendable) / ownersInRange.size());
                if (ownersInRange.size() > 0) {
                    for (Entity entity : ownersInRange) {
                        int powerSentToEntity = this.chargeItemsGivenEntity(entity, totalChargeForEntity, meta);
                        sentPower += powerSentToEntity;
                    }
                }
            }

            this.chargeFromGUISlot();

            this.tickAndSync(this.isActive != oldActive);
        }

        if (this.sentPower > 0 && ServerHelper.isClientWorld(this.worldObj)) this.spawnParticles(meta);
    }

    @Override
    public PacketETBase getPacket() {
        PacketETBase packet = super.getPacket();
        packet.addInt(this.sentPower);

        return packet;
    }

    @Override
    public void handleTilePacket(PacketETBase tilePacket, boolean isServer) {
        super.handleTilePacket(tilePacket, isServer);

        int sentPower = tilePacket.getInt();

        if (!isServer) {
            this.sentPower = sentPower;
        }
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip) {
        currenttip = super.getWailaBody(itemStack, currenttip);
        currenttip.add(
            LocalisationHelper.localiseString("info.sent", StringHelper.getEnergyString(this.sentPower) + " RF/t"));
        return currenttip;
    }

    @SideOnly(Side.CLIENT)
    protected EntityFX createParticle(World world, double x, double y, double z, int maxAge, double[] velocity,
        float[] colour, float sizeModifier) {
        return new EntityChargePadFX(world, x, y, z, maxAge, velocity, colour, sizeModifier);
    }

    @SideOnly(Side.CLIENT)
    public int getParticleCount(int meta) {
        if (meta == 0) return 5;
        else if (meta == 2) return 2;
        else return 1;
    }

    @SideOnly(Side.CLIENT)
    public float getParticleSizeModifier(int meta) {
        if (meta == 0) return 2.0F;
        else if (meta == 2) return 1.75F;
        else return 1.5F;
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
