package org.cyclops.cyclopscore;

import net.fabricmc.api.ModInitializer;
import org.cyclops.cyclopscore.init.ModBaseFabric;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 *
 */
public class CyclopsCoreMainFabric extends ModBaseFabric<CyclopsCoreMainFabric> implements ModInitializer {

    /**
     * The unique instance of this mod.
     */
    public static CyclopsCoreMainFabric _instance;

    public CyclopsCoreMainFabric() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
    }

    @Override
    protected boolean hasDefaultCreativeModeTab() {
        return true;
    }
}
