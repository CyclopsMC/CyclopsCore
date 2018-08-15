package org.cyclops.cyclopscore.infobook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.Helpers;

/**
 * An advanced button type.
 * @author rubensworks
 */
@SideOnly(Side.CLIENT)
public class AdvancedButton extends GuiButton {

    private InfoSection target;
    protected GuiInfoBook gui;

    public AdvancedButton() {
        super(-1, 0, 0, "");
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * This is called each render tick to update the button to the latest render state.
     * @param x The X position.
     * @param y The Y position.
     * @param displayName The text to display.
     * @param target The target section.
     * @param gui The gui.
     */
    public void update(int x, int y, String displayName, InfoSection target, GuiInfoBook gui) {
        this.x = x;
        this.y = y;
        this.displayString = displayName;
        this.target = target;
        this.gui = gui;
        this.width = 16;
        this.height = 16;
        this.enabled = isVisible();
    }

    @Override
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        if(isVisible() && isHover(mouseX, mouseY)) {
            minecraft.fontRenderer.drawString(("§n") +
                            displayString + "§r", x, y,
                    Helpers.RGBToInt(100, 100, 150));
        }
    }

    /**
     * Render the button tooltip.
     * @param mx Mouse x.
     * @param my Mouse Y.
     */
    public void renderTooltip(int mx, int my) {

    }

    protected boolean isHover(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y &&
                mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    public boolean isVisible() {
        return this.visible && getTarget() != null;
    }

    public InfoSection getTarget() {
        return this.target;
    }

    public void onClick() {

    }

}
