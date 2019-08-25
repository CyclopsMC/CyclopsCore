package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.potion.Effect;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for potion effects.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class EffectConfig extends ExtendedConfigForge<EffectConfig, Effect> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabledDefault     If this should is enabled by default. If this is false, this can still
     *                           be enabled through the config file.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param elementConstructor The element constructor.
     */
    public EffectConfig(ModBase mod, boolean enabledDefault, String namedId,
                        String comment, Function<EffectConfig, ? extends Effect> elementConstructor) {
        super(mod, enabledDefault, namedId, comment, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "potions." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.EFFECT;
    }

    @Override
    public IForgeRegistry<Effect> getRegistry() {
        return ForgeRegistries.POTIONS;
    }

}
