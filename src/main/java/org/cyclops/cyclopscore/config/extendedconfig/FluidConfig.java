package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Config for fluids.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class FluidConfig extends ExtendedConfigForge<FluidConfig, Fluid> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabledDefault     If this should is enabled by default. If this is false, this can still
     *                           be enabled through the config file.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param elementConstructor The element constructor.
     */
    public FluidConfig(ModBase mod, boolean enabledDefault, String namedId,
                       String comment, Function<FluidConfig, ? extends Fluid> elementConstructor) {
        super(mod, enabledDefault, namedId, comment, elementConstructor);
    }

    @Override
	public String getTranslationKey() {
		return "fluid.fluids." + getMod().getModId() + "." + getNamedId();
	}
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.FLUID;
	}
    
    @Override
    public boolean isDisableable() {
        return false;
    }

    /**
     * Get the still icon location.
     * @return The icon location.
     */
    public ResourceLocation getIconLocationStill() {
        return new ResourceLocation(getMod().getModId(), "blocks/" + getNamedId() + "_still");
    }

    /**
     * Get the flow icon location.
     * @return The icon location.
     */
    public ResourceLocation getIconLocationFlow() {
        return new ResourceLocation(getMod().getModId(), "blocks/" + getNamedId() + "_flow");
    }

    @Nullable
    @Override
    public IForgeRegistry<Fluid> getRegistry() {
        return ForgeRegistries.FLUIDS;
    }
}
