package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for sound events.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class SoundEventConfig extends ExtendedConfigForge<SoundEventConfig, SoundEvent>{

    public SoundEventConfig(ModBase mod, String namedId, Function<SoundEventConfig, SoundEvent> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "sound_events." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.SOUND_EVENT;
    }

    @Override
    public Registry<? super SoundEvent> getRegistry() {
        return BuiltInRegistries.SOUND_EVENT;
    }
}
