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

    public static final ResourceLocation BUTTONS = new ResourceLocation(CyclopsCore._instance.getModId(),
            CyclopsCore._instance.getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI) + "buttons.png");

    public static final ResourceLocation WIDGETS = new ResourceLocation(CyclopsCore._instance.getModId(),
            CyclopsCore._instance.getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI) + "widgets.png");

    public static final Image ARROW_DOWN = new Image(ICONS, 18, 0, 18, 18);
    public static final Image ARROW_UP = new Image(ICONS, 36, 0, 18, 18);
    public static final Image ARROW_RIGHT = new Image(ICONS, 90, 0, 18, 18);
    public static final Image ARROW_LEFT = new Image(ICONS, 108, 0, 18, 18);
    public static final Image ANVIL = new Image(ICONS, 18, 18, 18, 18);
    public static final Image CONFIG_BOARD = new Image(ICONS, 36, 18, 18, 18);
    public static final Image PICKAXE = new Image(ICONS, 54, 18, 18, 18);
    public static final Image SHOVEL_BROKEN = new Image(ICONS, 72, 18, 18, 18);
    public static final Image ERROR = new Image(ICONS, 54, 0, 13, 13);
    public static final Image OK = new Image(ICONS, 72, 0, 14, 12);

    /**
     * 0: default; 1: active; 2: disabled
     */
    public static final Image[] BUTTON_ARROW_RIGHT = new Image[] {
            new Image(BUTTONS, 20, 0, 10, 15),
            new Image(BUTTONS, 0, 0, 10, 15),
            new Image(BUTTONS, 10, 0, 10, 15),
    };
    /**
     * 0: default; 1: active; 2: disabled
     */
    public static final Image[] BUTTON_ARROW_LEFT = new Image[] {
            new Image(BUTTONS, 20, 15, 10, 15),
            new Image(BUTTONS, 0, 15, 10, 15),
            new Image(BUTTONS, 10, 15, 10, 15),
    };
    /**
     * 0: default; 1: active; 2: disabled
     */
    public static final Image[] BUTTON_ARROW_DOWN = new Image[] {
            new Image(BUTTONS, 0, 50, 15, 10),
            new Image(BUTTONS, 0, 30, 15, 10),
            new Image(BUTTONS, 0, 40, 15, 10),
    };
    /**
     * 0: default; 1: active; 2: disabled
     */
    public static final Image[] BUTTON_ARROW_UP = new Image[] {
            new Image(BUTTONS, 15, 50, 15, 10),
            new Image(BUTTONS, 15, 30, 15, 10),
            new Image(BUTTONS, 15, 40, 15, 10),
    };

}
