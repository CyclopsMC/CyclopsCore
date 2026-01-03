package org.cyclops.cyclopscore.modcompat.almostunified;

import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;

public class ModCompatAlmostUnified implements IModCompat {
    @Override
    public String getId() {
        return Reference.MOD_ALMOSTUNIFIED;
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "Recipe unification";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return new ModCompatAlmostUnifiedInitializer();
    }
}
