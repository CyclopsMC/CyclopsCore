package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.function.Consumer;
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
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public FluidConfig(ModBase mod, String namedId, Function<FluidConfig, ? extends Fluid> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    protected static ForgeFlowingFluid.Properties getDefaultFluidProperties(ModBase mod, String texturePrefixPath,
                                                                            Consumer<FluidAttributes.Builder> fluidAttributesConsumer) {
        FluidAttributes.Builder fluidAttributes = FluidAttributes.builder(
                new ResourceLocation(mod.getModId(), texturePrefixPath + "_still.png"),
                new ResourceLocation(mod.getModId(), texturePrefixPath + "_flow.png")
        );
        fluidAttributesConsumer.accept(fluidAttributes);

        Wrapper<ForgeFlowingFluid.Properties> properties = new Wrapper<>();
        properties.set(new ForgeFlowingFluid.Properties(
                () -> new ForgeFlowingFluid.Source(properties.get()),
                () -> new ForgeFlowingFluid.Flowing(properties.get()),
                fluidAttributes
        ));
        return properties.get();
    }

    @Override
	public String getTranslationKey() {
		return "fluid.fluids." + getMod().getModId() + "." + getNamedId();
	}
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.FLUID;
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
