package io.endertech.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static Configuration configuration;

    public static void init(String configPath) {
        GeneralConfig.init(new File(configPath + "general.cfg"));
        ItemConfig.init(new File(configPath + "items.cfg"));
    }
}
