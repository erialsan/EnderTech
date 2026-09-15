package io.endertech.gui.client;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import cofh.lib.gui.element.ElementEnergyStored;
import io.endertech.gui.container.ContainerPad;
import io.endertech.gui.element.ElementIcon;
import io.endertech.tile.TileET;
import io.endertech.tile.TilePad;

public class GuiPad extends GuiETBase {

    public static final String TEXTURE_PATH = "endertech:textures/gui/ChargePad.png";
    public static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
    public TilePad tilePad;
    private ElementIcon elementChargingIcon;

    public GuiPad(InventoryPlayer inventoryPlayer, TileET tileEntity) {
        super(new ContainerPad(inventoryPlayer, tileEntity), TEXTURE, tileEntity);

        this.tilePad = (TilePad) tileEntity;
        this.name = this.tilePad.getName();
    }

    @Override
    public void initGui() {
        super.initGui();

        ElementEnergyStored elementEnergyStored = new ElementEnergyStored(this, 8, 8, this.tilePad);
        this.addElement(elementEnergyStored);

        elementChargingIcon = new ElementIcon(this, 80, 30);
        this.addElement(elementChargingIcon);
    }

    @Override
    protected void updateElementInformation() {
        elementChargingIcon.setIconToDraw(tilePad.getFrontIcon());
    }
}
