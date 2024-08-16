package org.cyclops.cyclopscore.infobook.condition;

import org.apache.logging.log4j.util.Strings;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigCommon;
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
            mod = ModBase.get(modId);
            if (mod == null) {
                throw new IllegalArgumentException("The mod " + modId + " could not be found as ModBase.");
            }
        }
        ExtendedConfigCommon<?, ?, ?> config = mod.getConfigHandler().getDictionary().get(param);
        return config != null;
    }

}
