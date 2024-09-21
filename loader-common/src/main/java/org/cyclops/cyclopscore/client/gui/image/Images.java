package org.cyclops.cyclopscore.client.gui.image;

import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;

/**
 * Default images provided by this mod.
 * @author rubensworks
 */
public class Images {

    public static final ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath(CyclopsCoreInstance.MOD.getModId(),
            "textures/gui/icons.png");

    public static final ResourceLocation BUTTONS = ResourceLocation.fromNamespaceAndPath(CyclopsCoreInstance.MOD.getModId(),
            "textures/gui/buttons.png");

    public static final ResourceLocation WIDGETS = ResourceLocation.fromNamespaceAndPath(CyclopsCoreInstance.MOD.getModId(),
            "textures/gui/widgets.png");

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
    public static final Image LOCKED = new Image(ICONS, 144, 18, 10, 14);

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
    /**
     * 0: default; 1: hovered; 2: checked
     */
    public static final Image[] CHECKBOX = new Image[] {
            new Image(ICONS, 90, 18, 10, 10),
            new Image(ICONS, 108, 18, 10, 10),
            new Image(ICONS, 126, 18, 10, 10),
    };

}
