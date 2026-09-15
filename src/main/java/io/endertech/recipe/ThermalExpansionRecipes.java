package io.endertech.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import io.endertech.EnderTech;
import io.endertech.block.BlockChargePad;
import io.endertech.block.BlockHealthPad;
import io.endertech.item.ETItems;
import io.endertech.multiblock.block.BlockMultiblockGlass;
import io.endertech.multiblock.block.BlockTankController;
import io.endertech.multiblock.block.BlockTankPart;
import io.endertech.util.helper.LocalisationHelper;
import io.endertech.util.helper.LogHelper;

public class ThermalExpansionRecipes {

    public static void register() {
        if (Loader.isModLoaded("ThermalExpansion")) {
            ItemStack capacitorReinforced = GameRegistry.findItemStack("ThermalExpansion", "capacitorReinforced", 1);
            ItemStack capacitorResonant = GameRegistry.findItemStack("ThermalExpansion", "capacitorResonant", 1);
            EnderTech.capacitor = capacitorResonant.getItem();
            ItemStack powerCoilElectrumStack = GameRegistry.findItemStack("ThermalExpansion", "powerCoilElectrum", 1);
            ItemStack tesseract = new ItemStack(GameRegistry.findBlock("ThermalExpansion", "Tesseract"));
            ItemStack enderiumIngot = GameRegistry.findItemStack("ThermalFoundation", "ingotEnderium", 1);
            ItemStack electrumIngot = GameRegistry.findItemStack("ThermalFoundation", "ingotElectrum", 1);
            ItemStack enderiumNugget = GameRegistry.findItemStack("ThermalFoundation", "nuggetEnderium", 1);
            ItemStack machineResonant = new ItemStack(GameRegistry.findBlock("ThermalExpansion", "Frame"), 1, 3);
            ItemStack machineRedstone = new ItemStack(GameRegistry.findBlock("ThermalExpansion", "Frame"), 1, 2);
            ItemStack hardenedGlass = new ItemStack(GameRegistry.findBlock("ThermalExpansion", "Glass"));
            ItemStack tankResonant = new ItemStack(GameRegistry.findBlock("ThermalExpansion", "Tank"), 1, 4);

            ItemStack enderEyeStack = new ItemStack(Items.ender_eye);
            ItemStack goldenApple = new ItemStack(Items.golden_apple);

            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ETItems.toolExchangerRedstone,
                    new Object[] { "XEX", "ITI", "XCX", 'E', enderEyeStack, 'I', electrumIngot, 'C',
                        capacitorReinforced, 'T', tesseract }));
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ETItems.toolExchangerResonant,
                    new Object[] { "XEX", "ITI", "XCX", 'E', enderEyeStack, 'I', enderiumIngot, 'C', capacitorResonant,
                        'T', tesseract }));
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ETItems.toolExchangerResonant,
                    new Object[] { "XSX", "IEI", "XCX", 'S', enderEyeStack, 'E', ETItems.toolExchangerRedstone, 'I',
                        enderiumIngot, 'C', capacitorResonant, 'T', tesseract }));

            ItemStack enderTankFrame = new ItemStack(
                BlockTankPart.itemBlockTankFrame.getItem(),
                8,
                BlockTankPart.itemBlockTankFrame.getItemDamage());
            ItemStack enderTankEnergyInput = new ItemStack(
                BlockTankPart.itemBlockTankEnergyInput.getItem(),
                8,
                BlockTankPart.itemBlockTankEnergyInput.getItemDamage());
            ItemStack enderTankValve = new ItemStack(
                BlockTankPart.itemBlockTankValve.getItem(),
                8,
                BlockTankPart.itemBlockTankValve.getItemDamage());
            ItemStack enderTankGlass = new ItemStack(
                BlockMultiblockGlass.itemBlockMultiblockGlass.getItem(),
                16,
                BlockMultiblockGlass.itemBlockMultiblockGlass.getItemDamage());
            ItemStack enderTankController = BlockTankController.itemBlockTankController;

            GameRegistry.addRecipe(
                enderTankFrame,
                new Object[] { "IEI", "EFE", "IEI", 'I', enderiumNugget, 'F', machineResonant, 'E', enderEyeStack });
            GameRegistry.addRecipe(
                enderTankEnergyInput,
                new Object[] { "ICI", "EFE", "ITI", 'I', enderiumNugget, 'F', machineResonant, 'C', capacitorResonant,
                    'T', tesseract, 'E', enderEyeStack });
            GameRegistry.addRecipe(
                enderTankValve,
                new Object[] { "IAI", "EFE", "ITI", 'I', enderiumNugget, 'A', tankResonant, 'F', machineResonant, 'T',
                    tesseract, 'E', enderEyeStack });
            GameRegistry.addRecipe(
                enderTankController,
                new Object[] { "IEI", "EFE", "ITI", 'I', enderiumNugget, 'F', machineResonant, 'T', tesseract, 'E',
                    enderEyeStack });
            GameRegistry.addRecipe(
                enderTankGlass,
                new Object[] { "GIG", "EFE", "GIG", 'I', enderiumNugget, 'G', hardenedGlass, 'F', machineResonant, 'E',
                    enderEyeStack });

            GameRegistry.addRecipe(
                BlockChargePad.itemChargePadResonant,
                new Object[] { "IEI", "CFC", "IAI", 'I', enderiumIngot, 'F', machineResonant, 'E', enderEyeStack, 'C',
                    powerCoilElectrumStack, 'T', tesseract, 'A', capacitorResonant });
            GameRegistry.addRecipe(
                BlockChargePad.itemChargePadRedstone,
                new Object[] { "IEI", "CFC", "IAI", 'I', electrumIngot, 'F', machineRedstone, 'E', enderEyeStack, 'C',
                    powerCoilElectrumStack, 'T', tesseract, 'A', capacitorReinforced });

            GameRegistry.addRecipe(
                BlockHealthPad.itemHealthPadResonant,
                new Object[] { "IEI", "CFC", "IAI", 'I', enderiumIngot, 'F', machineResonant, 'E', enderEyeStack, 'C',
                    goldenApple, 'T', tesseract, 'A', capacitorResonant });
            GameRegistry.addRecipe(
                BlockHealthPad.itemHealthPadRedstone,
                new Object[] { "IEI", "CFC", "IAI", 'I', electrumIngot, 'F', machineRedstone, 'E', enderEyeStack, 'C',
                    goldenApple, 'T', tesseract, 'A', capacitorReinforced });
        } else {
            LogHelper.warn(LocalisationHelper.localiseString("warning.thermalexpansion.missing"));
        }
    }
}
