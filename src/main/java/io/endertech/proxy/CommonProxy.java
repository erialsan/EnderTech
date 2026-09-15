package io.endertech.proxy;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.endertech.handler.WorldEventHandler;
import io.endertech.multiblock.handler.MultiblockEventHandler;
import io.endertech.multiblock.handler.MultiblockServerTickHandler;

public class CommonProxy {

    public static int connectedTexturesRenderID = 0;

    public void registerTickerHandlers() {
        FMLCommonHandler.instance()
            .bus()
            .register(new WorldEventHandler());
        FMLCommonHandler.instance()
            .bus()
            .register(new MultiblockServerTickHandler());
        MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());
    }

    public void registerTESRs() {}

    public void registerRenderers() {}

    public void registerItemRenderers() {}

    public void registerClientPostInit() {}

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerIcons(TextureStitchEvent.Pre event) {}

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void initializeIcons(TextureStitchEvent.Post event) {}
}
