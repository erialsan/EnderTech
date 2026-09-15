package io.endertech;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.endertech.block.ETBlocks;
import io.endertech.config.ConfigHandler;
import io.endertech.config.GeneralConfig;
import io.endertech.creativetab.CreativeTabET;
import io.endertech.gui.GuiHandler;
import io.endertech.item.ETItems;
import io.endertech.network.PacketHandler;
import io.endertech.network.PacketKeyPressed;
import io.endertech.network.PacketTile;
import io.endertech.proxy.CommonProxy;
import io.endertech.recipe.ThermalExpansionRecipes;
import io.endertech.reference.Reference;
import io.endertech.util.Exchange;
import io.endertech.util.fluid.BucketHandler;
import io.endertech.util.helper.BlockHelper;
import io.endertech.util.helper.LocalisationHelper;
import io.endertech.util.helper.LogHelper;

@Mod(
    modid = Reference.MOD_ID,
    name = Reference.MOD_NAME,
    version = Reference.VERSION_NUMBER,
    certificateFingerprint = Reference.FINGERPRINT,
    dependencies = "required-after:CoFHCore@[1.7.10R3.1.0,);after:ThermalExpansion@[1.7.10R4.0.0B1,)")
public class EnderTech {

    public static final CreativeTabs tabET = new CreativeTabET();
    public static final GuiHandler guiHandler = new GuiHandler();
    @SuppressWarnings("unused")
    @Mod.Instance(Reference.MOD_ID)
    public static EnderTech instance;
    @SidedProxy(clientSide = "io.endertech.proxy.ClientProxy", serverSide = "io.endertech.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static boolean loadDevModeContent = false;
    public static Item capacitor;

    @EventHandler
    @SuppressWarnings("unused")
    public void invalidFingerprint(FMLFingerprintViolationEvent event) {
        if (Reference.FINGERPRINT.equals("@FINGERPRINT@")) {
            LogHelper.warn(LocalisationHelper.localiseString("warning.fingerprint.missing"));
        } else {
            LogHelper.fatal(LocalisationHelper.localiseString("error.fingerprint.tampered"));
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(
            event.getModConfigurationDirectory()
                .getAbsolutePath() + File.separator
                + Reference.CHANNEL_NAME.toLowerCase()
                + File.separator);

        LogHelper.debug("Loaded config");

        if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") || GeneralConfig.forceLoadDevContent) {
            loadDevModeContent = true;
        }

        PacketHandler.instance.init();

        proxy.registerTickerHandlers();

        ETBlocks.init();

        ETItems.init();

        BucketHandler.initialize();

        LogHelper.debug("preInit complete");
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);

        PacketTile.init();
        PacketKeyPressed.init();

        proxy.registerTESRs();

        proxy.registerRenderers();

        proxy.registerItemRenderers();

        FMLInterModComms.sendMessage(
            "Waila",
            "register",
            "io.endertech.integration.waila.MultiblockWailaProvider.callbackRegister");
        FMLInterModComms
            .sendMessage("Waila", "register", "io.endertech.integration.waila.GenericWailaProvider.callbackRegister");

        LogHelper.debug("init complete");

        MinecraftForge.EVENT_BUS.register(proxy);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent event) {
        PacketHandler.instance.postInit();

        proxy.registerClientPostInit();

        BlockHelper.initSoftBlocks();
        Exchange.initSpecialBlocks();

        LogHelper.info(LocalisationHelper.localiseString("info.postinit.recipes"));
        ThermalExpansionRecipes.register();
    }

    @EventHandler
    public void onIdMapping(FMLModIdMappingEvent event) {
        BucketHandler.refreshMap();
    }
}
