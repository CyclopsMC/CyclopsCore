package org.cyclops.cyclopscore.modcompat.baubles;

import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;

public class ModCompatBaubles implements IModCompat {

    @Override
    public String getId() {
        return Reference.MOD_BAUBLES;
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "Inventory iteration over baubles slots";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return new ModCompatBaublesInitializer();
    }

}
