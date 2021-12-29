package org.cyclops.cyclopscore.infobook.condition;

import net.minecraftforge.fml.ModList;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Condition handler for checking if mods are available.
 * @author rubensworks
 *
 */
public class ModSectionConditionHandler implements ISectionConditionHandler {

    @Override
    public boolean isSatisfied(ModBase<?> mod, String param) {
        return ModList.get().isLoaded(param);
    }

}
