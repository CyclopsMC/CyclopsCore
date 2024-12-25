package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Config for fluids.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class FluidConfigNeoForge extends ExtendedConfigCommon<FluidConfigNeoForge, BaseFlowingFluid.Properties, ModBaseNeoForge<?>> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public FluidConfigNeoForge(ModBaseNeoForge<?> mod, String namedId, Function<FluidConfigNeoForge, BaseFlowingFluid.Properties> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    protected static BaseFlowingFluid.Properties getDefaultFluidProperties(ModBaseNeoForge<?> mod, String texturePrefixPath,
                                                                           Consumer<FluidType.Properties> fluidAttributesConsumer) {
        FluidType.Properties fluidAttributes = FluidType.Properties.create();
        fluidAttributesConsumer.accept(fluidAttributes);
        FluidType fluidType = new FluidType(fluidAttributes);
        mod.getModEventBus().addListener((RegisterClientExtensionsEvent event) -> {
            event.registerFluidType(new IClientFluidTypeExtensions() {
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
            }, fluidType);
        });

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
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypesNeoForge.FLUID;
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
