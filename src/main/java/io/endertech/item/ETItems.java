package io.endertech.item;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import io.endertech.reference.MachineTier;
import io.endertech.reference.Strings;

public class ETItems {

    public static ItemExchanger itemExchanger;
    public static ItemStack toolExchangerCreative;
    public static ItemStack toolExchangerRedstone;
    public static ItemStack toolExchangerResonant;

    public static void init() {
        itemExchanger = (ItemExchanger) new ItemExchanger().setUnlocalizedName(Strings.EXCHANGER_BASE);

        GameRegistry.registerItem(itemExchanger, "endertech." + Strings.EXCHANGER_BASE);

        loadItems();
    }

    public static void loadItems() {
        toolExchangerCreative = itemExchanger.addItem(MachineTier.CREATIVE.ordinal(), Strings.EXCHANGER_CREATIVE);
        toolExchangerRedstone = itemExchanger.addItem(MachineTier.REDSTONE.ordinal(), Strings.EXCHANGER_REDSTONE);
        toolExchangerResonant = itemExchanger.addItem(MachineTier.RESONANT.ordinal(), Strings.EXCHANGER_RESONANT);
    }
}
