package org.cyclops.cyclopscore.infobook.condition;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.util.Strings;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Condition handler for checking if configs are enabled.
 * @author rubensworks
 *
 */
public class ConfigSectionConditionHandler implements ISectionConditionHandler {

    @Override
    public boolean isSatisfied(ModBase<?> mod, String param) {
        String modId = null;
        if (param.contains(":")) {
            String[] split = param.split(":");
            modId = split[0];
            param = split[1];
        }
        if (!Strings.isEmpty(modId)) {
            ModContainer untypedMod = ModList.get().getModContainerById(modId).orElse(null);
            if (!(untypedMod.getMod() instanceof ModBase)) {
                throw new IllegalArgumentException("The mod " + modId + " is not of type ModBase.");
            }
            mod = (ModBase) untypedMod.getMod();
        }
        ExtendedConfig<?, ?> config = mod.getConfigHandler().getDictionary().get(param);
        return config != null;
    }

}
