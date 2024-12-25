package org.cyclops.cyclopscore.infobook.condition;

import net.neoforged.fml.ModList;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * Condition handler for checking if mods are available.
 * @author rubensworks
 *
 */
public class ModSectionConditionHandler implements ISectionConditionHandler {

    @Override
    public boolean isSatisfied(ModBaseNeoForge<?> mod, String param) {
        return ModList.get().isLoaded(param);
    }

}
