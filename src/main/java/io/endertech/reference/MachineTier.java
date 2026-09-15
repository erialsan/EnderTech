package io.endertech.reference;

import net.minecraft.item.EnumRarity;

public enum MachineTier {

    CREATIVE,
    REDSTONE,
    RESONANT;

    public EnumRarity getRarity() {
        switch (this) {
            case CREATIVE:
                return EnumRarity.epic;
            case REDSTONE:
                return EnumRarity.uncommon;
            case RESONANT:
                return EnumRarity.rare;
            default:
                return EnumRarity.common;
        }
    }

    public String textureName() {
        switch (this) {
            case CREATIVE:
                return "Creative";
            case REDSTONE:
                return "Redstone";
            case RESONANT:
                return "Resonant";
            default:
                return "Unknown";
        }
    }

    public static EnumRarity rarityForMeta(int meta) {
        MachineTier[] values = values();
        if (meta >= 0 && meta < values.length) {
            return values[meta].getRarity();
        }
        return EnumRarity.common;
    }

    public static String textureNameForMeta(int meta) {
        MachineTier[] values = values();
        if (meta >= 0 && meta < values.length) {
            return values[meta].textureName();
        }
        return "Unknown";
    }
}
