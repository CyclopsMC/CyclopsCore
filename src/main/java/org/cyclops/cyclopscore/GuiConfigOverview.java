package org.cyclops.cyclopscore;

import net.minecraft.client.gui.GuiScreen;
import org.cyclops.cyclopscore.client.gui.config.ExtendedConfigGuiFactoryBase;
import org.cyclops.cyclopscore.client.gui.config.GuiConfigOverviewBase;

/**
 * @author rubensworks
 */
public class GuiConfigOverview extends GuiConfigOverviewBase {
    /**
     * Make a new instance.
     * @param parentScreen the parent GuiScreen object
     */
    public GuiConfigOverview(GuiScreen parentScreen) {
        super(CyclopsCore._instance, parentScreen);
    }

    public static class ExtendedConfigGuiFactory extends ExtendedConfigGuiFactoryBase {

        @Override
        public Class<? extends GuiConfigOverviewBase> mainConfigGuiClass() {
            return GuiConfigOverview.class;
        }
    }
}
