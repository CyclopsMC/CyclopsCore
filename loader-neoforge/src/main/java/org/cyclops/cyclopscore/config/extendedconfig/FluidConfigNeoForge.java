package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Config for fluids.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class FluidConfigNeoForge extends ExtendedConfigCommon<FluidConfigNeoForge, BaseFlowingFluid.Properties, ModBaseNeoForge<?>> {

    private FluidClientConfigNeoForge clientConfig;

    private Fluid sourceFluid;
    private Fluid flowingFluid;

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public FluidConfigNeoForge(ModBaseNeoForge<?> mod, String namedId, Function<FluidConfigNeoForge, BaseFlowingFluid.Properties> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    public Fluid getSourceFluid() {
        return sourceFluid;
    }

    public Fluid getFlowingFluid() {
        return flowingFluid;
    }

    protected static BaseFlowingFluid.Properties getDefaultFluidProperties(FluidConfigNeoForge config, Consumer<FluidType.Properties> fluidAttributesConsumer) {
        FluidType.Properties fluidAttributes = FluidType.Properties.create();
        fluidAttributesConsumer.accept(fluidAttributes);
        FluidType fluidType = new FluidType(fluidAttributes);

        Wrapper<BaseFlowingFluid.Properties> properties = new Wrapper<>();
        final Wrapper<Fluid> source = new Wrapper<>();
        final Wrapper<Fluid> flowing = new Wrapper<>();
        properties.set(new BaseFlowingFluid.Properties(
                () -> fluidType,
                () -> {
                    if (source.get() == null) {
                        source.set(new BaseFlowingFluid.Source(properties.get()));
                        config.sourceFluid = source.get();
                    }
                    return source.get();
                },
                () -> {
                    if (flowing.get() == null) {
                        flowing.set(new BaseFlowingFluid.Flowing(properties.get()));
                        config.flowingFluid = flowing.get();
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

    @Nullable
    public FluidClientConfigNeoForge constructFluidClientConfigNeoForge() {
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            return new FluidClientConfigNeoForge(this);
        }
        return null;
    }

    @Nullable
    public final FluidClientConfigNeoForge getFluidClientConfigNeoForge() {
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            if (this.clientConfig == null) {
                this.clientConfig = constructFluidClientConfigNeoForge();
            }
            return this.clientConfig;
        }
        return null;
    }

    @Override
    public void onRegistryRegistered() {
        super.onRegistryRegistered();
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            getMod().getModEventBus().addListener(getFluidClientConfigNeoForge()::onRegisterFluidModels);
        }
    }
}
