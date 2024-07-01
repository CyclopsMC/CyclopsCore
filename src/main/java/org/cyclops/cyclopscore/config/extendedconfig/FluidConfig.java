package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Config for fluids.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class FluidConfig extends ExtendedConfig<FluidConfig, BaseFlowingFluid.Properties> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public FluidConfig(ModBase mod, String namedId, Function<FluidConfig, BaseFlowingFluid.Properties> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    protected static BaseFlowingFluid.Properties getDefaultFluidProperties(ModBase mod, String texturePrefixPath,
                                                                            Consumer<FluidType.Properties> fluidAttributesConsumer) {
        FluidType.Properties fluidAttributes = FluidType.Properties.create();
        fluidAttributesConsumer.accept(fluidAttributes);
        FluidType fluidType = new FluidType(fluidAttributes) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions()
                {
                    private final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath(mod.getModId(), texturePrefixPath + "_still");
                    private final ResourceLocation FLOW = ResourceLocation.fromNamespaceAndPath(mod.getModId(), texturePrefixPath + "_flow");

                    @Override
                    public ResourceLocation getStillTexture()
                    {
                        return STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture()
                    {
                        return FLOW;
                    }
                });
            }
        };

        Wrapper<BaseFlowingFluid.Properties> properties = new Wrapper<>();
        final Wrapper<Fluid> source = new Wrapper<>();
        final Wrapper<Fluid> flowing = new Wrapper<>();
        properties.set(new BaseFlowingFluid.Properties(
                () -> fluidType,
                () -> {
                    if (source.get() == null) {
                        source.set(new BaseFlowingFluid.Source(properties.get()));
                    }
                    return source.get();
                },
                () -> {
                    if (flowing.get() == null) {
                        flowing.set(new BaseFlowingFluid.Flowing(properties.get()));
                    }
                    return flowing.get();
                }
        ));
        return properties.get();
    }

    @Override
    public String getTranslationKey() {
        return "block." + getMod().getModId() + ".block_" + getNamedId();
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
        return ResourceLocation.fromNamespaceAndPath(getMod().getModId(), "blocks/" + getNamedId() + "_still");
    }

    /**
     * Get the flow icon location.
     * @return The icon location.
     */
    public ResourceLocation getIconLocationFlow() {
        return ResourceLocation.fromNamespaceAndPath(getMod().getModId(), "blocks/" + getNamedId() + "_flow");
    }

}
