package org.cyclops.cyclopscore.client.gui.image;

import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Default images provided by this mod.
 * @author rubensworks
 */
public class Images {

    public static final ResourceLocation ICONS = new ResourceLocation(CyclopsCore._instance.getModId(),
            CyclopsCore._instance.getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI) + "icons.png");

    public static final Image ARROW_DOWN = new Image(ICONS, 18, 0, 18, 18);
    public static final Image ARROW_UP = new Image(ICONS, 36, 0, 18, 18);
    public static final Image ANVIL = new Image(ICONS, 18, 18, 18, 18);
    public static final Image CONFIG_BOARD = new Image(ICONS, 36, 18, 18, 18);
    public static final Image PICKAXE = new Image(ICONS, 54, 18, 18, 18);
    public static final Image SHOVEL_BROKEN = new Image(ICONS, 72, 18, 18, 18);

}
