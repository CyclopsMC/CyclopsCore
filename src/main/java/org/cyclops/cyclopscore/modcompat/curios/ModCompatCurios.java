package org.cyclops.cyclopscore.modcompat.curios;

import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;

/**
 * @author rubensworks
 */
public class ModCompatCurios implements IModCompat {
    @Override
    public String getId() {
        return Reference.MOD_CURIOS;
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "Inventory iteration over curios slots";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return new ModCompatCuriosInitializer();
    }
}
