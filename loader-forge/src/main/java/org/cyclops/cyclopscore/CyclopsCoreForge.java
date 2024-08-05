package org.cyclops.cyclopscore;

import net.minecraftforge.fml.common.Mod;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class CyclopsCoreForge extends ModBaseForge<CyclopsCoreForge> {

    /**
     * The unique instance of this mod.
     */
    public static CyclopsCoreForge _instance;

    public CyclopsCoreForge() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
    }

}
