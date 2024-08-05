package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.item.CreativeModeTab;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for creative mode tabs.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public class CreativeModeTabConfig extends CreativeModeTabConfigCommon<ModBase<?>>{
    public CreativeModeTabConfig(ModBase<?> mod, String namedId, Function<CreativeModeTabConfigCommon<ModBase<?>>, CreativeModeTab> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
