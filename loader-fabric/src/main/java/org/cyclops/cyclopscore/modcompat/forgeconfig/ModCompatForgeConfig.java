package org.cyclops.cyclopscore.modcompat.forgeconfig;

import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;

/**
 * @author rubensworks
 */
public class ModCompatForgeConfig implements IModCompat {
    @Override
    public String getId() {
        return "forgeconfigapiport";
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "Forge Config API Port support";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return new ModCompatForgeConfigInitializer();
    }
}
