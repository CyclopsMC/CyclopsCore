package org.cyclops.cyclopscore.client.gui.image;

import lombok.Data;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

/**
 * A wrapper that contains a reference to a {@link net.minecraft.util.ResourceLocation} and its sheet position.
 * @author rubensworks
 */
@Data
public class Image implements IImage {

    private final ResourceLocation resourceLocation;
    private final int sheetX, sheetY, sheetWidth, sheetHeight;

    @Override
    public void draw(Gui gui, int x, int y) {
        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
        gui.drawTexturedModalRect(x, y, sheetX, sheetY, sheetWidth, sheetHeight);
    }

}
