package io.endertech.util.helper;

import io.endertech.reference.MachineTier;

public class TextureHelper {

    public static String metaToType(int meta) {
        return MachineTier.textureNameForMeta(meta);
    }
}
