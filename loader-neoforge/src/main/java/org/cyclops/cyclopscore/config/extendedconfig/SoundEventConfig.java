package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.sounds.SoundEvent;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for sound events.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class SoundEventConfig extends SoundEventConfigCommon<ModBase<?>>{
    public SoundEventConfig(ModBase<?> mod, String namedId, Function<SoundEventConfigCommon<ModBase<?>>, SoundEvent> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
